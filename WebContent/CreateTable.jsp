<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Create New Table</title>
		<link rel="stylesheet" type="text/css" href="Assets/createtable.css" />
	</head>
	<body>
		<div id="background">
		</div>
		<div id="container">
			<span id="header">Creating a new table</span>
			<form class="form" name="createTable" id="createTable" method="POST" action="CreateTable">
				Mode 
				<select>
				  <option value="freeforall">Free For All</option>
				  <option value="allagainstdealer">All Against Dealer</option>
				</select><br />
				Minimum Bet <br /><input type="number" name="minbet" id="minbet" min="1"><br />
				Max number of players (1-10) <br /><input type="number" name="numAI" id="numAI" min="1" max="10"><br />
				Number of AI players (0-9)<br /><input type="number" name="numAI" id="numAI" min="0" max="9"><br />
				<button name="submit" id="submit" onclick="submit">Apply and join</button>
			</form>
		</div>
	</body>
</html>