                                                                                                                                                                                          
<!DOCTYPE html>                                                                                                                                                                           
<html>                                                                                                                                                                                    
	<head>                                                                                                                                                                                
		<meta charset="UTF-8">                                                                                                                                                            
		<title>Chat Room</title>                                                                                                                                                          
		<script>                                                                                                                                                                          
			var socket;  
			
			function list() {
				var message = "a|CMD|LIST";
				socket.send(message);
				return false;
			}
			                                                                                                                                                                              
			function connectToServer() {
				
				socket = new WebSocket("ws://localhost:8080/BlackJack/ws");
				
				socket.onopen = function(event) {
					document.getElementById("mychat").innerHTML += "Connected<br>";
					document.getElementById("reconnect").style.display = "none";
					document.getElementById("tableStatus").innerHTML = "Not In Table";
					list();
				}
				
				socket.onmessage = function(event) {
					
					var info = event.data.split("|");
					
					if (info[0] === "UPD") {
						if (info[1] === "INTABLE") {
							document.getElementById("tableStatus").innerHTML = "In Table";
							
							if (info[2] === "WAITING") {
								document.getElementById("roundStatus").innerHTML = "Not in Round";
							}
							
							else document.getElementById("roundStatus").innerHTML = "In Round";
							
							document.getElementById("playerCount").innerHTML = info[3];
							document.getElementById("playerList").innerHTML = "";
							var playerCount = info[3];
							
							for (i = 0; i<playerCount; i++) {
								document.getElementById("playerList").innerHTML += i+1 + ": " + info[4+i] + "<br>";
							}
							
						}
					}
					
					else if (info[0] == "LIST") {
						document.getElementById("tables").innerHTML = "";
						var tableCount = info[1];
						
						var index = 1;					
						
						for (i = 0; i<tableCount; i++) {
							document.getElementById("tables").innerHTML += "Table " + info[++index] + " has " + info[++index] + " open spots, and is owned by " +  info[++index] + "<br>";
						}
						
					}
					
					document.getElementById("mychat").innerHTML += event.data + "<br>";
				}
				
				socket.onclose = function(event) {
					document.getElementById("mychat").innerHTML += "Disconnected<br>";
					document.getElementById("reconnect").style.display = "block";
				}
				return false;
			}
			
			function sendMessage() {
				socket.send(document.chatForm.message.value);
				return false;
			}
			
			function createTable() {
				var message = document.createTableForm.username.value + "|CMD|NEWTABLE|" + document.createTableForm.size.value;
				socket.send(message);
				return false;
			}
			
			function joinTable() {
				var message = document.joinTableForm.username.value + "|CMD|JOINTABLE|" + document.joinTableForm.index.value;
				socket.send(message);
				return false;
			}
			
			function ready() {
				var message = document.commonInputs.username.value + "|ACT|READY";
				socket.send(message);
				return false;
			}
			
			function start() {
				var message = document.commonInputs.username.value + "|ACT|START";
				socket.send(message);
				return false;
			}
			
			function hit() {
				var message = document.commonInputs.username.value + "|ACT|HIT";
				socket.send(message);
				return false;
			}
			
			function stay() {
				var message = document.commonInputs.username.value + "|ACT|STAY";
				socket.send(message);
				return false;
			}
			                                                                                                                                                                                                                                                                                                                                                       
		</script>                                                                                                                                                                         
	</head>                                                                                                                                                                               
	<body onload="connectToServer();">                                                                                                                                           
		<form name="chatForm" onsubmit="return sendMessage();">                                                                                                                             
			Message: <input type="text" name="message"> <br>                                                                                                                                     
			<input type="submit" name="submit" value="Send Message">                                                                                                                      
		</form>                                                                                                                                                                                                                                                                                                                                       
		<br />   
		<form name="createTableForm" onsubmit="return createTable();"> 
			Create Table: <br>
			Username: <input type="text" name="username"> <br>                                                                                                                              
			Size: <input type="text" name="size"> <br>                                                                                                                                     
			<input type="submit" name="submit" value="Create Table">                                                                                                                      
		</form>                                                                                                                                                                                                                                                                                                                                       
		<br />  
		<form name="joinTableForm" onsubmit="return joinTable();"> 
			Join Table: <br>
			Username: <input type="text" name="username"> <br>                                                                                                                              
			Index of Table: <input type="text" name="index"> <br>                                                                                                                                     
			<input type="submit" name="submit" value="Join Table">                                                                                                                      
		</form> 
		
		<br>
		<br>
		
		<form name="commonInputs">
			In-round Inputs: <br>
			Username: <input type="text" name="username"> <br> 
			<input type="button" value="Ready" onclick="return ready();">
			<input type="button" value="Start" onclick="return start();">
			<input type="button" value="Hit" onclick="return hit();">
			<input type="button" value="Stay" onclick="return stay();">
			<input type="button" value="List" onclick="return list();">
		
		</form>
		
		                                                                                                                                                                                                                                                                                                                                      
		<br /> 
		<div id="reconnect" style="display: none;">
			<form name="reconnectForm" onsubmit="return connectToServer();">                                                                                                                                 
				<input type="submit" name="submit" value="Reconnect to server?">                                                                                                                      
			</form> 
		</div> 
		<div id="Status">
		Active Tables: <div id="tables"> </div> 
		<br>
		In Table or Not: <div id="tableStatus"> </div> 
		<br>
		In Round or Not: <div id="roundStatus"> </div> 
		<br>
		Amount of Players in Table: <div id="playerCount"> </div> <br>
		
		List of Players in Table: <div id="playerList"> </div> 
		
		</div>

		<br>
		                                                                                                                                                                       
		<div id="mychat">
		Messages: <br>
		</div>                                                                                                                                                           
	</body>                                                                                                                                                                               
</html>                                                                                                                                                                                   