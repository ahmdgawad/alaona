

$(document).ready(function(){
	
	//console.log("base url: " + baseURL);
	//console.log("window.location.href: " + window.location.href)
	
	var url = window.location.href;
	var itemID = url.substring(url.lastIndexOf("/")+1); 
	
	if(url.indexOf("item") == -1){
		console.log("item not requested yet")
		
	} else {
		
		getItemModule(itemID);
	}

});

/* --------- Global Variables ----------- */

var map;
var geocoder; // setting the map to the country selected
var location_marker;
var user_auctions;
var latitude;
var longtitude;

/* --------- Global Variables ----------- */

function setListeners(){
	//console.log("Setting listeners")
	$('#bidNow').click( function(event){
		//console.log("clicked bid now")
		
		var bid_amount = $("#bid-amount").val();
		var first_bid = $("#firstbid").text();
		var current_highBid = $("#highest-bid").text();
		//console.log("current highest: " + parseFloat(current_highBid));
		if(bid_amount == "") {
			$("#warningBid-text").html("Please offer a bid first!");
			$("#warningBidModal").modal('show');
		} else {
			//console.log("first_bid: " + first_bid + " and bid_amount: " + bid_amount);
			if(parseFloat(current_highBid) == 0){
				//console.log("There is no current bid");
				if(parseFloat(bid_amount) >= parseFloat(first_bid)){
					//console.log("bid amount given is greater than first bid");
					$("#confirm-bid-btn").css("display","block");
					$("#warningBid-text").html("Are you sure that you want to bid " + bid_amount + " $ ?");
					$("#warningBidModal").modal('show');
				} else {
					$("#warningBid-text").html("Sorry but your bid must be greater or equal than the first bid");
					$("#warningBidModal").modal('show');
				}
			} else {
				//console.log("Checking the current bid");
				if(parseFloat(bid_amount) > parseFloat(current_highBid)){
					//console.log("bid amount given is greater than current bid");
					$("#confirm-bid-btn").css("display","block");
					$("#warningBid-text").html("Are you sure that you want to bid " + bid_amount + " $ ?");
					$("#warningBidModal").modal('show');
				} else {
					$("#warningBid-text").html("Sorry but your bid must be greater or equal than the current highest bid (" + current_highBid +")");
					$("#warningBidModal").modal('show');
				}
			}
			
		}
		
	});
	
	$('#confirm-bid-btn').click(function(){
		
		//console.log("confirm bid button");
		var bid_amount = $("#bid-amount").val();
		submitOffer(bid_amount);
		$("#warningBidModal").modal('hide');
		
	});
}


function submitOffer(bid_amount){
	var url = window.location.href;
	var itemID = url.substring(url.lastIndexOf("/")+1);
	var username = getUser();
	//console.log("Sending: " + itemID + " bid: " + bid_amount);
	$.ajax({
		type : "POST",
		dataType:'json',
		url  :window.location.href + "/submit-bid",
		data :{username:username,itemID:itemID,bid_amount:bid_amount},
		success : function(data) {
			if(data != "problem-bid"){
				$("#successBidModal-Label").text("Successfull Bid")
				$("#successBidModal-text").html(data);
				$("#successBidModal").modal('show');
				$("#successBid-btn-ok").click(function(){
					window.location.reload();
				});
			} else {
				$("#errorModal-Label").text("Error on Bid")
				$("#errorModal-text").html("Sorry a problem occured with your offer.<br>Please contact the administrator");
				$("#errorModal").modal('show');
				$("#error-btn-ok").click(function(){
					window.location.reload();
				});
			}
			
			
		}
			
		
	});
}

function getItemModule(itemID) {
	
	//console.log("getting the item module ");
	$.get( window.location.href + "/details-module", function( details_module ) {
		getDetails(itemID,details_module);
		
	});
	
}

function getDetails(itemID, details_module) {
	
	//console.log("getting item details");
	$.ajax({
		type : "GET",
		dataType:'json',
		url  :window.location.href + "/details",
		data :{itemID:itemID},
		success : function(data) {
			$("#item-details").empty();
			//console.log("moving on formatting the page")
			if(data.length == 0){
				alert("No data found");
			}else {
				//console.log(data)
				
				var panel = $("<div>" + details_module + "</div>");
				panel.find('#Title').text(data.name);
				panel.find('#description-textarea').text(data.description);
				panel.find('#itemID').text(data.id);
				panel.find('#location').text(data.location);
				panel.find('#latitude').text(data.lat);
				panel.find('#longtitude').text(data.lon);
				panel.find('#allcategories').text(data.category);
				panel.find('#buyprice').text(parseFloat(data.buyprice).toFixed(2) + " $");
				panel.find('#seller').text(data.seller);
				panel.find('#firstbid').text(parseFloat(data.firstbid).toFixed(2) + " $");
				panel.find('#expire').text(data.expires);
				panel.find('#highest-bid').html(parseFloat(data.highest_bid).toFixed(2) + " $");
				panel.find('#bids-num').text(data.numOfBids);
				
				
				//console.log("buy price: " + parseFloat(data.buyprice).toFixed(2));
				
				latitude = data.lat;
				longtitude = data.lon;
				//console.log("lat: " + latitude + " --- lon: " + longtitude);
				var bidsHistory = data.bidsHistory;
					
				html = panel.html();
				$("#item-details").append(html);
				
				if(bidsHistory.length == 0) {
					//console.log("No data for history");
					$("#history-table").css("display","none");
					$("#no-history-warning").css("display","block");
				} else {
					$.each(bidsHistory, function(key,value) {
						  var bidder = value.Bidder;
						  var bidPrice = value.BidPrice;
						  var html =  "<tr><td>" + 
							bidder + "</td>" 
						+ " <td> " + parseFloat(bidPrice).toFixed(2) + " $"
						+ "</td></tr>";
						  //console.log("bidder: " + bidder + " bidPrice: " + bidPrice);
						  $("#history-table").find('tbody').append(html);
						
						});
				}
				if(data.closed == "yes"){
					$("#bid-section").css("display","none");
					$("#closed-auction-warning").css("display","block");
				} else {
					if(parseFloat(data.buyprice).toFixed(2) != 0.00){
						
						$("#buy-section").css("display","block");
						
						$("#buyNow").click(function(event){
							$("#confirm-buy-btn").css("display","block");
							$("#warningBid-text").html("Are you sure that you want to buy this item ?");
							$("#warningBidModal").modal('show');
							
							$("#confirm-buy-btn").click(function(event){
								$("#warningBidModal").modal('hide');
								buyCall();
							});
						});
					}
					
					var username = getUser();
					//console.log("username: " + username + " --- seller: " + data.seller);
					// if the user is also the seller don't let him 
					if(username == data.seller){
						
						$("#bid-section").css("display","none");
						if($("#buy-section").is(":visible")){
							$("#buy-section").css("display","none");
						}
					}
				}
				
				setListeners();
				checkForUser();
				
			}
		}	
	}); 
	
	//console.log("end of getting item details")
}

function buyCall(){
	var username = getUser();
	var url = window.location.href;
	var itemID = url.substring(url.lastIndexOf("/")+1);
	$.ajax({
		type : "POST",
		dataType:'json',
		url  :window.location.href + "/buy",
		data :{username:username,itemID:itemID},
		success : function(data) {
			if(data == "success"){
				$("#successModal-Label").text("Successfull Buy")
				$("#successModal-text").html("Your purchase was submitted!<br>The seller has been notified and" +
						" he will contact you soon.");
				$("#successModal").modal('show');
				
				$("#success-btn-ok").click(function(){
					window.location.replace(baseURL+"/user/"+username+"/auctions");
				});
			} else {
				$("#errorModal-Label").text("Problem")
				$("#errorModal-text").html("A problem occured during buy");
				$("#errorModal").modal('show');
				$("#error-btn-ok").click(function(){
					window.location.reload();
				});
			}	
		}		
	});
}


function initMap(){
	//console.log("Creating google maps")
	var center = new google.maps.LatLng(latitude,longtitude);
	var mapDiv = document.getElementById('itemMap');
	map = new google.maps.Map(mapDiv, {
	    center: center,
	    zoom: 8,
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	});
	
	//var position = new google.maps.LatLng(latitude,longtitude);
	//console.log("center: " + center)
	
	location_marker = new google.maps.Marker({
        position: center,
        map: map
    });
	location_marker.setMap(map);


	
	//console.log("end of creation")
}