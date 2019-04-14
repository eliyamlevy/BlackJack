package Server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BJServer {
	
	private ArrayList<TableThread> tables = new ArrayList<TableThread>();
	private ArrayList<PlayerThread> playersOnline = new ArrayList<PlayerThread>();
	
	public BJServer(int port) {
		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Listening on port: " + port);
			//Add the two default tables
			while(true) {
				Socket s = ss.accept(); // blocking
				System.out.println("Connection from: " + s.getInetAddress()); 
				PlayerThread pt = new PlayerThread(this, s);
				playersOnline.add(pt);
				System.out.println("Amount of people online: " + playersOnline.size());
			}
		} catch (IOException ioe) {
			System.out.println("ioe in Server constructor: " + ioe.getMessage());
		}
	}
	
	public static void main(String[] args) {
		BJServer bjServer = new BJServer(6969);
	}
	
	public void createTable(PlayerThread pt) {
		TableThread tbt = new TableThread(pt);
		tables.add(tbt);
	}
}
