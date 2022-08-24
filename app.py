import os 
from flask import Flask, Response
import requests
from pip import main 

app = Flask(__name__)

data = '494949499494'
r = requests.post("http://localhost:5000", data= data)

@app.route('/', methods=['POST'])
def get_data():
    print('Recieved.format'.format(requests.data))
    return Response('we recieved')


if __name__ == "__main__":
    app.run(debug = True)