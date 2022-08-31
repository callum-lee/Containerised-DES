# syntax=docker/dockerfile:1

# Define the base image
# FROM nginx:1.10.1
FROM python:3.9.2

# Copy the source files to a destination
# COPY src/html /Users/callum/GitHub/Containerised-DES
RUN python3 -m venv /opt/venv

COPY requirements.txt requirements.txt 


RUN . /opt/venv/bin/activate && pip3 install -r requirements.txt

COPY . .

COPY app.py . 

CMD . /opt/venv/bin/activate && exec python app.py 




# CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0", "--port=8000"]

# Expose the host ports
# EXPOSE 80

# Define entry point
# CMD ["nginx", "-g" ,"daemon off;"]

