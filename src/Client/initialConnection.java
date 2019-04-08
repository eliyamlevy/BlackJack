package Client;

import java.io.IOException;
import java.net.Socket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class initialConnection
 */
@WebServlet("/initialConnection")
public class initialConnection extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public initialConnection() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String hostname = "localhost";
		int port = 6969;
		HttpSession session = request.getSession(false);
		if(session.getAttribute("socket") == null) {
			try {
				System.out.println("Trying to connect to " + hostname + ":" + port);
				Socket s = new Socket(hostname, port);
				System.out.println("Connected to " + hostname + ":" + port);
				session.setAttribute("socket", s);
			} catch (IOException ioe) {
				System.out.println("ioe in ChatClient constructor: " + ioe.getMessage());
			}
		}
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
		//Actually do the redirect
		dispatcher.forward(request, response);
	}


}
