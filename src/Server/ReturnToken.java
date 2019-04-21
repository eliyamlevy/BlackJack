package blackjack;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ReturnToken")
public class ReturnToken extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReturnToken() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("user");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BlackJackDB?user=root&password=0330");

			ps = conn.prepareStatement("UPDATE Users SET bailoutTokens=bailoutTokens-1, balance=balance-500, score=score+200 WHERE username=?");
			ps.setString(1, username);
			ps.executeUpdate();
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: "+cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.print("sqle: "+sqle.getMessage());
		} finally {
			try {
				if(rs != null) {	rs.close(); }
				if(ps != null) {	ps.close(); }
				if(conn != null) {	conn.close(); }
			} catch (SQLException sqle) {
				System.out.println("sqle closing stuff: " + sqle.getMessage());
			}
		}
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/AccountInfo.jsp");
		dispatch.forward(request, response);
	}

}
