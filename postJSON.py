import requests
import json

data = {
    "language": "Python",
    "framework": "Flask",
    "website": "scotch",
    "version_info": {
        "python": 3.7,
        "flask": 1.0
    },
    "examples": ["query", "form", "json"],
    "boolean_test": True
}

jsonData = json.dumps(data)
requests.post('http://127.0.0.1:5000/json-example', json = data)
pass