<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*"%>
<!DOCTYPE html>

<%
	boolean login = (boolean)session.getAttribute("login");
	String username = "";
	String fullName = "";
	String email = "";
	int balance = -1;
	int tokens = -1;
	boolean canReturnToken = false;
	if(login){
		username = (String)session.getAttribute("user");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BlackJackDB?user=root&password=0330");
	
			ps = conn.prepareStatement("SELECT fullName, email, balance, bailoutTokens from Users WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next()) {
				fullName = rs.getString("fullName");
				email = rs.getString("email");
				balance = rs.getInt("balance");
				tokens = rs.getInt("bailoutTokens");
				if(tokens>0){
					canReturnToken = true;
				}
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: "+cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.print("sqle: "+sqle.getMessage());
		}
	}
%>

<html>
	<head>
		<meta charset="UTF-8">
		<title>Account Information</title>
		<link rel="stylesheet" type="text/css" href="Assets/accountinfo.css" />
		<link rel="shortcut icon" href="Assets/favicon.ico" type="image/x-icon">
	</head>
	<body>
		<div id="navigator">
			<a id="blackjack" href="${pageContext.request.contextPath}/HomePage.jsp">B L A C K J A C K</a>
		</div>
		<div id="background">
		</div>
		<div id="container">
			<img id="avatar" src="Assets/avatar.jpg" alt="avatar">
			<span id="greeting">Hi, <%= username %>!</span><br />
			<table id="info">
				<tr>
					<td>Full Name</td>
					<td><%= fullName %></td>
				</tr>
				<tr>
					<td>Email</td>
					<td><%= email %></td>
				</tr>
				<tr>
					<td>Balance</td>
					<td>$<%= balance %></td>
				</tr>
				<tr>
					<td>Tokens <br />holding</td>
					<td><%= tokens %>
					<%
						if(canReturnToken){
					%>
						<form id="tokenForm" method="POST" action="ReturnToken">
							<button id="returnToken" onclick="submit">Return Token</button>
						</form>
					<%
						}
					%>
					</td>
				</tr>
				<tr>
					<td>Ranking</td>
					<td><button name="toleader" id="toleader" onclick="window.location='Leaderboard.jsp'">
						see leaderboard...</button></td>
				</tr>
			</table>
			</div>
	</body>
</html>