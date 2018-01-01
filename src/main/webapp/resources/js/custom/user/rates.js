$(document).ready(function(){
	
	getExpiredAuctions();
	checkForUser();
	getUnreadMessages();
	initListeners();
});

var ratings = {};
function initListeners(){
	
	$("#submit-rating").click(function(event){
		var dataArray = [];
		$("#rating-table tbody tr").each(function(i, row){
			
			var dataObject = {};
			var row = $(row);
			var username = row.find('.user').text();
			
			var role = row.find('.role').text();
			var rate = row.find('input[name="score"]').val();
			
			dataObject["username"] = username;
			dataObject["role"] = role;
			dataObject["rate"] = rate;
			dataArray.push(dataObject)
			//console.log("username: " + username + " --- rate: " + rate);
			console.log(dataObject)
			//alert("username: " + username + " --- role: " + role + " --- rate: " + rate);
			
		});
		if(dataArray.length == 0){
			alert("Sorry there is no bid")
		} else {
			var jsonArray = JSON.stringify(dataArray);
			//console.log("JSON TO SEND")
			//console.log(jsonArray)
			submitRate(jsonArray);
		}
		
	});
	
}

function initRatingModule(counterRow){
	 var elem = "#"+counterRow;
	 //console.log(elem)
	 var options = {
             max_value: 6,
             step_size: 0.5,
             selected_symbol_type: 'utf8_star',
             update_input_field_name: $(elem),
         }
	 $(".rate").rate(options);
	 $(".rate").on("change", function(ev, data){
         //console.log("data to: " + data.to)
     });
	
}

function getExpiredAuctions(){
	
	var patharray = window.location.pathname.split( '/' );
	var location = baseURL+"/"+patharray[2]+"/"+patharray[3];
	
	var username = getUser();
	var dest = location + "/user-expired-auctions";
	
	$.ajax({
		type : "GET",
		url  : dest,
		dataType : 'json',
		data : {username:username},
		success:function(expired){
			
			//console.log(expired);
			if(expired.length == 0){
				$("#rating-table").css("display","none");
				$("#btn-area").css("display","none");
				$("#no-rateData").css("display","block");
			} else {
				var counterRow = 0;
				$.each(expired, function(i, item) {
			
				    var auctionID = item.auctionID;
				    var seller = item.seller;
				    var bidder = item.bidder;
				    var item_title = item.item_title;
				    
				    var starRateModule = "<div class=\"rate\" style=\"margin:0 auto;font-size:24px;\"></div>" +
				    		"<div class=\"col-xs-3\" style=\"float:none; margin:0 auto;\">"+"<input name=\"score\" class=\"form-control input-sm\" id='"+ counterRow + "' "+"type=\"text\"></div>";
				    			    
				    if(bidder == username){
				    	var row =  "<tr id=' " + auctionID+"-"+seller + '-seller'+  "'> " +
				    	 "<td class=\"user\">" + 
						seller            
						+ "</td> " 
						+ "<td class=\"role\">" +
						"Seller" 
						+ "</td>" 
						+ " <td class=\"title\"> " +
						item_title
						+ "</td> "
						+ "<td>" +
						"<div id=\"wrapper\" style=\"text-align: center\">" +
						starRateModule 
						+ "</div></td> </tr>";
				    	
				    	$("#rating-table").find('tbody').append(row);
				    	/*data["username"] = seller;
				    	data["role"] = "seller";
				    	data["auctionID"] = auctionID;*/
				    	 
				    } else {
				    	var row =  "<tr id=' " + auctionID+"-"+bidder + '-bidder'+  "'> <td class=\"user\">" + 
						bidder               
						+ "</td> " 
						+ "<td class=\"role\">" +
						"Bidder" 
						+ "</td>" 
						+ " <td class=\"title\"> " +
						item_title
						+ "</td> "
						+ "<td>" + 
						starRateModule 
						+ "</td> </tr>";

				    	$("#rating-table").find('tbody').append(row);
				    	/*data["username"] = bidder;
				    	data["role"] = "bidder"; 
				    	data["auctionID"] = auctionID;*/
				    }
				    
				    initRatingModule(counterRow);
				    counterRow++;
				    
				});
				
			}
			
			
		}
	});
}

function submitRate(ratings){
	$.ajax({
		type : "POST",
		dataType:'json',
		url  : window.location.href + "/submit-rates",
		data : {ratings:ratings},
		success:function(result){
			$("#rate-text").html("Thank you for your rating");
			$("#ratesModal").modal('show');
			$("#rates-btn").click(function(){
				window.location.reload();
			});
		},
		
	});
	
}