package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientRunner extends Thread {
	
	private BufferedReader br;
	private PrintWriter pw;
	
	public ClientRunner(String hostname, int port) {
		try {
			System.out.println("Trying to connect to " + hostname + ":" + port);
			Socket s = new Socket(hostname, port);
			System.out.println("Connected to " + hostname + ":" + port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
			this.start();
			Scanner scan = new Scanner(System.in);
			while (true) {
				String line = scan.nextLine();
				pw.println(line);
				pw.flush();
			}

		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient constructor: " + ioe.getMessage());
		}
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				String line = br.readLine();
				System.out.println(line);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient.run(): " + ioe.getMessage());
		}
	}

	
	public static void main(String [] args) {
		ClientRunner cc = new ClientRunner("localhost", 6969);
	}

}
