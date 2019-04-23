<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	session.setAttribute("login", false);
	session.setAttribute("user", "GUEST");
%>
<html>
	<head>
		<meta charset="UTF-8">
		<title>BlackJack</title>
		<link rel="stylesheet" type="text/css" href="Assets/welcomepage.css" />
		<link rel="shortcut icon" href="Assets/favicon.ico" type="image/x-icon">
	</head>
	<body>
		<div id="navigator">
			<a id="blackjack" href="${pageContext.request.contextPath}/WelcomePage.jsp">B L A C K J A C K</a>
		</div>
		<div id="background"></div>
		<h3 id="title1">WELCOME TO</h3>
		<h1 id="title2">BLACKJACK!</h1>
		<div id="menu">
			<div class="menuEntry">
				<a href="${pageContext.request.contextPath}/Login.jsp">Login</a>
			</div>
			<div class="menuEntry">
				<a href="${pageContext.request.contextPath}/Register.jsp">Register</a>
			</div>
			<div class="menuEntry">
				<a href="${pageContext.request.contextPath}/Table.jsp">Continue as guest</a>
			</div>
		</div>
	</body>
</html>