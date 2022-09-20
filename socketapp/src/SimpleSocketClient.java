import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
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
        int port = 8080;

        System.out.println( "Loading contents of URL: " + server );

        try {
//    	    ClientMultiSocket = Socket.socket();
//    	    ClientMultiSocket.settimeout(100)
            // Connect to the server
            Socket socket = new Socket( server, port );


            System.out.println("Waiting for connection response");
//    	    try:
//    	        ClientMultiSocket.connect((host, port))
//    	    except socket.error as e:
//    	        print(str(e))
//    	    res = ClientMultiSocket.recv(1024)
//
    	    while (true) {
    	    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	    	String dataStr;
    	    	System.out.println("Hey there: ");
    	        dataStr = br.readLine();

    	        if (dataStr == "Bye"){
    	          break;
    	        }
    	        int dataInt = Integer.parseInt(dataStr);
    	        
    	        OutputStream output = socket.getOutputStream(); // output to the server
    	        output.write(dataInt);
    	        PrintWriter writer = new PrintWriter(output, true);
    	        
//    	        ClientMultiSocket.send(str.encode(Input))
//    	        acknowledged = False
//    	        while not acknowledged:
//    	            try {
                ObjectInputStream res = new ObjectInputStream(socket.getInputStream());
                String message = (String) res.readObject();
                System.out.println("From (" + (String)(server) + "," + port + "): " + res);
//    	                acknowledged = True
//    	            }
//    	            except socket.timeout{
//    	    }
    	    }
//    	    ClientMultiSocket.close()
            socket.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}