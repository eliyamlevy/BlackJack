<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
	boolean login = (boolean)session.getAttribute("login");
	String username;
	if(login){
		username = (String)session.getAttribute("user");
	}
	else{
		username = "Guest Player";
	}
%>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Home Page</title>
		<link rel="stylesheet" type="text/css" href="Assets/homepage.css" />
	</head>
	<body>
		<h1>Tables</h1>
		<div id="background"></div>
		<div id="tables">
		</div>
		
		<div id="userInfo">
			<%
				if(login){
			%>
				<button id="newTable" onclick="window.location='CreateTable.jsp'">New Table</button>
				<button id="moreInfo" onclick="window.location='AccountInfo.jsp'">More Info...</button>
			<%
				}
			%>
			<div id="avatar"><img src="Assets/jeffrey_miller.jpg"></div>
			<span id="username"><%=username %></span>
		</div>
	</body>
</html>