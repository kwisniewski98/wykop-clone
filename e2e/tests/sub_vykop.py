import requests
import json
from util import authorization

VYKOP_URL = "http://localhost:8080"


def test_suvvykop_by_id(authorization):
    sub_vykop_id = 1
    res = requests.get(f"{VYKOP_URL}/sub_vykop/{sub_vykop_id}", headers={"Authorization": authorization})
    assert res.ok
    with open(f"expected_values/sub_vykop/sub_vykop_{sub_vykop_id}.json") as f:
        expected = json.load(f)
    assert res.json() == expected


def test_suvvykop_is_subscribed(authorization):
    sub_vykop_id = 1
    res = requests.get(f"{VYKOP_URL}/subvykop/{sub_vykop_id}/isSubscribed", headers={"Authorization": authorization})
    assert res.ok and res.text == "false"


def test_suvvykop_find(authorization):
    sub_vykop_id = 1
    res = requests.get(f"{VYKOP_URL}/sub_vykop/search?match=so", headers={"Authorization": authorization})
    assert res.ok
    with open(f"expected_values/sub_vykop/sub_vykop_{sub_vykop_id}.json") as f:
        expected = [json.load(f)]
    assert res.json() == expected


def test_suvvykop_subscribe(authorization):
    sub_vykop_id = 1
    res = requests.post(f"{VYKOP_URL}/subvykop/subscribe", data=str(sub_vykop_id),
                        headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/subvykop/{sub_vykop_id}/isSubscribed", headers={"Authorization": authorization})
    assert res.ok and res.text == "true"


def test_subvykop_create(authorization):
    expected = [{
        'id': 2,
        'name': 'test',
        'description': 'test',
        'banner': 'http://localhost:8080/files/zaba.png',
        'avatar': 'http://localhost:8080/files/zaba.png'}]
    multipart_form_data = {
        "banner": ("zaba.png", open("expected_values/smutna_zaba.png", "rb")),
        "avatar": ("zaba.png", open("expected_values/smutna_zaba.png", "rb"))
    }
    res = requests.post(f"{VYKOP_URL}/sub_vykop?name=test&description=test",
                        files=multipart_form_data, headers={"Authorization": authorization})

    assert res.ok

    res = requests.get(f"{VYKOP_URL}/sub_vykop/search?match=test", headers={"Authorization": authorization})
    assert res.ok

    assert expected == res.json()

def test_serve_file(authorization):
    with open("expected_values/smutna_zaba.png", "rb") as f:
        expected = f.read()
    res = requests.get(f"{VYKOP_URL}/files/zaba.png", headers={"Authorization": authorization})
    print(res.content)
    assert res.ok and res.content == expected