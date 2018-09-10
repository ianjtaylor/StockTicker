$(document).ready(function(){
	// Set up Web Sockets stuff

	$("#submit").click(function(){
		var stock = $("#stock").val();
		if (stock != '' && stock != undefined) {
			subscribe(stock);
		}
		$("#stock").val('');
	});
	
	function subscribe(stock){
		var ws = new WebSocket("ws://localhost:9000/subscribe");

		ws.onmessage = function(event){
			var row = JSON.parse(event.data);
			var tableRow = $("#" + row.symbol);
			if (tableRow.length > 0) {
				var newPrice = '$' + row.price.toFixed(2);
				var rowPriceId = "#" + row.symbol + "-price"
				var oldPrice = $(rowPriceId)[0].innerText;
				if (newPrice != oldPrice) {
					$(rowPriceId).html('$' + row.price.toFixed(2));

					tableRow.addClass("highlight");

					setTimeout(function(){
						tableRow.removeClass("highlight");
					}, 1000);
				}
			}
			else {
				$("#tbody").append("<tr id='" + row.symbol + "'>" + 
					"<td>" + row.symbol + "</td>" +
					"<td>" + row.name + "</td>" +
					"<td id='" + row.symbol + "-price'>" + '$' + row.price.toFixed(2)+ "</td>" +
					"</tr>");

				var tableRow = $("#" + row.symbol);
				tableRow.addClass("highlight");

				setTimeout(function(){
					tableRow.removeClass("highlight");
				}, 1000);
			}
		}

		ws.onopen = function(){
			ws.send(stock);
			// setInterval(function(){
			// 	ws.send(stock);
			// }, 5000);
		}
	}
})