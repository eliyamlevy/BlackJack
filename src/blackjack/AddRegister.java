package blackjack;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AddRegister")
public class AddRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public AddRegister() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		boolean success = true;
		String errorMessage = "";
		String fullName = request.getParameter("fullname");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// cpassord: confirm password, should be equal to password
		String cpassword = request.getParameter("cpassword");
		
		// check username not empty
		username = username.trim();
		if(username == null || username.length() == 0) {
			success = false;
			errorMessage = "username can't be empty. ";
		}
		// check passwords not empty
		else if(password == null || password.length() == 0) {
			success = false;
			errorMessage = "password can't be empty. ";
		}
		else if(cpassword == null || cpassword.length() == 0) {
			success = false;
			errorMessage = "confirm password can't be empty. ";
		}
		
		System.out.println(username+" "+password+" "+cpassword);
		
		// make sure username and passwords are not empty before checking with database
		if(success) {
			// **************************************************************************************
			// TODO: check 1 - username is unique
			//			   2 - passwords match
			// 		 if not, write specific error message in errorMessage and set success to false;
			
			
			
			
			
			
		}
		
		// invalid register information, back to register page
		if(!success) {
			session.setAttribute("registerError", errorMessage);
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/Register.jsp");
			dispatch.forward(request, response);
		}
		// valid register information, write into database
		else {
			session.setAttribute("registerError", null);
			// **************************************************************************************
			// TODO: write register info into database;
			
			
			
			
			
			
			
		}
	}
}
