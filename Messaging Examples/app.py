from doctest import Example
import os
from platform import python_version
import re
from xmlrpc.client import boolean 
from flask import Flask, Response, request
import requests
from pip import main 

app = Flask(__name__)


# @app.route("/")
# def hello():
#     return "Hello World!"

# data = '494949499494'
# r = requests.post("http://localhost:5000", data= data)

# request data format 1: url query parameters
@app.route('/query-example')
def query_example():
    language = request.args.get('language')
    framework = request.args.get('framework') 
    website = request.args.get('website')
    return '''<h1>The language is : {}</h1>
               <h1> The framework is : {}</h1>
                <h1> The website is : {}</h1>'''.format(language, framework, website)

# request data fromat 2: forms

# request data format 3: json data
@app.route('/json-example', methods = ['GET', 'POST'] )
def json_example(): # usually sent through some auto processes
    req_data = request.get_json()
    
    language = req_data["language"]
    framework = req_data["framework"]
    python_version = req_data["version_info"]["python"]
    examples = req_data["examples"][0]
    boolean_test = req_data["boolean_test"]

    return '''<h1>
    The language value is {}.
    The framework value is {}
    The Python version is {}
    The example at 0 index is {}
    The boolean value is {}
    </h1>'''.format(language, framework, python_version, examples, boolean_test)

# return which script sent a larger number
@app.route('/compare-example', methods = ['GET', 'POST'] )
def compare_example(): # usually sent through some auto processes
    req_data = request.get_json()
    
    A_val = req_data["A"]
    B_val = req_data["B"]
    
    if A_val > B_val:
        larger_key = "A"
    else:
        larger_key = "B"

    return '''<h1>
    Value of post A is {}.
    Value of post B is {}.
    Post {} sent a larger number.
    </h1>'''.format(A_val, B_val, larger_key)

if __name__ == "__main__":
    app.run(debug = True, port = 5000)