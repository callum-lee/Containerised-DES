import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class SimpleSocketClient
{
    public static void main( String[] args ) throws UnknownHostException
    {
//    	    # get the hostname
//    	    host = socket.gethostname()
//    	    port = 5000  # initiate port no above 1024
        String server = InetAddress.getLocalHost().getHostName();
        int port = 5000;

        System.out.println( "Loading contents of URL: " + server );

        try {
//    	    ClientMultiSocket = socket.socket()
//    	    ClientMultiSocket.settimeout(100)
            // Connect to the server
            Socket socket = new Socket( server, port );


//    	    print('Waiting for connection response')
//    	    try:
//    	        ClientMultiSocket.connect((host, port))
//    	    except socket.error as e:
//    	        print(str(e))
//    	    res = ClientMultiSocket.recv(1024)
//
//    	    while True:
//    	        Input = input('Hey there: ')
//    	        if Input == "Bye":
//    	          break
//    	        ClientMultiSocket.send(str.encode(Input))
//    	        acknowledged = False
//    	        while not acknowledged:
//    	            try:
//    	                res = ClientMultiSocket.recv(1024)
//    	                print('From ' + str((host, port)) + ": " + res.decode('utf-8'))
//    	                acknowledged = True
//    	            except socket.timeout:
//    	                ClientMultiSocket.send(str.encode(Input))

//    	    ClientMultiSocket.close()
            socket.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}