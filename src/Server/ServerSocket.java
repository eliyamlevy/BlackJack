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
		//Inside game logic
		if (typeTag.equals("ACT")) {
			String command = sc.next();
			System.out.println("Command:" + command);
			
			/*
			 * Basic format for player action
			 * Receive action from client (message from session)
			 * find corresponding PlayerThread pt
			 * call pt.setAction(message);
			 * call String result = pt.getResult();
			 */
			
			if (command.equals("READY")) {
				//starts hand on table when owner ready
				players.get(findPlayer(session)).SetReady(true);
			}
			
			else if (command.equals("START")) {
				//starts hand on table when owner ready
				players.get(findPlayer(session)).SetStart(true);
			}
			
			else if(command.equals("HIT")) {
				players.get(findPlayer(session)).setAction(message);
			}
			
			else if(command.equals("BET")) {
				players.get(findPlayer(session)).setAction(message);
			}
			
			else if(command.equals("STAY")) {
				players.get(findPlayer(session)).setAction(message);
			}
			else if(command.equals("LEAVE")) {
				players.get(findPlayer(session)).setAction(message);
				
			}
		}
		//Outside of game logic
		else if (typeTag.equals("CMD")) {
			String command = sc.next();
			System.out.println("Command:" + command);
			if (command.equals("JOINTABLE")) {
				int tableNum = Integer.parseInt(sc.next());
				TableThread t;
				//COME BACK
				try {
					t = tables.get(tableNum);
				} catch (Exception e) {
					System.out.println("Table could not be found.");
					sendMessage(session, "Table could not be found.");
					return;
				}
				
				if (t.GetOpenSpots() > 0) {
					PlayerThread pt = new PlayerThread(sessionVector.indexOf(session), username);
					players.add(pt);
					joinTable(pt, tableNum);
					System.out.println("Adding player " + username + " to table " + tableNum);
					broadcastToOthersAtTable("Player " + username + " has joined your table!", session);
					String forAll = "UPD|INTABLE|" + t.getPlayers().size();
					String forClient = "UPD|INTABLE|" + + t.getPlayers().size();
					
					for (int i = 0; i<t.getPlayers().size(); i++) {
						forAll = forAll + "|" + t.getPlayers().get(i).username;
						forClient = forClient + "|" + t.getPlayers().get(i).username;
					}
					
					System.out.println(	forClient);
					
					broadcastToOthersAtTable(forAll, session);
					sendMessage(session, forClient);
					System.out.println("Table joined!");
					//update everyone else who is in that table, that someone else has joined
				}
				else {
					System.out.println("Table is full.");
					sendMessage(session, "The table you chose is full.");
					return;
				}
			}
			else if (command.equals("NEWTABLE")) {
				int maxNum = Integer.parseInt(sc.next());
				PlayerThread pt = new PlayerThread(sessionVector.indexOf(session), username);
				players.add(pt);
				System.out.println("Table created! for " + findPlayer(session));
				createTable(pt, maxNum);
				//sendMessage to the owner, saying send ready or something if you're done
				System.out.println("There are now " + tables.size() + " tables.");
				sendMessage(session, "UPD|INTABLE|1|" + username);
			}	
			else if (command.equals("LIST")) {
				//sendMessage to the owner, saying send ready or something if you're done
				System.out.println("Sending table list");
				for (int i = 0; i < tables.size(); i++) {
					String tList = "Table " + i + " has " + tables.get(i).GetOpenSpots() + "\n\t Owned by " + tables.get(i).owner.username;
					sendMessage(session, tList);
				}
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
	
	//get the table the current session is at
	private TableThread getTable(Session s) {
		
		int playerIndex = findPlayer(s);
		PlayerThread player = players.elementAt(playerIndex);
		
		for (TableThread t : tables) {
			if (t.hasPlayer(player)) return t;
		}

		return null;
		
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
	
	private int findPlayer(Session s) {
		int index = sessionVector.indexOf(s);
		int playerIndex = -1;
		
		for (int i = 0; i<players.size(); i++) {
			if (players.get(i).sessionIndex == index) playerIndex = i;
		}
		
		System.out.println("Player found at: " + playerIndex);
		
		return playerIndex;
	}
	
	public void broadcastToOthersAtTable(String message, Session current) {
		System.out.println("ServerSocket: Broadcasting message: " + message);
		TableThread t = getTable(current);
		Vector<PlayerThread> players = t.getPlayers();
		PlayerThread currentPlayer = players.get(findPlayer(current));
		
		for (PlayerThread pt : players) {
			if(!pt.equals(currentPlayer)) {
				int sessionIndex = pt.sessionIndex;
				sendMessage(sessionVector.get(sessionIndex), message);
			}
		}
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnecting");
		
		System.out.println("Before closing: ");
		
		for (int i = 0; i<sessionVector.size(); i++) {
			System.out.println("Session " + i + " is linked to player " + findPlayer(sessionVector.get(i)));
		}
		
		//if this is the last open session, just reset everything so we don't have random tables left open
		if (sessionVector.size() == 1) {
			tables.clear();
			players.clear();
			sessionVector.clear();
			System.out.println("Socket: Everything reset to 0.");
			return;
		}
		
		//Updates all the player session IDs
		for (int i = sessionVector.indexOf(session)+1; i<sessionVector.size(); i++) {
			int playerIndex = findPlayer(sessionVector.get(i));
			if (playerIndex == -1) continue;
			players.get(playerIndex).sessionIndex--;
			System.out.println("Socket: player index" + playerIndex + " decremented to " + players.get(playerIndex).sessionIndex);
		}
		
		int removePlayer = findPlayer(session);
		
		if (removePlayer != -1) players.remove(removePlayer);
		
		sessionVector.remove(session);
		
		System.out.println("After closing: ");
		
		for (int i = 0; i<sessionVector.size(); i++) {
			System.out.println("Session " + i + " is linked to player " + findPlayer(sessionVector.get(i)));
		}
		
	}
	
	@OnError
	public void error(Throwable error) {
		System.out.println("Error!");
	}
	
}
