<!DOCTYPE html>

<html>


	<head>

	</head>


	<body>

		<table id="leaderboard" class="table">
		
		<%
			
				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;				
				
				try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost/BlackJackDB?user=INSERTUSERNAME&password=INSERTPASSWORD");
    				ps = conn.prepareStatement("SELECT username, score FROM Users ORDER BY score ASC;");
    				rs = ps.executeQuery();
    				
    				while (rs.next()) {
    					out.println("<tr class='row'> <td>" + rs.getString("username") + "</td> <td>" +
    					rs.getInt(score) + " </td></tr>");
    				}
    				
    				
				} catch (ClassNotFoundException nfe) {
    				System.out.println(nfe.getMessage());
  			} catch (SQLException sqle) {
    				System.out.print(sqle.getMessage());
  			}	
		
		%>
		
	</table>

	</body>


</html>