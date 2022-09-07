import requests
import json
from random import random

data = {
    "A": random()
}

# jsonData = json.dumps(data)
requests.post('http://127.0.0.1:5000/compare-example', json = data)
