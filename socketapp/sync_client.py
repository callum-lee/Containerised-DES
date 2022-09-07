import socket

def client_program():
    # get the hostname
    host = socket.gethostname()
    port = 5000  # initiate port no above 1024

    ClientMultiSocket = socket.socket()
    ClientMultiSocket.settimeout(100)

    print('Waiting for connection response')
    try:
        ClientMultiSocket.connect((host, port))
    except socket.error as e:
        print(str(e))
    res = ClientMultiSocket.recv(1024)

    while True:
        Input = input('Hey there: ')
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
    client_program()