package Server;

import java.io.IOException;
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
	
	
	@OnOpen
	public void open(Session session) {
		System.out.println("Connection Made!");
		sessionVector.add(session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Message Recieved:" + message);
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
}
