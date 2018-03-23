<?php
	error_reporting(0);
	$mysqli = new mysqli("mysql.host", "cinema", "cinemapass", "cinema_db");
?>
<html>
	<head>
		<title>Sample Cinema</title>
		<link rel="stylesheet" href="../style.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	</head>
	<body>
		<div id="container">
			<div id="header"></div>
			<div id="login">
				Login: <input id="lg" type="text">
				Password: <input id="ps" type="password">
				<button>Log in</button>
			</div>
			<div id="main">
			<h1>Latest Movies</h1>
			<div id="movies">
				<?php
					$moviesQuery = "SELECT * FROM movies LIMIT 10";
					$moviesResult = $mysqli->query($moviesQuery);
					if($moviesResult->num_rows != 0){
						while ($movie = $moviesResult->fetch_assoc()) {
							echo "<h2>";
							echo $movie['title'];
							echo "</h2><div class=\"poster\"></div><div class=\"scr\" id=\"scr_";
							echo $movie['id'];
							echo "\">";
							$screeningsQuery = "SELECT * FROM screenings WHERE movieid = ".$movie['id'];
							$screeningsResult = $mysqli->query($screeningsQuery);
							if($screeningsResult->num_rows != 0){
								while ($screening = $screeningsResult->fetch_assoc()) {
									echo "<span class=\"screening\">";
									echo $screening['city'];
									echo " at ";
									echo $screening['time'];
									echo "<button>Buy ticket</button></span>";
								}					
							}	
							echo "</div>";
						}
					}
				?>
			</div>
			</ul>
		</div>
	</body>
</html>
