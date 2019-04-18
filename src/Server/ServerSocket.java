package Server;

import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws")
public class ServerSocket {
	
	private static Vector<Session> sessionVector = new Vector<Session>();
	private static Vector<PlayerThread> players = new Vector<PlayerThread>();
	private static Vector<TableThread> tables = new Vector<TableThread>();
	private static Scanner sc;
	
	@OnOpen
	public void open(Session session) {
		System.out.println("Connection Made!");
		sessionVector.add(session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println(message);
		
		sc = new Scanner(message);
		sc.useDelimiter("\\|");
		String username = sc.next();
		System.out.println("Username:" + username);
		String typeTag = sc.next();
		System.out.println("Type:" + typeTag);
		
		if (typeTag.equals("CMD")) {
			
			String command = sc.next();
			System.out.println("Command:" + command);
			
			if (command.equals("JOIN")) {
				
				int tableNum = Integer.parseInt(sc.next());
				TableThread t;
				
				try {
					t = tables.get(tableNum);
				} catch (Exception e) {
					System.out.println("Table could not be found.");
					return;
				}
				
				
				if (t.GetOpenSpots() > 0) {
					System.out.println("Adding player " + username + " to table " + tableNum);
					PlayerThread pt = new PlayerThread(players.size(), username);
					joinTable(pt, tableNum);
					System.out.println("Table joined!");
				}
				
				else {
					
					System.out.println("Table is full.");
					sendMessage(session, "The table you chose is full.");
					return;
					
				}
				
				
			}
			
			else if (command.equals("NEW")) {
				int maxNum = Integer.parseInt(sc.next());
				PlayerThread pt = new PlayerThread(players.size(), username);
				System.out.println("Table created!");
				createTable(pt, maxNum);
				System.out.println("There are now " + tables.size() + " tables.");
			}	
			
		}
		
		try {
			for (Session s : sessionVector) {
				s.getBasicRemote().sendText(message);
			}
		} catch (IOException ioe) {
			System.out.println("ioe");
			close(session);
		}
	}
	
	private void sendMessage(Session s, String message) {
		try {
			s.getBasicRemote().sendText(message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	private void createTable(PlayerThread pt, int max) {
		tables.add(new TableThread(pt, max));
	}
	
	private void joinTable(PlayerThread pt, int tableNum) {
		tables.get(tableNum).AddPlayer(pt);
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnecting");
		sessionVector.remove(session);
	}
	
	@OnError
	public void error(Throwable error) {
		System.out.println("Error!");
	}
	
}
