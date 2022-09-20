import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class sample{
    public static void main( String[] args ) throws UnknownHostException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Hey there: ");
		String dataStr;
		try {
			dataStr = br.readLine();
			System.out.println(dataStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
