package blackjack;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Boolean valid = true;
		
		PrintWriter writer = response.getWriter();
		
		if (username == null || username.trim().length() == 0) {
			writer.println("Username needs a value");
			valid = false;
		}
		
		if (password == null || password.trim().length() == 0) {
			writer.println("Password needs a value");
			valid = false;
		}
		
		
		if (valid) {
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/BlackJackDB?user=INSERTUSERNAME&password=INSERTPASSWORD");
				ps = conn.prepareStatement("SELECT username, password from Users WHERE username=?");
				ps.setString(1, username);
				rs = ps.executeQuery();
				
				if (!rs.next()) {
					writer.println("Username does not exist.");
				}
				
				else if (!rs.getString("password").equals(password)) {
					writer.println("Incorrect password.");
				}

				else {
					session.setAttribute("user", username);
				}
				
			} catch (ClassNotFoundException nfe) {
				System.out.println(nfe.getMessage());
			} catch (SQLException sqle) {
				System.out.print(sqle.getMessage());
			}	
			
		}
		
		writer.flush();
		writer.close();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);	
	}

}
