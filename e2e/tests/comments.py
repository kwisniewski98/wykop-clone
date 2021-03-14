import requests
import json
from util import authorization

VYKOP_URL = "http://localhost:8080"


def test_comment_upvote(authorization):
    res = requests.post(f"{VYKOP_URL}/comments/upvote/1", headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/post?id=1", headers={"Authorization": authorization})
    assert res.ok and res.json()["comments"][0]["upvoted"]

def test_comment_delete(authorization):
    res = requests.delete(f"{VYKOP_URL}/comments/1", headers={"Authorization": authorization})
    assert res.ok
    res = requests.get(f"{VYKOP_URL}/post?id=1", headers={"Authorization": authorization})
    assert res.ok and len(res.json()["comments"]) == 0