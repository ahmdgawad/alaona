package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.users.messages.Conversation;

public interface QueryConversation {
	
	/*
	 * Return inbox messages of given user
	 */
	public List<Conversation> getInboxMessages(String username);
	
	/*
	 * Return sent messages of given user
	 */
	public List<Conversation> getSentMessages(String username);
	
	/*
	 * Return number of new messages of given user
	 */
	public int countNewMessages(String username);
	
	//Mark given message as read
	public int markAsRead(int messageID);
	
	/*
	 * Return max id of conversations
	 */
	public int getMaxConversationID();
	
	/*
	 * Return max id of messages
	 */
	public int getMaxMessageID();
	
	/*
	 * Insert message to database
	 */
	public int submitMessage(Conversation conversation);
	
	/*
	 * Delete message from database
	 */
	public int deleteMessage(String username, int messageID);
	
	/*
	 * Return inbox messages of given user with another way
	 */
	public List<Object[]> inbox(String username);
	
	/*
	 * Return inbox messages of given user with another way
	 */
	public List<Object[]> sent(String username);
}
