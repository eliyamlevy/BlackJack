package Server;

import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/bjs")
public class ServerSocket {
	private static Vector<Session> sessionVector = new Vector<Session>();
	private static Vector<TableThread> tables = new Vector<TableThread>();
	private static Vector<PlayerThread> players = new Vector<PlayerThread>();
	
	private static Scanner sc;
	
	
	@OnOpen
	public void open(Session session) {
		System.out.println("Connection Made!");
		sessionVector.add(session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		sc = new Scanner(message);
		sc.useDelimiter("|");
		String username = sc.next();
		String typeTag = sc.next();
		if(typeTag == "CMD") {
			String command = sc.next();
			//Join table logic
			if(command == "NEWTABLE") {
				String numPlayers = sc.next();
				int maxNum = Integer.parseInt(numPlayers);
				System.out.println("Table created!");
				PlayerThread pt = new PlayerThread(username, session);
				createTable(pt, maxNum);
//				this.sendMessage("Start Hand?");
//				line = br.readLine();
//				while (!line.contains("YES")) {}
				pt.ready = true;
			}
			else if (command == "JOINTABLE") {
//				this.sendMessage("Available Tables:");
				int tableNum = Integer.parseInt(sc.next());
				TableThread t = tables.get(tableNum);
				if (t.GetOpenSpots() > 0) {
					System.out.println("Adding player " + username + " to table " + tableNum);
					PlayerThread pt = new PlayerThread(username, session);
					joinTable(pt, tableNum);
					try {
						session.getBasicRemote().sendText("Joined table: " + tableNum);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					t.broadcast("Player has joined this table.", this);
				}
				else {
					try {
						session.getBasicRemote().sendText("TABLEFULL");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
		else if(typeTag == "UPD") {
			
		}
		else if(typeTag == "ACT") {
			
		}
		for (Session s : sessionVector) {
			try {
				s.getBasicRemote().sendText(message);
			} catch (IOException e) {
				System.out.println("IOE: " + e.getMessage());
			}
		}	
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnected!");
		sessionVector.remove(session);
	}
	
	@OnError
	public void error(Throwable error) {
		System.out.println("Error!");
	}
	
	private void joinTable(PlayerThread pt, int tableNum) {
		tables.get(tableNum).AddPlayer(pt);
	}
	
	private void createTable(PlayerThread pt, int numPlayers) {
		tables.add(new TableThread(pt, numPlayers));
	}
}
