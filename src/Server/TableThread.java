package Server;
import java.util.ArrayList;

public class TableThread extends Thread{
	private PlayerThread owner = null;
	private ArrayList<PlayerThread> tables = new ArrayList<PlayerThread>();
	
	public TableThread(PlayerThread opt) {
		this.owner = opt;
		tables.add(opt);
	}
	
	public void run() {
		//execute game logic
	}
}
