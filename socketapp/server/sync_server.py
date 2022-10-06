import socket
import os
from _thread import *
import sys

class SyncServer:
  
    def __init__(self):
      self.socket = None
      self.ThreadCount = None
      self.ConnectionDict = None
      
    def multi_threaded_client(self, connection):
      connection.send(str.encode('Server is working:'))
      while True:
        data = connection.recv(2048)
        prev = next(iter(self.ConnectionDict))
        if not data:
          del self.ConnectionDict[connection]
        else:
          message = data.decode('utf-8')
          tick = int(message)
          self.ConnectionDict[connection] = tick
          print(str(connection.getpeername()) + ": " + str(message))

        if len(self.ConnectionDict.keys()) > 0:
          self.ConnectionDict = {k: v for k, v in sorted(self.ConnectionDict.items(), key=lambda item: item[1])}
          print("Connections = " + ', '.join([str(conn.getpeername()) for conn in self.ConnectionDict.keys()]))
  
          curr = next(iter(self.ConnectionDict))
          
          if (connection == curr) or (prev != curr):
            # Only send a message if the current executing connection has changed
            response = 'Server message: Connection = ' + str(curr.getpeername()) + ' at ' + str(self.ConnectionDict[curr]) + os.linesep
            print(response)
            curr.sendall(str.encode(response))          
          
        if not data: break
       
      connection.close()
      if len(self.ConnectionDict.keys()) == 0: self.socket.close()
      
    def run(self, host=None, port=None):
      if host is None:
        # get the hostname
        host = socket.gethostname()
      if port is None:
        port = 8080  # initiate port no above 1024

      self.socket = socket.socket()
      self.ThreadCount = 0
      self.ConnectionDict = {}
      try:
        self.socket.bind((host, port))
      except socket.error as e:
        print(str(e))

      print('Socket is listening..')
      self.socket.listen(5)

      while True:
        try:
          Client, address = self.socket.accept()
          print('Connected to: ' + address[0] + ':' + str(address[1]))
          self.ConnectionDict[Client] = 2 * sys.maxsize + 1
          self.ThreadCount += 1
          assert(len(self.ConnectionDict.keys()) == self.ThreadCount)
          print('Thread Number: ' + str(self.ThreadCount))
          start_new_thread(self.multi_threaded_client, (Client, ))
        except OSError:
          break
          
if __name__ == '__main__':
  server = SyncServer()
  server.run()