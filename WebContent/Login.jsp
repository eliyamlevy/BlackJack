<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href="Assets/login.css" />
	</head>
	<% 
		String lError = (String)session.getAttribute("loginError");
		if(lError == null){
			lError = "";
		}
		session.setAttribute("loginError", null);
	%>
	<body>
		<div id="background">
		</div>
		<div id="container">
			<span id="header">Login</span>
			<form class="form" name="loginForm" id="loginForm" method="POST" action="CheckLogin">
				Username <br /><input type="text" name="username" id="username"><br />
				Password <br /><input type="password" name="password" id="password"><br />
				<span id="errorMessage"><%= lError %></span><br />
				<button name="submit" id="submit" onclick="submit">Login</button>
			</form>
		</div>
	</body>
</html>