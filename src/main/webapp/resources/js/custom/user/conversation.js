$(document).ready(function (){
	

	getRecipients();
	getInboxMessagesModule();
	getSentMessagesModule();
	checkForUser();
	getUnreadMessages();
	getUnreadConvMessages()
	initListeners();
	var username = getUser();
	//console.log("user: "+username);
	$("#username-upright").append(username);
	
	
	
});

var recipientTags = [];
var bodyMessageInboxHolder = {};
var bodyMessageSentHolder = {};
var inboxSize;
var sentSize;



function initListeners() {
	
	$("#recipient").click(function(event){
		event.preventDefault();
		//console.log("recipients####")
		//console.log(recipientTags);
		//$("#recipient").attr('autocomplete', 'on');
		$("#recipient").autocomplete({
	         source: recipientTags
	       });
		
	});
	
	$("a.sent-ref").click(function(event){
		event.preventDefault();
		$("#inbox-item").removeClass("active");
		$("#sent-item").addClass("active");
		/*if($("#no-inbox-alert").is(":visible")){
			console.log("here1")
			$("#no-inbox-alert").css("display","none");
			
		}*/
		if($("#compose-area").is(":visible")){
			$("#compose-area").css("display","none");
			$("#main-content-area").css("display","block");
			$("#back-to-read").css("display","none");
			$("#compose-button").css("display","block");
		}
		//$("#inbox-table").css("display","none");
		//$("#sent-table").css("display","block");
		$("#inbox-area").hide();
		
		if(sentSize == 0){
			$("#no-inbox-alert").css("display","block");
		} else {
			$("#no-inbox-alert").css("display","none");
			$("#sent-area").show();
		}
		$("#active-area").text("Sent");
		
	});
	
	$("a.inbox-ref").click(function(event){
		event.preventDefault();
		
		
		if($("#compose-area").is(":visible")){
			$("#compose-area").css("display","none");
			$("#main-content-area").css("display","block");
			$("#back-to-read").css("display","none");
			$("#compose-button").css("display","block");
		}
		
		$("#sent-area").hide();
		
		$("#sent-item").removeClass("active");
		$("#inbox-item").addClass("active");
		
		if(inboxSize == 0){
			$("#no-inbox-alert").css("display","block");
		} else {
			$("#no-inbox-alert").css("display","none");
			$("#inbox-area").show();
		}
		
		$("#active-area").text("Inbox");
	});
	
	$("#compose-button").click(function(e){
		e.preventDefault();
		$("#main-content-area").css("display","none");
		$("#compose-area").css("display","block");
		$("#compose-button").css("display","none");
		$("#back-to-read").css("display","block");
		
	});
	
	$("#back-to-read").click(function(e){
		e.preventDefault();
		$("#compose-area").css("display","none");
		$("#main-content-area").css("display","block");
		$("#back-to-read").css("display","none");
		$("#compose-button").css("display","block");
		
		
	});
	
	$("#send-message").click(function(event){
		event.preventDefault();
		var message = {};
		message["recipient"] = $("#recipient").val();
		message["subject"] = $("#subject").val();
		message["message_body"] = $("#message-body").val();
		sendMessage(message);
		
	});
	
	
	$("#discard-compose").click(function(){
		$("#message-body").val("");
		$("#subject").val("");
		$("#recipient").val("");
	});
	
	$("#reply-message").click(function(){
		
		// get the neccessary fields 
		var msgID = $("#view-inbox-msgID").val();
		var sender = $("#sender-inbox-view").val();
		var subj = $("#subject-inbox-view").val();
		var initBody = $("#view-inbox-textarea").val();
		
		$("#reply-msgID").val(msgID);
		$("#reply-msg-recipient").val(sender);
		$("#reply-subject").val(subj);
		$("#initial-message").val(initBody);
		$("#initial-message").attr('readonly','readonly');
		
		$("#view-inbox-area").fadeOut();
	
		$("#reply-area").fadeIn();
	});
	
	$("#send-reply-message").click(function(){
		var message = {};
		message["recipient"] = $("#reply-msg-recipient").val();
		message["subject"] = $("#reply-subject").val();
		message["message_body"] = $("#reply-textarea").val();
		sendMessage(message);
	});
	
  
	
}


function inboxListeners(){
	var rows = document.getElementById('inbox-table').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    for (i = 0; i < rows.length; i++) {
        rows[i].onclick = function(e) {
        	if(e.target.type == "checkbox"){
        		//console.log("checkbox clicked")
        		$("#trash-message").css("display","block");
        		
        	} else {
        		var message_id = $(this).find("#messageID").val();
        		//console.log("message_id: " + message_id)
            	var message_body = bodyMessageInboxHolder[message_id].messageBody;
            	var from = bodyMessageInboxHolder[message_id].sender;
            	var subject = bodyMessageInboxHolder[message_id].subject;
            	
            	if ( $( this ).hasClass( "unread" ) ) {
            		$(this).removeClass("unread");
            		var inbox_num = parseInt($("#inbox-counter").text());
            		if(inbox_num == 1){
            			$("#inbox-counter").css("display","none");
            			$("#header-unread-messages").css("display","none");
            			$('#notify').css("display","none");
            			
            		} else {
            			inbox_num--;
            			$("#inbox-counter").text(inbox_num);
            			$("#header-unread-messages").text(inbox_num);
            			$('#notify').text(inbox_num);
            		}
            		markAsRead(message_id);
            	}
            	$("#view-inbox-msgID").val(message_id);
            	$("#sender-inbox-view").val(from);
            	$("#subject-inbox-view").val(subject);
            	if(from == "system"){
            		$("#reply-message").css("display","none");
            	}
            	$("#view-inbox-textarea").text(message_body);
            	$("#view-inbox-area").css("display","block");
            	
        	}
        	
        	
        }
    }
    
    $("#delete-msginbox-area").click(function(){
    	var toDelete = [];
    	var msgID = $("#view-inbox-msgID").val();
    	toDelete.push(msgID);
    	deleteMessage(toDelete);
    });
    
    
    $("#trash-message").click(function(event){
    	var toDelete = [];
    	 $('#inbox-table').find('tr').each(function () {
    	        var row = $(this);
    	        if (row.find('input[type="checkbox"]').is(':checked')) {
    	        	var m_id = row.find("#messageID").val();
    	        	toDelete.push(m_id);
    	        }
    	    });
    	 //alert(toDelete);
    	// deleteMessage(JSON.stringify(toDelete));
    	 if(toDelete.length == 0){
    		 alert("Please choose an email to delete")
    	 } else {
    		 deleteMessage(toDelete);
    	 }
    	 
    	
    });
    
    $("#hide-inbox-message").click(function(event){
		
		$("#view-inbox-area").css("display","none");
	});
    
    
    
    $("#reply-message").click(function(event){
    	
    });
}


function sentListeners() {
	var rows = document.getElementById('sent-table').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    for (i = 0; i < rows.length; i++) {
        rows[i].onclick = function(e) {
        	
        	if(e.target.type == "checkbox"){
        		$("#trash-message").css("display","block");
        		//console.log("checkbox clicked")
        	} else {
        		var message_id = $(this).find("#messageID").val();
            	var message_body = bodyMessageSentHolder[message_id].messageBody;
            	var to = bodyMessageSentHolder[message_id].recipient;
            	var subject = bodyMessageSentHolder[message_id].subject;
            	$("#view-sent-msgID").val(message_id);
            	$("#view-sent-textarea").text(message_body);
            	$("#recipient-sent-view").val(to);
            	$("#subject-sent-view").val(subject);
            	$("#view-sent-area").css("display","block");
        	}
        	
        }
    }
    
    $("#delete-msgsent-area").click(function(){
    	var toDelete = [];
    	var msgID = $("#view-sent-msgID").val();
    	toDelete.push(msgID);
    	 deleteMessage(toDelete);
    });
    
    $("#trash-message").click(function(event){
    	var toDelete = [];
    	 $('#sent-table').find('tr').each(function () {
    	        var row = $(this);
    	        if (row.find('input[type="checkbox"]').is(':checked')) {
    	        	var m_id = row.find("#messageID").val();
    	        	toDelete.push(m_id);
    	        }
    	    });
    	 //alert(toDelete);
    	// deleteMessage(JSON.stringify(toDelete));
    	 if(toDelete.length == 0){
    		 alert("Please choose an email to delete")
    	 } else {
    		 deleteMessage(toDelete);
    	 }
    	 
    	
    });
    
    $("#hide-sent-message").click(function(event){
		
		$("#view-sent-area").css("display","none");
	});
}

function markAsRead(message_id){
	$.ajax({
		type : "GET",
		dataType:'json',
		data: {messageID:message_id},
		url  : window.location.href + "/markAsRead",
		success:function(data){
			//console.log(data);
			//console.log("message with id: " + message_id + " marked as read");
		}
		
	});
}

function getUnreadConvMessages(){
	var username = getUser();
	$.ajax({
		type : "GET",
		dataType:'json',
		data: {username:username},
		url  : window.location.href + "/unread-number",
		success:function(unread){
			//console.log("unread: " + unread);
			if(unread != "0") {
				$("#inbox-counter").css("display","block");
				$("#inbox-counter").text(unread);
			}
		}
		
	});
}

function getRecipients() {
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : window.location.href + "/recipients",
		success:function(recipients){
			//console.log(recipients);
			recipientTags = recipients;
		}
		
	});
	
}

function sendMessage(data){
	var username = getUser();
	var message = JSON.stringify(data);
	$.ajax({
		type : "POST",
		dataType:'json',
		data: {username:username,message:message},
		url  : window.location.href + "/message",
		success:function(result){
			$("#info-text").append("Your message to" + data["recipient"] + "was sent");
			$("#info-modal").modal('show');
			window.location.reload();
		}
		
	});
	
}


function getInboxMessagesModule(){
	//console.log("module get...")

	$.get( window.location.href+"/inbox-module", function( inboxModule ) {
		getInboxMessages(inboxModule);
	});
	//console.log("module get end...")
}

function getSentMessagesModule(){

	$.get( window.location.href+"/sent-module", function( sentModule ) {
		getSentMessages(sentModule);
	});
}



function getInboxMessages(data) {
	//console.log("getting inbox messages")
	var inboxModule = data;
	var username = getUser();
	$.ajax({
		type : "GET",
		url  : window.location.href + "/inbox",
		dataType:'json',
		data: {username:username},
		success:function(inbox){
			inboxSize = inbox.length;
			if(inbox.length == 0) {
				$("#inbox-area").css("display","none");
				$("#no-inbox-alert").css("display","block");
				$("#no-inbox-text").text("You have no messages");
			}	
			else{
				console.log(inbox);
				for(var i=0; i<inbox.length; i++){
					//var body = null;
					bodyMessageInboxHolder[inbox[i].messageID] = inbox[i];
					
					
					var body = $("<tr>" + inboxModule + "</tr>");
					body.find("#messageID").val(inbox[i].messageID);
					body.find("#sender-inbox").text(inbox[i].sender);
					body.find("#subject-inbox").text(inbox[i].subject);
					body.find("#datetime-inbox").text(inbox[i].dateCreated);
					var html = body.html();
					if(inbox[i].isRead == 0  ) {
						$("#inbox-table").find('tbody').append('<tr class="unread">' + html + '</tr>');
					} else {
						$("#inbox-table").find('tbody').append('<tr>' + html + '</tr>');
					}
					
				}
				inboxListeners();
			}
			//console.log("end of getting inbox messages")
			
		}
		
	});
}


function getSentMessages(sentModule) {
	//console.log("getting sent messages")
	
	var username = getUser();
	$.ajax({
		type : "GET",
		url  : window.location.href + "/sent",
		dataType:'json',
		data: {username:username},
		success:function(sent){
			sentSize = sent.length;
			if(sent.length == 0) {
				/*if(!$("#no-inbox-alert").is(":visible")){
					$("#sent-table").css("display","none");
					$("#no-sent-alert").css("display","block");
					$("#no-sent-text").text("You have no sent messages");
				}*/
				
				
			}	
			else{
				//console.log(sent);
				for(var i=0; i<sent.length; i++){
					
					bodyMessageSentHolder[sent[i].messageID] = sent[i];
					
					var body = $("<tr>" + sentModule + "</tr>");
					
					body.find("#messageID").val(sent[i].messageID);
					body.find("#recipient-sent").text(sent[i].recipient);
					body.find("#subject-sent").text(sent[i].subject);
					body.find("#datetime-sent").text(sent[i].dateCreated);
					var html = body.html();
					//console.log(html);
					$("#sent-table").find('tbody').append("<tr>"+html+"</tr>");
				}
				sentListeners();
			}
			console.log("end of getting sent messages")
			
		}
		
	});
}


function deleteMessage(messages){
	var username = getUser();
	jmessages = JSON.stringify(messages);
	console.log("url: " + window.location.href);
	console.log(messages);
	$.ajax({
		type : "POST",
		url  : window.location.href + "/delete-messages",
		data : {username:username,messages:jmessages},
		dataType:'json',
		success:function(data){
			//console.log(data);
			window.location.reload();
			
		}
		
	});
}

