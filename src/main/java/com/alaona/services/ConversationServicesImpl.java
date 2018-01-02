package com.alaona.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alaona.dao.QueryAuction;
import com.alaona.dao.QueryConversation;
import com.alaona.dao.QueryUser;
import com.alaona.entities.auctions.Auction;
import com.alaona.entities.users.messages.Conversation;
import com.alaona.entities.users.messages.ConversationPK;
import org.springframework.beans.factory.annotation.Autowired;

public class ConversationServicesImpl implements ConversationServices{

	@Autowired
    QueryConversation queryConversation;
	
	@Autowired
    QueryUser queryUser;
	
	@Autowired
    QueryAuction queryAuction;
	
	@Override
	public List<Conversation> getInboxMessages(String username) {
		
		return queryConversation.getInboxMessages(username);
	}

	@Override
	public List<Conversation> getSentMessages(String username) {
		
		return queryConversation.getSentMessages(username);
	}

	@Override
	public int countNewMessages(String username) {
		
		return queryConversation.countNewMessages(username);
	}

	@Override
	public int markAsRead(int messageID) {
		
		return queryConversation.markAsRead(messageID);
	}

	@Override
	public int submitMessage(String sender, String recipient, String subject, String message_body) {
		
		int conversationID = queryConversation.getMaxConversationID();
		
		//System.out.println("Message body: " + message_body);
		/*
		 * Submit message method:
		 * 1) Create an instance for the conversation embedded key
		 * 2) Create an instance for the conversation and add the embedded key reference
		 * 3) Set the neccessary fields
		 * 3) Call the dao class for persisting the object
		 */
		ConversationPK convpk = new ConversationPK();
		convpk.setConversationID(conversationID);
		convpk.setRecipient(recipient);
		
		
		Conversation conv = new Conversation();
		conv.setId(convpk);
		conv.setRecipient(queryUser.getUser(recipient).getRegistereduser());
		conv.setSender(sender);
		
		Calendar calendar = Calendar.getInstance();
        Date date =  calendar.getTime();
		conv.setDateCreated(date);
		
		conv.setIsRead(Byte.parseByte("0".toString()));
		conv.setSubject(subject);
		conv.setMessageText(message_body);
		
		
		if(queryConversation.submitMessage(conv) == 0){
			System.out.println("The message was submitted, messageID: " + conversationID);
			conversationID++;
			return 0;
		}
		return -1;
		
	}

	@Override
	public int deleteMessage(String username, int messageID) {
		if(queryConversation.deleteMessage(username, messageID) == 0){
			return 0;
		}
		return 1;
	}

	@Override
	public List<Object[]> inbox(String username) {
		
		return queryConversation.inbox(username);
	}

	@Override
	public List<Object[]> sent(String username) {
		
		return queryConversation.sent(username);
	}

	@Override
	public int notifyUser(int itemID, String buyer) {
		Auction auction = queryAuction.getDetails(itemID);
		String seller = auction.getRegistereduser().getUsername();
	
		String subject = "Item bought";
		String messageBody = "Your product with id: " + itemID + " has been purchased from " + buyer +
				 "\nDo not reply to this message!!";
		
		if(submitMessage("system", seller, subject, messageBody) == 0){
			return 0;
		}
		return 1;
	}

}
