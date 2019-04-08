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

			HttpSession session = request.getSession();
			Boolean success = true;
			String fullName = request.getParameter("fullname");
			String email = request.getParameter("email");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String cpassword = request.getParameter("cpassword");

			PrintWriter writer = response.getWriter();

			if (username == null || username.trim().length() == 0) {
				writer.println("Username needs a value");
				success = false;
			}

			if (password == null || password.trim().length() == 0) {
				writer.println("Password needs a value");
				success = false;
			}

			if (cpassword == null || cpassword.trim().length() == 0) {
				writer.println("Confirm Password a value");
				success = false;
			}

			if(!cpassword.equals(password)) {
				writer.println("Passwords do not match.");
				success = false;
			}
		
			if(success) {
				
				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;

				try {
					
					conn = DriverManager.getConnection("jdbc:mysql://localhost/BlackJackDB?user=INSERTUSERNAME&password=INSERTPASSWORD");

					ps = conn.prepareStatement("SELECT username from Users WHERE username=?");
					ps.setString(1, username);

					rs = ps.executeQuery();
				
					if (rs.next()) {
						writer.println("Username already exists");
					}

					else {
						ps = conn.prepareStatement("INSERT INTO Users (fullName, email, username, password, balance, bailoutTokens, score)" + "VALUES (?,?,?,?,500,0,500)");
						ps.setString(1, fullName);
						ps.setString(2, email);
						ps.setString(3, username);
						ps.setString(4, password);
						ps.executeUpdate();
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
