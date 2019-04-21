<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*"%>
<!DOCTYPE html>

<%
	boolean login = (boolean)session.getAttribute("login");
	String username;
	int balance=-1;
	boolean canGetToken = false;
	if(login){
		username = (String)session.getAttribute("user");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BlackJackDB?user=root&password=0330");

			ps = conn.prepareStatement("SELECT balance from Users WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("Getting Balance...");
				balance = rs.getInt("balance");
				System.out.println("Got user balance, amount: "+ balance);
				if(balance<50){
					canGetToken = true;
				}
			}
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
	}
	else{
		username = "";
	}
%>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Home Page</title>
		<link rel="stylesheet" type="text/css" href="Assets/homepage.css" />
		<link rel="shortcut icon" href="Assets/favicon.ico" type="image/x-icon">
	</head>
	<body>
		<div id="navigator">
			<%
				if(login){
			%>
					<a id="blackjack" href="${pageContext.request.contextPath}/HomePage.jsp">B L A C K J A C K</a>
					<a id="back" href="${pageContext.request.contextPath}/WelcomePage.jsp">SIGN OUT</a>
			<%
				}
				else {
			%>
					<a id="blackjack" href="${pageContext.request.contextPath}/WelcomePage.jsp">B L A C K J A C K</a>
			<%
				}
			%>
		</div>
		<h1>Tables</h1>
		<div id="background"></div>
		<div id="tables">
		</div>
		
		<div id="userInfo">
		<div id="blockImage"></div>
			<div id="avatarBlock"><img src="Assets/avatar.jpg" id="avatar"></div>
			<%
				if(login){
			%>
				<button id="newTable" onclick="window.location='CreateTable.jsp'">New Table</button><br />
				<button id="moreInfo" onclick="window.location='AccountInfo.jsp'">More Info...</button>
				<span id="username"><%= username %></span><br />
				<span id="balance">$<%= balance %></span>
			<%
					if(canGetToken){
			%>
						<form id="tokenForm" method="POST" action="GetToken">
							<button id="getToken" onclick="submit">Add balance</button>
						</form>
			<%
					}
				}
				else {
			%>
					<span id="guest">Guest Player</span>
			<%
				}
			%>
		</div>
	</body>
</html>