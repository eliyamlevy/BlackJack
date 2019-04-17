<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Chat Room</title>
		<script>
			var socket;
			
			function connectToServer() {
				socket = new WebSocket("ws://localhost:8080/BlackJackProject/bjs");
				socket.onopen = function(event) {
					document.getElementById("mychat").innerHTML += "Connected!<br />";
				}
				socket.onmessage = function(event) {
					document.getElementById("mychat").innerHTML += event.data + "<br />";
				}
				socket.onclose = function(event) {
					document.getElementById("mychat").innerHTML += "Disconnected!<br />";
				}
			}
			
			function sendCommand() {
				socket.send(document.chatForm.message.value);
				return false;
			}
			
			function createTable() {
				socket.send("yamio|CMD|NEWTABLE|6");
				return false;
			}
		</script>
	</head>
	<body onload="connectToServer()">
		<form name="chatForm" onsubmit="return sendCommand()">
			<input type="text" name="message"> <br />
			<input type="submit" name="submit" value="Send Message">
		</form>
		<br />
		<form name="chatForm" onsubmit="return createTable()">
			<input type="text" name="message"> <br />
			<input type="submit" name="submit" value="Send Message">
		</form>
		<br />
		<div id="mychat"></div>
	</body>
</html>