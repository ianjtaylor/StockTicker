$(document).ready(function(){
	// Set up Web Sockets stuff

	$("#submit").click(function(){
		subscribe($("#stock").val())
	});
	
	function subscribe(stock){
		var ws = new WebSocket("ws://localhost:9000/subscribe");

		ws.onmessage = function(event){
			console.log("got a message!");

			$("#messages").append("<div>" + event.data + "</div>");
		}

		ws.onopen = function(){
			ws.send(stock);
		}
	}
})