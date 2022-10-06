import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class SimpleSocketClient
{
    public static void main( String[] args ) throws UnknownHostException
    {

        String server = InetAddress.getLocalHost().getHostName();
        int port = 8080;

        System.out.println( "Loading contents of URL: " + server );

        try {

            // Connect to the server
            Socket socket = new Socket( server, port );

            // Writer to send message to server
            Writer out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(streamReader);
            
           while(true) {
        	   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	   	       String dataStr;
	   	       System.out.println("Hey there: ");
	   	       dataStr = br.readLine();
	   	       
	   	       if (dataStr == "Bye"){
	  	         break;
	   	       }
        	        	
	   	       out.append(dataStr).append("\n");
	   	       out.flush();
	   	       
            boolean acknowledged = false;

			while (!acknowledged) {
	            // StreamReader to read the response from the server

	            
//				String responseMsg;
				StringBuilder sb = new StringBuilder();
				
				String responseMessage = reader.readLine();
				System.out.println(responseMessage);

//				for (int ch; (ch = streamReader.read()) != -1; ) { // this gets stuck
//				    sb.append((char) ch);
//				}
//				System.out.println(sb.toString());
	        	acknowledged = true;

    	    }
           }
            socket.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}