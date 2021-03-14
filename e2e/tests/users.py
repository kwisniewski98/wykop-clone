from datetime import datetime

import requests
import json
from util import authorization

VYKOP_URL = "http://localhost:8080"

def test_users_by_id(authorization):
    user_id = 1
    res = requests.get(f"{VYKOP_URL}/users/{user_id}", headers={"Authorization": authorization})
    assert res.ok
    with open(f"expected_values/users/user_{user_id}.json") as f:
        expected = json.load(f)
    assert res.ok and res.json() == expected


def test_users_get_all(authorization):
    res = requests.get(f"{VYKOP_URL}/users", headers={"Authorization": authorization})
    with open(f"expected_values/users/users.json") as f:
        expected = json.load(f)
    assert res.ok and res.json() == expected


def test_users_new_user(authorization):
    payload = {
        "username": "dasdsadsa",
        "password": "!Password123",
        "email": "dasdsadsa@test.com",
        "role": "user",
    }
    res = requests.post(f"{VYKOP_URL}/users", json=payload, headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/users", headers={"Authorization": authorization})

    assert res.ok and len(res.json()) == 4

def test_user_register(authorization):
    body = {
        "email": "test@test.com",
        "username": "user3",
        "password": "!Password123"
        }
    resp = requests.post(f"{VYKOP_URL}/users/signup", json=body)
    assert resp.ok

    res = requests.get(f"{VYKOP_URL}/users", headers={"Authorization": authorization})

    assert res.ok and len(res.json()) == 5

def test_user_register_same_username():
    body = {
        "email": "test@test.com",
        "username": "user3",
        "password": "!Password123"
    }
    resp = requests.post(f"{VYKOP_URL}/users/signup", json=body)
    assert resp.status_code != 200


def test_user_login():
    body = {
        "username": "admin",
        "password": "password"
        }
    resp = requests.post(f"{VYKOP_URL}/users/login", json=body)
    assert resp.ok and resp.headers.get("Authorization")


def test_user_stats(authorization):
    res = requests.get(f"{VYKOP_URL}/users/stats", headers={"Authorization": authorization})
    assert res.ok
    with open(f"expected_values/users/user_1_stats.json") as f:
        expected = json.load(f)
    assert res.json() == expected


def test_user_posts(authorization):
    res = requests.get(f"{VYKOP_URL}/users/posts", headers={"Authorization": authorization})
    assert res.ok
    with open(f"expected_values/users/user_1_posts.json") as f:
        expected = json.load(f)
    expected[1]["creationDate"] = datetime.today().strftime('%Y-%m-%d')
    assert res.json() == expected


def test_users_me(authorization):
    res = requests.get(f"{VYKOP_URL}/users/me", headers={"Authorization": authorization})
    assert res.ok
    with open(f"expected_values/users/me.json") as f:
        expected = json.load(f)
    assert res.json() == expected


def test_user_by_username(authorization):
    res = requests.get(f"{VYKOP_URL}/u/admin", headers={"Authorization": authorization})
    assert res.ok
    with open(f"expected_values/users/me.json") as f:
        expected = json.load(f)
    assert res.json() == expected


def test_users_replace_user(authorization):
    user_id = 2
    data = {
        "password": "!Password234",
        "email" : "changed@test.com",
        "role" : "user"
    }
    res = requests.put(f"{VYKOP_URL}/users/{user_id}",json=data, headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/users/{user_id}", headers={"Authorization": authorization})
    with open(f"expected_values/users/user_{user_id}.json") as f:
        expected = json.load(f)
    assert res.ok and res.json() == expected


def test_users_delete_user(authorization):
    user_id = 3
    res = requests.delete(f"{VYKOP_URL}/users/{user_id}", headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/users", headers={"Authorization": authorization})

    assert res.ok and len(res.json()) == 4


