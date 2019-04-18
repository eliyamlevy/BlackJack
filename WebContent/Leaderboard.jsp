<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="javax.servlet.*, java.sql.*, java.io.*, java.util.*"%>
<!DOCTYPE html>
<%
	
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Queue<String> users = new LinkedList<String>();
	Queue<Integer> scores = new LinkedList<Integer>();
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost/BlackJackDB?user=root&password=0330");
		ps = conn.prepareStatement("SELECT username, score FROM Users ORDER BY score DESC;");
		rs = ps.executeQuery();
		// store results into queues
		while (rs.next()) {
			users.add(rs.getString("username"));
			scores.add(rs.getInt("score"));
		}
	} catch (SQLException sqle) {
		System.out.println("sqle: " + sqle.getMessage());
	} catch (ClassNotFoundException cnfe) {
		System.out.println("cnfe: " + cnfe.getMessage());
	} finally {
		try {
			if(rs != null) {	rs.close(); }
			if(ps != null) {	ps.close(); }
			if(conn != null) {	conn.close(); }
		} catch (SQLException sqle) {
			System.out.println("sqle closing stuff: " + sqle.getMessage());
		}
	}
%>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Leaderboard</title>
		<link rel="stylesheet" type="text/css" href="Assets/leaderboard.css" />
	</head>
	<body>
		<div id="background"></div>
		<div id="container">
			<span id="title">Leaderboard</span>
			<table id="leaderboard">
				<tr>
					<th>#</th>
					<th>username</th>
					<th>score</th>
				</tr>
					<%
					int i=1;
					while(users.size()>0){
					%>
					<tr>
						<td><%= i%></td>
						<td><%= users.remove() %></td>
						<td><%= scores.remove() %></td>
					</tr>
					<%
						i++;
					}
					%>
			</table>
		</div>
	</body>
</html>