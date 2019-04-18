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
	
	public void createTable(PlayerThread pt, int max) {
		TableThread tbt = new TableThread(pt, this, max);
		tables.add(tbt);
	}
	
	public TableThread getTable(int index) {
		return tables.get(index);
	}
	
	public ArrayList<TableThread> allTables() {
		return tables;
	}
	
	public void joinTable(PlayerThread pt, int index) {
		System.out.println("Player joining table " + index);
		tables.get(index).AddPlayer(pt);
	}
	
	public void PrintMessage(String message) {
		System.out.println("Table: " + message);
	}
	
}
