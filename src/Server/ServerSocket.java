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
				
				TableThread t = getTable(session);
				
				sendMessage(session, this.getInGameUpdate(t));	
				this.broadcastToOthersAtTable(this.getInGameUpdate(t), session);
				
			}
			
			else if (command.equals("START")) {
				//starts hand on table when owner ready
				TableThread t = getTable(session);
				
				if (!t.allReady()) {
					sendMessage(session, "ERR|WAITING|INVSTART");
					return;
				}
				
				
				players.get(findPlayer(session)).SetStart(true);
				

				
				sendMessage(session, this.getInGameUpdate(t));
				this.broadcastToOthersAtTable(this.getInGameUpdate(t), session);
				
			}
			
			else if(command.equals("HIT")) {
				players.get(findPlayer(session)).setAction(message);
				
				TableThread t = getTable(session);
								
				this.sendMessage(session, this.getInGameUpdate(t));
				this.broadcastToOthersAtTable(this.getInGameUpdate(t), session);
				
			}
			
			else if(command.equals("BET")) {
				players.get(findPlayer(session)).setAction(message);
				String result = players.get(findPlayer(session)).getResult();
				
				if (result == "SUCCESS") {
					TableThread t = this.getTable(session);
					this.sendMessage(session, this.getInGameUpdate(t));
					this.broadcastToOthersAtTable(this.getInGameUpdate(t), session);
				}
				
				else {
					this.sendMessage(session, result);
				}	
				
			}
			
			else if(command.equals("STAY")) {
				players.get(findPlayer(session)).setAction(message);
			}
			else if(command.equals("LEAVE")) {
				
				TableThread t = getTable(session);
				
				if (t.getRoundStatus()) {
					this.sendMessage(session, "ERR|PLAYING|INVLEAVE");
					return;
				}
				
				
				players.get(findPlayer(session)).setAction(message);	
			}
		}
		//Outside of game logic
		else if (typeTag.equals("CMD")) {
			String command = sc.next();
			System.out.println("Command:" + command);
			
			if (command.equals("LIST")) {
				this.sendTableList(session);
			}
			
			else if (command.equals("JOINTABLE")) {
				int tableNum = Integer.parseInt(sc.next());
				TableThread t;
				try {
					t = tables.get(tableNum);
				} catch (Exception e) {
					System.out.println("Table could not be found.");
					sendMessage(session, "ERR|OUTTABLE|TABDNE");
					return;
				}
				
				if (t.GetOpenSpots() > 0) {
					PlayerThread pt = new PlayerThread(sessionVector.indexOf(session), username, t);
					players.add(pt);
					joinTable(pt, tableNum);
					System.out.println("Adding player " + username + " to table " + tableNum);
					broadcastToOthersAtTable("Player " + username + " has joined your table!", session);
					
					String forClient = this.getInGameUpdate(t);
					
					broadcastToOthersAtTable(forClient, session);
					sendMessage(session, forClient);
					broadcastToOthersAtTable(forClient, session);
					sendTableListToAll();
					System.out.println("Table joined!");
					//update everyone else who is in that table, that someone else has joined
				}
				
				else {
					System.out.println("Table is full.");
					sendMessage(session, "ERR|OUTTABLE|TABFULL");
					return;
				}
				
			}
			
			else if (command.equals("NEWTABLE")) {
				int maxNum = Integer.parseInt(sc.next());
				
				if (maxNum < 1) {
					sendMessage(session, "ERR|OUTTABLE|SIZEINV");
					return;
				}
				
				if (username == "GUEST" ) {
					sendMessage(session, "ERR|OUTTABLE|GUEST");
					return;
				}
				
				
				PlayerThread pt = new PlayerThread(sessionVector.indexOf(session), username, null);
				players.add(pt);
				System.out.println("Table created! for " + findPlayer(session));
				TableThread t = createTable(pt, maxNum);
				pt.setTable(t);
				//sendMessage to the owner, saying send ready or something if you're done
				System.out.println("There are now " + tables.size() + " tables.");
				
				String forClient = this.getInGameUpdate(t);
				
				sendMessage(session, forClient);
				sendTableListToAll();
				
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
	
	private TableThread createTable(PlayerThread pt, int max) {
		TableThread t = new TableThread(pt, max, true);
		tables.add(t);
		return t;
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
	
	private void sendTableList(Session s) {
		String list = "LIST|";
		
		list += tables.size();
		
		for (int i = 0; i<tables.size(); i++) {
			list = list + "|" + i + "|" + tables.get(i).GetOpenSpots() + "|" + tables.get(i).owner.username;
		}
		
		sendMessage(s, list);
		
	}
	
	private void sendTableListToAll() {
		
		String list = "LIST|";
		//format: LIST|number of tables|table index|open spots|owner
		
		list += tables.size();
		
		for (int i = 0; i<tables.size(); i++) {
			list = list + "|" + i + "|" + tables.get(i).GetOpenSpots() + "|" + tables.get(i).owner.username;
		}
		
		for (Session s : sessionVector) {
			sendMessage(s, list);
		}
		
	}
	
	public void broadcastToOthersAtTable(String message, Session current) {
		TableThread t = getTable(current);
		Vector<PlayerThread> tablePlayers = t.getPlayers();
		PlayerThread currentPlayer = players.elementAt(findPlayer(current));
		for (PlayerThread pt : tablePlayers) {
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
	
	private String getInGameUpdate(TableThread t) {
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String tableStatus = null;
		
		if (t.getRoundStatus() == false) {
			tableStatus = "WAITING";
		}
		
		else tableStatus = "INROUND";
		
		String forClient = "UPD|INTABLE|" + tableStatus + "|" + t.getPlayers().size();
		
		if (tableStatus == "WAITING") {
			
			for (int i = 0; i<t.getPlayers().size(); i++) {
				
				forClient = forClient + "|" + t.getPlayers().get(i).username;
				
				if (t.getPlayers().get(i).isReady()) {
					forClient+="|READY";
				}
				
				else forClient+="|NOTREADY";
				
			}
			
		}
		
		else {
			
			for (int i = 0; i<t.getPlayers().size(); i++) {
				
				forClient = forClient + "|" + t.getPlayers().get(i).username;
				
				if (t.getPlayers().get(i).isTurn()) {
					forClient+="|TURN";
				}
				
				else forClient+="|NOTTURN";
				
				forClient+= "|" + t.getPlayers().get(i).getScore() + "|" + t.getPlayers().get(i).getHand().size();
				
				for (int j = 0; j<t.getPlayers().get(i).getHand().size(); j++) {
					forClient += "|" + t.getPlayers().get(i).getHand().get(j); //add if they bust/got blackjack
				}
					
			}	
			
		}
		
		System.out.println("Game Update String: " + forClient);
		return forClient;
		
		
	}
	
}
