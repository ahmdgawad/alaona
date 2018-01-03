
$(document).ready(function () {
    initialize();
});

/* Global variables */
var pendingusers_matrix = [];
var pending_table, registered_table, auctions_table;


function initialize(){
	collapse();
    getNumberOfUsers();
    createGrids();
    setListeners();
	
}


function collapse(){
	   $(window).bind("load resize", function () {
           if ($(this).width() < 768) {
               $('div.sidebar-collapse').addClass('collapse')
           } else {
               $('div.sidebar-collapse').removeClass('collapse')
           }
       });
}



function getNumberOfUsers(){
	//console.log("getting the num of users");
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : "/administrator/count-users",
		success : function(data) {
			$("#pending-number").text(data.pending_users);
			$("#registered-number").text(data.registered_users);
		}
		
	});  
	
}



function setListeners(){
	
	$("a.navbar-brand").click(function(event){
		event.preventDefault();
		document.location.href="/administrator";
	});
	
	$("a.dashboard").click(function(event){
		event.preventDefault();
		document.location.href="/administrator";
	});
	
	$("a.admin-logout").click(function(event){
		//console.log("logout from admin");
		event.preventDefault();
		
		document.location.href="/";
		
		});
	
	$('a.export-link').click(function(event){
		//console.log("export clicked");
		event.preventDefault();
		document.getElementById('registered-users').style.display = "none";
		document.getElementById('pending-users').style.display = "none";
		document.getElementById('export-options').style.display = "block";
		
		
	} );
	
	$('a.export-nav').click(function(event){
		//console.log("export clicked");
		event.preventDefault();
		document.getElementById('registered-users').style.display = "none";
		document.getElementById('pending-users').style.display = "none";
		document.getElementById('export-options').style.display = "block";
		
		
	} );
	
	$('a.pen-nav').click(function(event){
		//console.log("export clicked");
		event.preventDefault();
		document.getElementById('registered-users').style.display = "none";
		document.getElementById('export-options').style.display = "none";
		document.getElementById('pending-users').style.display = "block";

	} );
	
	$('a.reg-nav').click(function(event){
		//console.log("export clicked");
		event.preventDefault();	
		document.getElementById('export-options').style.display = "none";
		document.getElementById('pending-users').style.display = "none";
		document.getElementById('registered-users').style.display = "block";

	} );
	
	
	
	$('#export-all-btn').click(function(event){
		//console.log("export all clicked");
		event.preventDefault();
		ExportAll();
	} );
	
	$("#pending-users-link").click(function(event){
		//console.log("hi pen users");
		event.preventDefault();
		document.getElementById('registered-users').style.display = "none";
		document.getElementById('export-options').style.display = "none";
		document.getElementById('pending-users').style.display = "block";
	});
	
	$("#registered-users-link").click(function(event){
		//console.log("hi reg users");
		event.preventDefault();
		document.getElementById('export-options').style.display = "none";
		document.getElementById('pending-users').style.display = "none";
		document.getElementById('registered-users').style.display = "block";

		
	});
	

	$('#registered-users-grid tbody').on( 'click', 'tr', function () {
		//console.log("i am clicked");
	    console.log( registered_table.row( this ).data() );
	} );
	
	
	$('#pending-users-grid tbody').on('click', 'button.accept-button', function () {
		var tr = $(this).parents('tr');
		var row = pending_table.row(tr);
		pendingusers_matrix.push(row.data()[0]);
        
        //console.log(row.data()[0]);
        var username = row.data()[0];
        accept_user(username);
        //console.log('ACCEPTED USER');
		
    } );
	
	$('#auctions-grid tbody').on('click', 'button.export-button', function () {
		var tr = $(this).parents('tr');
		var row = auctions_table.row(tr);
		
        
        //console.log("auctionID: " + row.data()[0] + " and Name: " + row.data()[1]);
        var auctionID = row.data()[0];
        var itemID = row.data()[1];
        exportAuction(itemID);
    } );
	
}


function createGrids(){
	
	pending_table = $('#pending-users-grid').DataTable( {
		"processing": true,
	    "serverSide": true,
	    ajax:"/administrator/pending-users",
	    
        columns: [
            { title: "Username" },
            { title: "Firstname" },
            { title: "Lastname" },
            { title: "Email" },
            { title: "Phone number" },
            { title: "TRN" },
            { title: "City" },
            { title: "Region" },
            { title: "Street" },
            { title: "Zip Code" },
            {
                 title: "Accept user",
                "orderable":      false,
                "data":           null,
                "defaultContent": '<button type=\"button\" id=\"accept-button\" class=\"btn btn-primary btn-sm accept-button\">Accept</button>'
            }
        ],
        "columnDefs": [
                      
                       {"className": "dt-center", "targets": "_all"}
          ]          
    	});
	
	
	registered_table = $('#registered-users-grid').DataTable( {
		"processing": true,
	    "serverSide": true,
	    ajax:"/administrator/registered-users",
	    
        columns: [
            { title: "Username" },
            { title: "Firstname" },
            { title: "Lastname" },
            { title: "Email" },
            { title: "Phone number" },
            { title: "TRN" },
            { title: "City" },
            { title: "Region" },
            { title: "Street" },
            { title: "Zip Code" }    
            
        ] 
    	});
	
	auctions_table = $('#auctions-grid').DataTable( {
		"processing": true,
	    "serverSide": true,
	    ajax:"/administrator/auctions-to-export",
	    
        columns: [
            { title: "auctionID" },
            { title: "itemID" },
            { title: "Title" },
            { title: "Seller" },
            {
                title: "Export",
               "orderable":      false,
               "data":           null,
               "defaultContent": '<button type=\"button\" id=\"export-button\" class=\"btn btn-primary btn-sm export-button\"><span class=\"glyphicon glyphicon-export\"></span></button>'
           }    
            
        ],
        "columnDefs": [
                       {
                           "targets": [ 0 ],
                           "visible": false,
                           "searchable": false
                       },
                       {
                           "targets": [ 1 ],
                           "visible": false,
                           "searchable": false
                       },
                       
                       {"className": "dt-center", "targets": "_all"}
          ]           
	});
	
}


function accept_user(username){
	$.ajax({
		type : "GET",
		data : {
			username : username
		},
		datatype: 'json',
		url  : "/administrator/accept-user",
		success : function(response) {	
			if(response == "accepted"){
				$("#infopen-text").html("User: <strong>" + username + "</strong> was accepted!");
				$('#InfoPenModal').modal('show');
				$("#infopen-btn").click(function(){
					getNumberOfUsers();
					pending_table.ajax.reload();
					$('#InfoPenModal').modal('hide');
				});
				
			}
			
		}
		
	});
}

function ExportAll () {
	$("#loading-image").fadeIn(3000);
	$.ajax({
		type : "POST",
		datatype: 'json',
		url  : "/administrator/export-all-to-xml",
		success : function(response) {
			if(response == "Success"){
				$("#loading-image").fadeOut(500);
				$("#info-text").text("All auctions have been exported to AuctionBayXML folder");
				$('#InfoModal').modal('show');
				
			} else {
				$("#loading-image").fadeOut(500);
				$("#info-text").text("An error occurred during export");
				$('#InfoModal').modal('show');
			}
			
		}
		
	});
}

function exportAuction(itemID) {
	//$("#loading-image").css("display","block");
	$("#loading-image").fadeIn(3000);
	$.ajax({
		type : "POST",
		datatype: 'json',
		url  : "/administrator/export-to-xml",
		data : {itemID:itemID},
		success : function(response) {	
			//$("#loading-image").css("display","none");
			$("#loading-image").fadeOut(500);
			$("#info-text").text(response);
			$('#InfoModal').modal('show');
			
		}
		
	});
}
