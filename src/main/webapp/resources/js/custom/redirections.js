/*
 * This file holds all the redirections services 
 * 
 */
var baseURL; // holds the base url of the website
$(document).ready(function(){
	
	//First choice for local host, Second choice for remote Server
	baseURL = window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split('/')[1];
	//baseURL = window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split('/')[1] + "/auctionbay";
	 
	$("a.login-link").on("click",function(event){
    	
		//console.log("login btn");
		event.preventDefault();
		window.location = baseURL+"/login-signup";
    	
   });
	
	$("a.profile-link").on("click",function(event){
    	
		//console.log("login btn");
		event.preventDefault();
		var patharray = window.location.pathname.split( '/' );
		var location = baseURL + "/" + patharray[2] + "/" + patharray[3] + "/profile";
		window.location = location;
		
    	
   });
	
	$("a.view-auctions-ref").on("click",function(event){

		event.preventDefault();
		//console.log("window.location.href: " + window.location.href)
		//console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var patharray = window.location.pathname.split( '/' );
		if(patharray.length >= 4) {
			//console.log("path: " + patharray)
			location = baseURL + "/" + patharray[2] + "/" + patharray[3] + "/auctions";
			window.location = location;
		} else {
			window.location = baseURL + "/auctions";
		}
		var lastChar = location.substr(location.length - 1);
		
    	
   });
	
	$("a.rates-link").on("click",function(event){
		event.preventDefault();
		//console.log("window.location.href: " + window.location.href)
		//console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var patharray = window.location.pathname.split( '/' );
		if(patharray.length >= 4) {
			//console.log("path: " + patharray)
			location = baseURL + "/" + patharray[2] + "/" + patharray[3] + "/rates";
			window.location = location;
		} else {
			window.location = baseURL + "/rates";
		}
		
	});
	
	/*$("a.rates-ref").on("click",function(event){
		event.preventDefault();
		console.log("window.location.href: " + window.location.href)
		console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var patharray = window.location.pathname.split( '/' );
		if(patharray.length >= 4) {
			console.log("path: " + patharray)
			location = baseURL + "/" + patharray[2] + "/" + patharray[3] + "/rates";
			window.location = location;
		} else {
			window.location = baseURL + "/rates";
		}
		
	});*/
	
	$("a.messages-link").on("click",function(event){
		event.preventDefault();
		//console.log("window.location.href: " + window.location.href)
		//console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var patharray = window.location.pathname.split( '/' );
		if(patharray.length >= 4) {
			//console.log("path: " + patharray)
			location = baseURL + "/" + patharray[2] + "/" + patharray[3] + "/conversation";
			window.location = location;
		} else {
			window.location = baseURL + "/conversation";
		}
	});
	
	
	$("a.create-auction-ref").on("click",function(event){

		event.preventDefault();
		//console.log("window.location.href: " + window.location.href)
		//console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var patharray = window.location.pathname.split( '/' );
		if(patharray.length >= 4) {
			//console.log("path: " + patharray)
			location = baseURL + "/" + patharray[2] + "/" + patharray[3] + "/manage-auctions";
			window.location = location;
		} else {
			window.location = baseURL + "/manage-auctions";
		}
		var lastChar = location.substr(location.length - 1);
		
    	
   });
	
	$("a.contact-ref").on("click",function(event){
    	
		//console.log("contact btn");
		event.preventDefault();
		window.location = baseURL+"/contact";
    	
   });
	
	$("a.index-link").click(function(event){
		//console.log("home btn");
		event.preventDefault();
		var result = checkUser();
		if(result == 0){
			document.location.href=baseURL;
		} else {
			var user = getUser();
			window.location = baseURL + "/user/"+user; 
		}
	});
	
	$("a.login-panel-link").click(function(event){
		event.preventDefault();
		window.location = baseURL+"/login-signup";		
	});
	
	$('a.contact-link').click(function(event){
		event.preventDefault();
		//console.log("window.location.href: " + window.location.href)
		//console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var patharray = window.location.pathname.split( '/' );
		if(patharray.length >= 4) {
			//console.log("path: " + patharray)
			location = baseURL + "/" + patharray[2] + "/" + patharray[3] + "/contact";
			window.location = location;
		} else {
			window.location = baseURL + "/contact";
		}
	})
	
	$('a.contact-panel-link').click(function(event){
		
		event.preventDefault();
		var result = checkUser();
		if(result == 0){
			window.location = baseURL+"/contact"
		} else {
			var user = getUser();
			window.location = baseURL + "/user/"+user+"/contact"; 
		}
		
	});
	
	$('a.mailbox-panel-link').click(function(event){
		
		event.preventDefault();
		//console.log("window.location.href: " + window.location.href)
		//console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var lastChar = location.substr(location.length - 1);
		if(lastChar == "/"){
			window.location = window.location.href+"conversation";
		} else {
			window.location = window.location.href+"/conversation";
		}
			
		
	});
	
	
	$('a.view-auctions-panel-link').click(function(event){
		
		event.preventDefault();
		//console.log("window.location.href: " + window.location.href)
		//console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var lastChar = location.substr(location.length - 1);
		if(lastChar == "/"){
			window.location = window.location.href+"auctions";
		} else {
			window.location = window.location.href+"/auctions";
		}
			
		
	});
	
	$('a.manage-panel-link').click(function(event){
		event.preventDefault();
		window.location = window.location.href+"/manage-auctions";
		
	});
	
	
	$('a.log-out').click(function(event){
		event.preventDefault();
		window.location = baseURL;
	})
	
	function checkUser(){
		var user="user";
		var urlpath = window.location.href;

		if(urlpath.indexOf(user) == -1){
			/* if not found then it return -1 
			 then it is a guest 
			  */
			return 0;
		} else {
			// it is registered user
			return 1;
		}
	}
	
});