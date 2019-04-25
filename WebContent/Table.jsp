                                                                                                                                                                                          
<!DOCTYPE html>                                                                                                                                                                           
<html>                                                                                                                                                                                    
	<head>                                                                                                                                                                                
		<meta charset="UTF-8">                                                                                                                                                            
		<title>Welcome to BlackJack</title>    
		<style>
			#actualPage div {
				display: block;
				position: relative;
				padding: 20px;
				border-style: solid;
  				border-width: 2px;
  				margin: 10px;
  				overflow: auto;
			}
			
			.card {
				height: 80px;
				width: auto;
			}
			
			body{
				color: gold;
			}
			
			#actualPage div {
				/* display: block; */
				position: relative;
				padding: 20px;
				border-style: solid;
				border-width: 2px;
				margin: 10px;
				z-index: 10;
			}
			
			#background {
				position:fixed;
				background-image: url("Assets/table.jpg"); 
				background-repeat: no repeat;
				background-size: cover;
				height: 103vh;
				width: 103vw;
				left: -1vw;
				top: -1vh;
				z-index: 1;
			}
			
			#usernameDiv {
				width: 20%;
			}
			
			#tableStatus {
				display: none;
			}
			
			#tableStuff {
				display: none;
			}
			
			#openTable {
				display: block;
				width: 40%;
				position: relative;
				top: 50%;
			}
			
			#debugSwitch {
				position: relative;
				z-index: 10;
			}
			
			.tableList {
				float: left;
				display: grid;
				grid-template-columns: 17% 17% 17% 17%;
				grid-gap: 5%;
				padding: 5%;
				width: 100%;
			}
			
			.table {
				padding: 20px;
				margin: 20px;
				width: 19%;
				height: auto;
				float: left;
				background-color: rgb(255, 255, 255, .04);
			}
					
		</style>                                                                                                                                                      
		<script>                                                                                                                                                                          
			var socket;  
			
			var username = "<%= (String) session.getAttribute("user") %>";

			var state = 0;
			
			if(username != null) {
				state = 1;
			}
			
			var currentBalance;

			function setUsername() {
				state = 1;
				username = document.getUsernameForm.username.value;
				document.getElementById("usernameDiv").innerHTML = "Hello " + username + "!";
				showStateDiv();
				return false;
			}
			
			function list() {
				var message = "dev|CMD|LIST";
				socket.send(message);
				return false;
			}
			                                                                                                                                                                              
			function connectToServer() {
				
				socket = new WebSocket("ws://localhost:8080/BlackJack/ws");
				
				socket.onopen = function(event) {
					document.getElementById("mychat").innerHTML += "Connected<br>";
					document.getElementById("reconnect").style.display = "none";
					showStateDiv();
					list();
				}
				
				socket.onmessage = function(event) {
					
					var info = event.data.split("|");
					
					if (info[0] === "UPD") {
						if(info[1] === "OUTTABLE") {
							state = 1;
						}
						else if(info[1] === "ENDGAME") {
							for(i = 0; i < info[2]; i++) {
								if(info[3+2*i] === username) {
									console.log(info[4+2*i]);
									if(info[4+2*i] === "WIN") {
										alert("You won this hand!");
									}
									else if (info[4+2*i] === "LOSS") {
										alert("You lost this hand :(");
									}
									
									else {
										alert("You tied with the dealer.");
									}
								}
								state = 2;
							}
						}
						else if (info[1] === "INTABLE") {
							if (info[2] === "WAITING") {
								state = 2;
								//Clear PlayerList
								document.getElementById("playerList").innerHTML = "";
								//Get num players
								var playerCount = info[3];
								//List all players
								for (i = 0; i < playerCount; i++) {
									var playerLine = (i+1) + ": ";
									currentBalance = info[5+3*i];
									if(info[6+3*i] === "READY") {
										if(info[4+3*i] === username) {
											document.getElementById("balance").innerHTML = "Your Balance: " + currentBalance;
											playerLine += "<b><i>" + info[4+3*i] + "</i></b> <br />";
										}
										else {
											playerLine += "<b><i>" + info[4+3*i] + ": Balance: " +  currentBalance + "</i></b> <br />";
										}
									}
									else {
										if(info[4+3*i] === username) {
											document.getElementById("balance").innerHTML = "Your Balance: " + currentBalance;
											playerLine += info[4+3*i] + "<br />";
										}
										else {
											playerLine += info[4+3*i] + ": Balance: "  + currentBalance + "<br />";
										}
									}
									document.getElementById("playerList").innerHTML += playerLine;
								}
							}
							else if(info[2] === "INROUND") {
								//Clear PlayerList
								document.getElementById("playerList").innerHTML = "";
								//Get num players
								var playerCount = info[3];
								var index = 4;
								for (i = 0; i < playerCount; i++) {
									var playerLine = (i+1) + ": ";
									if(info[++index] === "TURN") {
										playerLine += "<b><i>" + info[index - 1] + "</i></b>:";
										if(info[index - 1] === username) {
											currentBalance = info[++index];
											document.getElementById("balance").innerHTML = "Your Balance: " + currentBalance;
											playerLine += "<br />";
											state = 4;
											var numCards = info[++index];
											//Update Hand
											document.getElementById("hand").innerHTML = "Your Hand: <br />";
											for(j = 0; j < numCards; j++) {
												document.getElementById("hand").innerHTML += "<img class='card' src='Assets/CardsForWebsite/" + info[++index] + ".png'> ";
											}
										}
										else {
											playerLine += " Balance: " + info[++index] + " <br />";
											var numCards = info[++index];
											index += parseInt(numCards);
										}
									}
									else {
										playerLine += info[index - 1];
										if(info[index - 1] === username) {
											currentBalance = info[++index];
											document.getElementById("balance").innerHTML = "Your Balance: " + currentBalance;
											state = 3;
										}
										else {
											playerLine += ": Balance: " + info[++index] + " <br />";
										}
										var numCards = info[++index];
										index += parseInt(numCards);
									}
									document.getElementById("playerList").innerHTML += playerLine;
									++index;
								}
								//Now its the dealer display
								var dNumCards = info[++index];
								index++;
								document.getElementById("dealer").innerHTML = "";
								for(d = 0; d < dNumCards; d++) {
									if(d < 1) {
										document.getElementById("dealer").innerHTML += "<img class='card' src='Assets/CardsForWebsite/" + info[index + d] + ".png'> ";
									}
									else {
										document.getElementById("dealer").innerHTML += "<img class='card' src='Assets/CardsForWebsite/red_back.png'> ";
									}
								}
							}
							else {
								document.getElementById("playerList").innerHTML = "";
								var playerCount = info[3];
								for (i = 0; playerCount; i++) {
									var playerLine = i+1 + ": " + info[4+i] + " <br />";
									document.getElementById("playerList").innerHTML += playerLine;
								}
							}
						}
					}
					
					else if (info[0] === "LIST") {
						
						document.getElementById("tables").innerHTML = "";
						var tableCount = parseInt(info[1]);
						var index = 1;					
						
						for (i = 0; i<tableCount; i++) {
							document.getElementById("tables").innerHTML += "<div class='table' onclick='joinTable(" + info[++index] + ")'>Table: " + " has " + info[++index] + " open spots, and is owned by " +  info[++index] + "</div>"
						}
					}
					
					else if(info[0] === "ERR") {
						if(info[1] === "OUTTABLE") {
							if(info[2] === "TABDNE") {
								alert("Table does not exist");
							}
							else if(info[2] === "TABFULL") {
								alert("Given table is full");
							}
							else if(info[2] === "SIZEINV") {
								alert("Size provided is invalid");
							}
							else if(info[2] === "GUEST") {
								alert("Guests cannot create games, please register to create a game");
							}
						}
						else if(info[1] === "WAITING") {
							if(info[2] === "WAITING") {
								alert("Cannot start game until every player is ready");							}
						}
						else if(info[1] === "PLAYING") {
							if(info[2] === "INVLEAVE") {
								alert("Cannot leave the table in the middle of a round");
							}
							else if(info[2] === "INVBET") {
								alert("Invalid Bet");
							}
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
				showStateDiv();
				return false;
			}
			
			function joinTable(tableNum) {
				var message = username + "|CMD|JOINTABLE|" + tableNum;
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
				var message = username + "|ACT|LEAVE";
				socket.send(message);
				return false;
			}
			
			function showStateDiv() {
				//alert("State: " + state);
				if(state === 0) {
					document.getElementById("tableStuff").style.display = "none";
					document.getElementById("openTable").style.display = "none";
					document.getElementById("tableStatus").style.display = "none";
				}
				else if(state === 1) {
					document.getElementById("usernameDiv").style.display = "none";
					document.getElementById("tableStuff").style.display = "block";
					document.getElementById("openTable").style.display = "none";
					document.getElementById("tableStatus").style.display = "none";
				}
				else if(state === 2) {
					document.getElementById("tableStuff").style.display = "none";
					document.getElementById("openTable").style.display = "block";
					document.getElementById("tableStatus").style.display = "block";
					document.getElementById("yourTurn").style.display = "none";
					document.getElementById("notTurn").style.display = "none";
					document.getElementById("tableWait").style.display = "block";
					document.getElementById("hand").innerHTML = "Your hand: ";
					document.getElementById("dealer").innerHTML = "Dealer's hand: ";
				}
				else if(state === 3) {
					document.getElementById("yourTurn").style.display = "none";
					document.getElementById("notTurn").style.display = "block";
					document.getElementById("tableWait").style.display = "none";
				}
				else if(state === 4) {
					document.getElementById("yourTurn").style.display = "block";
					document.getElementById("notTurn").style.display = "none";
					document.getElementById("tableWait").style.display = "none";
				}
			}
			
			
			function showDebug() {
				document.getElementById("debuggingTools").style.display = "block";
				document.getElementById("debugSwitch").style.display = "none";
				return false;
			}
			
			function hideDebug() {
				document.getElementById("debuggingTools").style.display = "none";
				document.getElementById("debugSwitch").style.display = "block";
				return false;
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
		<div id="background"></div>
		<div id="actualPage"> 
			<div id="usernameDiv">
				<form name="getUsernameForm" onsubmit="return setUsername();"> 
					Username: <input type="text" name="username">                                                                                                                     
					<input type="submit" name="submit" value="Login">                                                                                                                   
				</form> 
			</div>
			<div id="tableStatus" style="margin: 5px;">
				List of Players in Table: <div id="playerList"> </div> 
			</div>
			<div id="tableStuff">
				<div id="tableList">
					Active Tables: <div id="tables" class="tableList"> </div> 
				</div> 
				<div id="createTable">
					<form name="createTableForm" onsubmit="return createTable();"> 
						Create Table: <br>
						Size: <input type="text" name="size"> <br>                                                                                                                                     
						<input type="submit" name="submit" value="Create Table">                                                                                                                      
					</form> 
				</div>
				<button href="AcountInfo.jsp">Acount Info</button>
			</div>
			
			<div id="openTable">
				In Table:
				<div id="dealer">Dealers Hand:<br /></div>
				<div id="hand">Your Hand:</div>
				<div id="balance">Your Balance:</div>
				<div id="yourTurn">
					<form name="yourTurnForm">
						Your Turn, Please Select a Move: <br>
						<input type="button" value="Hit" onclick="return hit();">
						<input type="button" value="Stay" onclick="return stay();">
						<br />
						<input type="text" name="betInput"> 
						<input type="button" value="Bet" onclick="return bet();">
					</form>
					<div class="error" id="playingErrors"></div>
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
					<div class="error" id="waitingErrors"></div>
				</div>
			</div>         
		</div>
		
		<br /><br />
		
		<form id="debugSwitch" name="debugging" onsubmit="return showDebug();">                                                                                                                                                                                                                                                 
			<input type="submit" name="debugging" value="Debug?">                                                                                                                      
		</form>   
		
		<!-- This stuff is the old stuff -->   
		<div id="debuggingTools" style="display: none;">
			<h3>Debugging Tools</h3>      
			                                                                                                                       
			<form name="chatForm" onsubmit="return sendMessage();">                                                                                                                             
				Console Input: <input type="text" name="message"> <br>                                                                                                                                     
				<input type="submit" name="submit" value="Send Message">                                                                                                                      
			</form>    
			
			<br>
			
			<div id="reconnect" style="display: none;">
				<form name="reconnectForm" onsubmit="return connectToServer();">                                                                                                                                 
					<input type="submit" name="submit" value="Reconnect to server?">                                                                                                                      
				</form> 
			</div> 
			
			<div id="mychat">
			Console: <br>
			</div>  
			
			<form name="hideDebugging" onsubmit="return hideDebug();">                                                                                                                                                                                                                                                 
				<input type="submit" name="debugging?" value="Hide Debug?">                                                                                                                      
			</form>  
		</div>                                                                                                                                             
	</body>                                                                                                                                                                               
</html>                                                                                                                                                                                   