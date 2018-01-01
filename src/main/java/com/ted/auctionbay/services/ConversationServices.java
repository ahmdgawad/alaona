package com.ted.auctionbay.services;

import java.util.List;

import com.ted.auctionbay.entities.users.messages.Conversation;

public interface ConversationServices {

	/*
	 * Gets the inbox messages
	 */
	public List<Conversation> getInboxMessages(String username);
	
	/*
	 * Gets the sent messages
	 */
	public List<Conversation> getSentMessages(String username);
	
	/*
	 * Gets the number of the new messages
	 */
	public int countNewMessages(String username);
	
	/*
	 * Submits the message and persists a new record
	 */
	public int submitMessage(String sender, String recipient, String subject, String message_body);
	
	/*
	 * Marks the message with the given id as read
	 */
	public int markAsRead(int messageID);
	
	/*
	 * Deletes the message specified
	 */
	public int deleteMessage(String username, int messageID);
	
	/*
	 * Notifies buyer when his purchase was successful
	 * It sends him a message with information about his purchase
	 */
	public int notifyUser(int itemID, String buyer);
	
	/*
	 * The two following function is another way of getting inbox and sent messages
	 */
	public List<Object[]> inbox(String username);
	
	public List<Object[]> sent(String username);
}
