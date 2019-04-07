package blackjack;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CheckLogin() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		boolean valid = true;
		String errorMessage = "";
		HttpSession session = request.getSession(true);
		// check username not empty
		username = username.trim();
		if(username == null || username.length() == 0) {
			valid = false;
			errorMessage = "username can't be empty. ";
		}
		// check passwords not empty
		else if(password == null || password.length() == 0) {
			valid = false;
			errorMessage = "password can't be empty. ";
		}
		System.out.println(username+" "+password);
		if(valid) {
			// **************************************************************************************
			// TODO: Check if valid username and password
			//		 if valid, store information into session
			// 		 if not, update errorMessage and set valid = false;
			
			
			
		}
		
		if(!valid) {
			session.setAttribute("loginError", errorMessage);
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/Login.jsp");
			dispatch.forward(request, response);
		}
		else {
			session.setAttribute("loginError", null);
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/HomePage.jsp");
			dispatch.forward(request, response);
		}
	}

}
