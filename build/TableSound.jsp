	
<link rel="shortcut icon" href="Assets/favicon.ico" type="image/x-icon">
<body onload="loadSound()">  

<script>
	var win;
	var bet;
	var hit;
	var shuffle;
	var lose;
	function loadSound(){
		win = new sound("Assets/win.wav");
		bet = new sound("Assets/chip.wav");
		hit = new sound("Assets/takeCard.wav");
		shuffle = new sound("Assets/shuffle.wav");
		lose = new sound("Assets/lose.mp3");			
	}
	
	
	function sound(src) {
	    this.sound = document.createElement("audio");
	    this.sound.src = src;
	    this.sound.setAttribute("preload", "auto");
	    this.sound.setAttribute("controls", "none");
	    this.sound.style.display = "none";
	    document.body.appendChild(this.sound);
	    this.play = function(){
	        this.sound.play();
	    }
	    this.stop = function(){
	        this.sound.pause();
	    }    
	}
</script>