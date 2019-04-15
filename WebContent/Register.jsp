<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Register</title>
		<link rel="stylesheet" type="text/css" href="Assets/register.css" />

		<script>

			function validate() {
				
				var xhttp = new XMLHttpRequest();

				xhttp.open("POST", "AddRegister?fullname=" + document.registerForm.fullname.value + 
					"&email=" + document.registerForm.email.value + "&username=" + document.registerForm.username.value + "&password=" + document.registerForm.password.value + "&cpassword=" + document.registerForm.cpassword.value, false);
		
				xhttp.send();
		
				if (xhttp.responseText.trim().length > 0) {
					document.getElementById("errorMessage").innerHTML = xhttp.responseText;
					return false;
				}
		
				alert('Account successfully created');
				return true;
			
			}



		</script>



	</head>

	<body>
		<div id="background">
		</div>
		<div id="container">
			<span id="header">Register</span>
			<form class="form" name="registerForm" id="registerForm" method="POST" onsubmit="return validate();" action="HomePage.jsp">
				Full Name <br /><input type="text" name="fullname" id="fullname"><br />
				Email <br /><input type="email" name="email" id="email"><br />
				Username <br /><input type="text" name="username" id="username"><br />
				Password <br /><input type="password" name="password" id="password"><br />
				Confirm Password <br /><input type="password" name="cpassword" id="cpassword"><br />
				<span id="errorMessage"></span><br />
				<span id="balanceInfo">Starting balance: $500</span><br />
				<div id="avatarBlock">Avatar Block</div>
				<input type="submit" value="Register">
			</form>
		</div>
	</body>
</html>