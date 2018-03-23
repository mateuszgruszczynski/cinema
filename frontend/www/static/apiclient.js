var host = "gateway.host";

function getMovies() {
				$.ajax({
					url: "http://" + host + ":8000/movies?page=1",
					headers: {'Authorization': '94ab54b5-f120-4290-a4ec-587dee09206e'},
					success: function(moviedata) {
						for(index = 0; index < moviedata.length; ++index){
							$("#movies").append("<h2>" + moviedata[index]['title'] + "</h2><div class=\"poster\"></div><div class=\"scr\" id=\"scr_"+ moviedata[index]['id'] +"\"></div>");
							getMovieScreenings(moviedata[index]['id']);
						}
					}
				})
			};
function getMovieScreenings(movieid){
				$.ajax({
					url: "http://" + host + ":8000/movies/" + movieid + "/screenings?page=1",
					headers: {"Authorization": "94ab54b5-f120-4290-a4ec-587dee09206e"},
					success: function(screendata) {
						for(index = 0; index < screendata.length; ++index){
							$("#scr_" + movieid).append("<span class=\"screening\">" + screendata[index]['city'] + " at " + screendata[index]['time'] + "<button>Buy ticket</button></span>");
						}
					}
				})
			};
function sendLogin(){
				$.ajax({
				  url: "http://" + host + ":8000/authenticate",
				  type:"POST",
				  data: '{"username":"' + $("#lg").val() + '", "password":"' + $("#ps").val() + '"}',
				  contentType:"application/json; charset=utf-8",
				  headers: {"Authorization": "94ab54b5-f120-4290-a4ec-587dee09206e"},
				  dataType:"json",
				  success: function(response){
						$("#login").empty();
						$("#login").append("<span id=\"ok\">Logged in succesfully</span>");
					},
				  error: function(response){
						$("#error").remove();
						$("#login").append("<span id=\"error\">Invalid credentials</span>");
					},
					
				})
			};
