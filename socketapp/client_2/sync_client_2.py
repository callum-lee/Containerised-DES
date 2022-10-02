import socket

def client_program(message):
    # get the hostname
    host = socket.gethostname()
    port = 8080  # initiate port no above 1024

    ClientMultiSocket = socket.socket()
    ClientMultiSocket.settimeout(100)

    print('Waiting for connection response')
    try:
        ClientMultiSocket.connect((host, port))
    except socket.error as e:
        print(str(e))
    res = ClientMultiSocket.recv(1024) 
    
    for i in range(len(message)):
        Input = message[i] # Set an arbitrary input
        if Input == "Bye":
          break
        ClientMultiSocket.send(str.encode(Input))
        acknowledged = False
        while not acknowledged:
            try:
                res = ClientMultiSocket.recv(1024)
                print('From ' + str((host, port)) + ": " + res.decode('utf-8'))
                acknowledged = True
            except socket.timeout:
                ClientMultiSocket.send(str.encode(Input))
    ClientMultiSocket.close()
  
if __name__ == '__main__':
    message = ["10","11"]
    client_program(message)