                                                                                                                                                                                          
<!DOCTYPE html>                                                                                                                                                                           
<html>                                                                                                                                                                                    
	<head>                                                                                                                                                                                
		<meta charset="UTF-8">                                                                                                                                                            
		<title>Chat Room</title>                                                                                                                                                          
		<script>                                                                                                                                                                          
			var socket;                                                                                                                                                                   
			                                                                                                                                                                              
			function connectToServer() {
				
				socket = new WebSocket("ws://localhost:8080/BlackJack/ws");
				
				socket.onopen = function(event) {
					document.getElementById("mychat").innerHTML += "Connected<br>";
					document.getElementById("reconnect").style.display = "none";
					document.getElementById("tableStatus").innerHTML = "Not In Table";
				}
				
				socket.onmessage = function(event) {
					
					var info = event.data.split("|");
					
					if (info[0] === "UPD") {
						if (info[1] === "INTABLE") {
							document.getElementById("tableStatus").innerHTML = "In Table";
							document.getElementById("playerCount").innerHTML = info[2];
						}
					}
					
					else document.getElementById("mychat").innerHTML += event.data + "<br>";
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
			
			
			                                                                                                                                                                                                                                                                                                                                                       
		</script>                                                                                                                                                                         
	</head>                                                                                                                                                                               
	<body onload="connectToServer()">                                                                                                                                           
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
		<br /> 
		<div id="reconnect" style="display: none;">
			<form name="reconnectForm" onsubmit="return connectToServer();">                                                                                                                                 
				<input type="submit" name="submit" value="Reconnect to server?">                                                                                                                      
			</form> 
		</div> 
		<div id="Status">
		In Table or Not: <div id="tableStatus"> </div> 
		<br>
		Amount of Players in Table: <div id="playerCount"> </div> 
		
		</div>

		<br>
		                                                                                                                                                                       
		<div id="mychat">
		Messages: <br>
		</div>                                                                                                                                                           
	</body>                                                                                                                                                                               
</html>                                                                                                                                                                                   