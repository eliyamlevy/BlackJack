<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Register</title>
		<link rel="stylesheet" type="text/css" href="Assets/register.css" />
	</head>
	<% 
		String rError = (String)session.getAttribute("registerError");
		if(rError == null){
			rError = "";
		}
		session.setAttribute("registerError", null);
	%>
	<body>
		<div id="background">
		</div>
		<div id="container">
			<span id="header">Register</span>
			<form class="form" name="registerForm" id="registerForm" method="POST" action="AddRegister">
				Full Name <br /><input type="text" name="fullname" id="fullname"><br />
				Email <br /><input type="email" name="email" id="email"><br />
				Username <br /><input type="text" name="username" id="username"><br />
				Password <br /><input type="password" name="password" id="password"><br />
				Confirm Password <br /><input type="password" name="cpassword" id="cpassword"><br />
				<span id="errorMessage"><%= rError %></span><br />
				<span id="balanceInfo">Starting balance: $500</span><br />
				<div id="avatarBlock">Avatar Block</div>
				<button name="submit" id="submit" onclick="submit">Register</button>
			</form>
		</div>
	</body>
</html>