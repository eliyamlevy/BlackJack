                                                                                                                                                                                          
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
				}
				
				socket.onmessage = function(event) {
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
			                                                                                                                                                                                                                                                                                                                                                       
		</script>                                                                                                                                                                         
	</head>                                                                                                                                                                               
	<body onload="connectToServer()">                                                                                                                                                     
		<form name="chatForm" onsubmit="return sendMessage();">                                                                                                                            
			<input type="text" name="message"> <br>                                                                                                                                     
			<input type="submit" name="submit" value="Send Message">                                                                                                                      
		</form>                                                                                                                                                                                                                                                                                                                                       
		<br />   
		<div id="reconnect" style="display: none;">
			<form name="reconnectForm" onsubmit="return connectToServer();">                                                                                                                                 
				<input type="submit" name="submit" value="Reconnect to server?">                                                                                                                      
			</form> 
		</div>                                                                                                                                                                         
		<div id="mychat"></div>                                                                                                                                                           
	</body>                                                                                                                                                                               
</html>                                                                                                                                                                                   