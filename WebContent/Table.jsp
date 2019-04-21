                                                                                                                                                                                          
<!DOCTYPE html>                                                                                                                                                                           
<html>                                                                                                                                                                                    
	<head>                                                                                                                                                                                
		<meta charset="UTF-8">                                                                                                                                                            
		<title>Table</title>    
		<style>
			#actualPage div {
				display: block;
				position: relative;
				padding: 20px;
				border-style: solid;
  				border-width: 2px;
  				margin: 10px;
			}
		</style>                                                                                                                                                      
		<script>                                                                                                                                                                          
			var socket;  
			
			var username;

			var state = 0;
			
			function setUsername() {
				state = 1;
				username = document.getUsernameForm.username.value;
				document.getElementById("usernameDiv").innerHTML = "Hello " + username + "!";
				showStateDiv();
				return false;
			}
			
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
					showStateDiv()
				}
				
				socket.onmessage = function(event) {
					
					var info = event.data.split("|");
					
					if (info[0] === "UPD") {
						if (info[1] === "INTABLE") {
							document.getElementById("tableStatus").innerHTML = "In Table";
							
							if (info[2] === "WAITING") {
								document.getElementById("roundStatus").innerHTML = "Not in Round";
								state = 2;
							}
							
							else { 
								document.getElementById("roundStatus").innerHTML = "In Round";
								state = 3;
							}
							
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
					showStateDiv();
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
				var message = username + "|CMD|NEWTABLE|" + document.createTableForm.size.value;
				socket.send(message);
				state = 2;
				showStateDiv();
				return false;
			}
			
			function joinTable() {
				var message = username + "|CMD|JOINTABLE|" + document.joinTableForm.index.value;
				socket.send(message);
				state = 2;
				showStateDiv();
				return false;
			}
			
			function ready() {
				var message = username + "|ACT|READY";
				socket.send(message);
				return false;
			}
			
			function start() {
				var message = username + "|ACT|START";
				socket.send(message);
				return false;
			}
			
			function hit() {
				var message = username + "|ACT|HIT";
				socket.send(message);
				return false;
			}
			
			function stay() {
				var message = username + "|ACT|STAY";
				socket.send(message);
				return false;
			}
			
			function bet() {
				var message = username + "|ACT|BET|" + document.yourTurnForm.betInput.value;
				socket.send(message);
				return false;
			}
			
			function leave() {
				alert("Sorry you can't do that yet");
				return false;
			}
			
			function showStateDiv() {
				//alert("State: " + state);
				if(state === 0) {
					document.getElementById("tableStuff").style.display = "none";
					document.getElementById("openTable").style.display = "none";
				}
				else if(state === 1) {
					document.getElementById("tableStuff").style.display = "block";
					document.getElementById("openTable").style.display = "none";
				}
				else if(state === 2) {
					document.getElementById("tableStuff").style.display = "none";
					document.getElementById("openTable").style.display = "block";
					document.getElementById("yourTurn").style.display = "none";
					document.getElementById("notTurn").style.display = "none";
					document.getElementById("tableWait").style.display = "block";
				}
				else if(state === 3) {
					document.getElementById("yourTurn").style.display = "block";
					document.getElementById("notTurn").style.display = "block";
					document.getElementById("tableWait").style.display = "none";
				}
			}
			
			function sleep(milliseconds) {
				var start = new Date().getTime();
				for (var i = 0; i < 1e7; i++) {
				    if ((new Date().getTime() - start) > milliseconds){
				    	break;
					}
				}
			}
			                                                                                                                                                                                                                                                                                                                                                       
		</script>                                                                                                                                                                         
	</head>                                                                                                                                                                               
	<body onload="connectToServer();"> 
		<div id="actualPage"> 
			<div id="usernameDiv">
				<form name="getUsernameForm" onsubmit="return setUsername();"> 
					Username: <input type="text" name="username">                                                                                                                     
					<input type="submit" name="submit" value="Login">                                                                                                                   
				</form> 
			</div>
			
			<div id="tableStuff">
				<div id="tableList">
					Active Tables: <div id="tables"> </div> 
				</div> 
				<div id="joinTable">
					<form name="joinTableForm" onsubmit="return joinTable();"> 
						Join Table: <br>                                                                                                                              
						Index of Table: <input type="text" name="index"> <br>                                                                                                                                     
						<input type="submit" name="submit" value="Join Table">                                                                                                                      
					</form>
				</div>
				or ...
				<div id="createTable">
					<form name="createTableForm" onsubmit="return createTable();"> 
						Create Table: <br>
						Size: <input type="text" name="size"> <br>                                                                                                                                     
						<input type="submit" name="submit" value="Create Table">                                                                                                                      
					</form> 
				</div>
			</div>
			
			<div id="openTable">
				In Table:
				<div id="yourTurn">
					<form name="yourTurnForm">
						Your Turn, Please Select a Move: <br>
						<input type="button" value="Hit" onclick="return hit();">
						<input type="button" value="Stay" onclick="return stay();">
						<br />
						<input type="number" name="betInput"> 
						<input type="button" value="Bet" onclick="return bet();">
					</form>
				</div>
				<div id="notTurn">
					<!-- Add no turn stuff -->
					Not Turn Stuff
				</div>
				<div id="tableWait">
					<form name="tableWaitForm">
						Table Wait State Form: <br>
						<input type="button" value="Ready" onclick="return ready();">
						<input type="button" value="Start" onclick="return start();">
						<input type="button" value="Leave" onclick="return leave();">
					</form>
				</div>
			</div>         
		</div>
		<br /><br /><br /><br /><br />
		
		<!-- This stuff is the old stuff -->         
		                                                                                                                       
		<form name="chatForm" onsubmit="return sendMessage();">                                                                                                                             
			Console Input: <input type="text" name="message"> <br>                                                                                                                                     
			<input type="submit" name="submit" value="Send Message">                                                                                                                      
		</form>    
		                                                                                                                                                                                                                                                                                                                                   
		                                                                                                                                                                                                                                                                                                                                  
		<br />  
		
		
		
		<br>
		
		<div id="reconnect" style="display: none;">
			<form name="reconnectForm" onsubmit="return connectToServer();">                                                                                                                                 
				<input type="submit" name="submit" value="Reconnect to server?">                                                                                                                      
			</form> 
		</div> 
		
		<div id="Status">
			In Table or Not: <div id="tableStatus"> </div> 
			<br>
			In Round or Not: <div id="roundStatus"> </div> 
			<br>
			Amount of Players in Table: <div id="playerCount"> </div> <br>
			
			List of Players in Table: <div id="playerList"> </div> 
		</div>
		
		<div id="mychat">
		Console: <br>
		</div>                                                                                                                                                           
	</body>                                                                                                                                                                               
</html>                                                                                                                                                                                   