<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*"%>
<!DOCTYPE html>

<%
	boolean login = (boolean)session.getAttribute("login");
	String username;
	int balance=-1;
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
				balance = rs.getInt("balance");
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: "+cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.print("sqle: "+sqle.getMessage());
		}
	}
	else{
		System.out.println("not logged in");
		username = "Guest Player";
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
		<h1>Tables</h1>
		<div id="background"></div>
		<div id="tables">
		</div>
		
		<div id="userInfo">
		<div id="blockImage"></div>
			<%
				if(login){
			%>
				<button id="newTable" onclick="window.location='CreateTable.jsp'">New Table</button><br />
				<button id="moreInfo" onclick="window.location='AccountInfo.jsp'">More Info...</button>
				<span id="balance">$<%= balance %></span>
			<%
				}
			%>
			<div id="avatarBlock"><img src="Assets/avatar.jpg" id="avatar"></div>
			<span id="username"><%= username %></span><br />
		</div>
	</body>
</html>