import requests
import pytest

VYKOP_URL = "http://localhost:8080"


@pytest.fixture()
def authorization():
    """Authorizes user to endpoint and returns JWT token"""
    body = {
        "username": "admin",
        "password": "password"
        }
    resp = requests.post(f"{VYKOP_URL}/users/login", json=body)
    return resp.headers.get("Authorization")
