<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href="Assets/login.css" />

		<script>

				function validate() {
		
					var xhttp = new XMLHttpRequest();
					
					xhttp.open("GET", "CheckLogin?username=" + document.loginForm.username.value +
							  "&password=" + document.loginForm.password.value, false);
					
					xhttp.send();
					
					if (xhttp.responseText.trim().length > 0) {
						document.getElementById("errorMessage").innerHTML = xhttp.responseText;
						return false;
					}
					
					alert('Successful login!');
					return true;
		
				}

		</script>


	</head>

	<body>
		<div id="background">
		</div>
		<div id="container">
			<span id="header">Login</span>
			<form class="form" name="loginForm" id="loginForm" method="GET" onsubmit = "return validate();" action="HomePage.jsp">
				Username <br /><input type="text" name="username" id="username"><br />
				Password <br /><input type="password" name="password" id="password"><br />
				<span id="errorMessage"></span><br />
				<input type="submit" value="Login">
			</form>
		</div>
	</body>
</html>