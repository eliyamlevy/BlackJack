<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Account Information</title>
		<link rel="stylesheet" type="text/css" href="Assets/accountinfo.css" />
	</head>
	<body>
		<div id="background">
		</div>
		<div id="container">
			<img id="avatar" src="Assets/jeffrey_miller.jpg" alt="avatar">
			<span id="greeting">Hi, Jeff!</span><br />
			<table id="info">
				<tr>
					<td>Full Name</td>
					<td>Jeffrey Miller</td>
				</tr>
				<tr>
					<td>Email</td>
					<td>jeffrey.miller@usc.edu</td>
				</tr>
				<tr>
					<td>Balance</td>
					<td>$800</td>
				</tr>
				<tr>
					<td>Tokens <br />holding</td>
					<td>0</td>
				</tr>
				<tr>
					<td>Ranking</td>
					<td>#21</td>
				</tr>
			</table>
			<button name="toleader" id="toleader" onclick="window.location='Leaderboard.jsp'">see leaderboard...</button>
		</div>
	</body>
</html>