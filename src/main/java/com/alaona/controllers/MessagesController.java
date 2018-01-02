package com.alaona.controllers;

import java.util.List;

import com.alaona.entities.users.messages.Conversation;
import com.alaona.services.UserServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.alaona.entities.users.Registereduser;
import com.alaona.services.ConversationServices;


/*
 * Controller for handling the messages beetween the registered users
 */

@Controller
@RequestMapping(value={"/user/{username}/conversation"})
public class MessagesController {
	
	@Autowired
	ConversationServices conversationServices;
	
	@Autowired
    UserServices userServices;
	
	/*
	 * Returns the module where the inbox messages are shown
	 */
	@RequestMapping(value = "/inbox-module",method = RequestMethod.GET)
	public String getInboxModule(){
		//System.out.println("getting inbox module");
		// Getting the inbox module
		return "/pages/modules/inboxMessagesModule.html";
	}
	
	/*
	 * Returns the module where the sent messages are shown
	 */
	@RequestMapping(value = "/sent-module",method = RequestMethod.GET)
	public String getSentModule(){
		//System.out.println("getting sent module");
		// Getting the sent module
		return "/pages/modules/sentMessagesModule.html";
	}
	
	/* 
	 * Return the number of the new messages for the user
	 */
	@RequestMapping(value = "/unread-number",method = RequestMethod.GET)
	@ResponseBody
	public String unread_number(@RequestParam("username") String username){
		
		int num = conversationServices.countNewMessages(username);
		if(num > 0) {
			return new Gson().toJson(num);
		}
		return new Gson().toJson("0");
	}
	
	
	/*
	 * Returns the possible recipients in order to populate the autocomplete in ui
	 */
	@RequestMapping(value = "/recipients",method = RequestMethod.GET)
	@ResponseBody
	public String getRecipients(){
				
		List<Registereduser> reg_users = userServices.getRecipients();
		JSONArray recipients = new JSONArray();
		//System.out.println(reg_users);
		if(reg_users.size() != 0){
			for(Registereduser r : reg_users){
				recipients.put(r.getUsername());
			}
			
			return recipients.toString();
		}
		
		return new Gson().toJson("problem-recipients");
	}
	
	/*
	 * Returns a list of inbox messages for the given user
	 */
	@RequestMapping(value = "/inbox",method = RequestMethod.GET)
	@ResponseBody
	public String getInbox(@RequestParam("username") String username){
		//System.out.println("getting inbox messages");
		
		List<Conversation> messages = conversationServices.getInboxMessages(username);
		JSONArray inbox = new JSONArray();
		for(Conversation c : messages){
			JSONObject message = new JSONObject();
			try{
				message.put("messageID", c.getId().getConversationID());
				message.put("sender", c.getSender());
				message.put("recipient", c.getRecipient().getUsername());
				message.put("subject", c.getSubject());
				message.put("dateCreated", c.getDateCreated());
				message.put("isRead", c.getIsRead());
				message.put("messageBody", c.getMessageText());
				
			} catch(JSONException e){
				e.printStackTrace();
			}
			inbox.put(message);
		}
		
		//System.out.println(inbox.toString());
		return inbox.toString();
	}
	
	/*
	 * Returns a list of sent messages for the given user
	 */
	@RequestMapping(value = "/sent",method = RequestMethod.GET)
	@ResponseBody
	public String getSent(@RequestParam("username") String username){
		//System.out.println("getting sent messages");
		
		List<Conversation> messages = conversationServices.getSentMessages(username);
	
		JSONArray sent = new JSONArray();
		for(Conversation c : messages){
			System.out.println("message: " + c.getSubject());
			JSONObject message = new JSONObject();
			try{
				message.put("messageID", c.getId().getConversationID());
				message.put("sender", c.getSender());
				message.put("recipient", c.getRecipient().getUsername());
				message.put("subject", c.getSubject());
				message.put("dateCreated", c.getDateCreated());
				message.put("isRead", c.getIsRead());
				message.put("messageBody", c.getMessageText());
				
			} catch(JSONException e){
				e.printStackTrace();
			}
			sent.put(message);
		}
		
		//System.out.println(sent.toString());
		return sent.toString();
	}
	
	/*
	 * Calls the service for submitting the message
	 */
	@RequestMapping(value = "/message",method = RequestMethod.POST)
	@ResponseBody
	public String submitMessage(@RequestParam("username") String username, 
			@RequestParam("message") String message){
		
		JSONObject message_json=null;
		String sender,recipient,subject,message_body;
		try {
			message_json = new JSONObject(message);
			sender = username.toString();
			recipient = message_json.get("recipient").toString();
			subject = message_json.get("subject").toString();
			message_body = message_json.get("message_body").toString();
			//System.out.println("Sender: " + sender + " ---- " + "recipient: " + recipient);
			if(conversationServices.submitMessage(sender, recipient, subject, message_body) == 0){
				return new Gson().toJson("Your message to " + recipient + " was sent");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new Gson().toJson("Your message was not sent");
	}
	
	
	/*
	 * Marks a message as read give the MessageID
	 */
	@RequestMapping(value = "/markAsRead",method = RequestMethod.GET)
	@ResponseBody
	public String markAsRead(@RequestParam("messageID") String messageID){
		
		if(conversationServices.markAsRead(Integer.parseInt(messageID)) == 0){
			return new Gson().toJson("Marked as Read");
		}
		return new Gson().toJson("Cannot mark as read");
	}
	
	/*
	 * Returns ok if the messages were deleted 
	 */
	@RequestMapping(value = "/delete-messages",method = RequestMethod.POST)
	@ResponseBody
	public String deleteMessage(@RequestParam("username") String username,
			@RequestParam("messages") String messages){
		//System.out.println("Deleting Messages....");
		try{
			JSONArray jarray = new JSONArray(messages);
			for(int i=0; i<jarray.length();i++){
				int m_id = Integer.parseInt(jarray.get(i).toString());
				//System.out.println("m_id to delete: " + m_id);
				if(conversationServices.deleteMessage(username, m_id) != 0){
					//return new Gson().toJson("A problem occured");
					System.out.println("Cannot delete message with id: " + m_id);
				}
				
			}
		} catch(JSONException e){
			System.out.println("JSON parse problem in deleteMessage");
		}
	
		return new Gson().toJson("ok");
	}
	

}
