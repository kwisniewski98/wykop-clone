import requests
import json
from datetime import  datetime
from util import authorization

VYKOP_URL = "http://localhost:8080"


def test_subvykop_posts(authorization):
    res = requests.get(f"{VYKOP_URL}/posts/Some%20subvykop?page=0", headers={"Authorization": authorization})
    with open("expected_values/posts/posts_subvykop_1.json") as f:
        expected = json.load(f)
    assert res.json() == expected


def test_posts_search(authorization):
    res = requests.get(f"{VYKOP_URL}/posts/Some%20subvykop/search", data="test", headers={"Authorization": authorization})
    assert res.ok and res.json() == ["Kolejny test"]


def test_userposts(authorization):
    res = requests.get(f"{VYKOP_URL}/userposts?page=0", headers={"Authorization": authorization})
    assert res.ok
    with open("expected_values/posts/posts_subvykop_1.json") as f:
        expected = json.load(f)
    assert res.json() == expected


def test_get_post(authorization):
    post_id = 1
    res = requests.get(f"{VYKOP_URL}/post?id={post_id}", headers={"Authorization": authorization})
    assert res.ok
    with open(f"expected_values/posts/post_{post_id}.json", "r") as f:
        expected = json.load(f)
    assert res.json() == expected


def test_comment_post(authorization):
    post_id = 1
    comment_text = "comment test"
    res = requests.post(f"{VYKOP_URL}/posts/{post_id}/comment", data=comment_text, headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/post?id={post_id}", headers={"Authorization": authorization})
    assert res.ok and comment_text in [comment["text"] for comment in res.json()["comments"]]


def test_upvote_post(authorization):
    post_id = 1
    comment_text = "comment test"
    res = requests.post(f"{VYKOP_URL}/posts/upvote/{post_id}", data=comment_text, headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/post?id={post_id}", headers={"Authorization": authorization})
    assert res.ok and res.json()["upvoted"]


def test_create_post(authorization):
    multipart_form_data = {
        "file": ("zaba.png", open("expected_values/smutna_zaba.png", "rb"), "image/png"),
    }
    res = requests.post(f"{VYKOP_URL}/posts?title=test&text=test&subVykop=Some%20subvykop",
                        files=multipart_form_data, headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/post?id=4", headers={"Authorization": authorization})
    print(res.json())
    with open("expected_values/posts/created_post.json") as f:
        expected = json.load(f)
    expected["creationDate"] = datetime.today().strftime('%Y-%m-%d')
    assert res.ok and res.json() == expected
