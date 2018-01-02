package com.alaona.services;

import java.util.List;

import com.alaona.entities.auctions.Auction;
import com.alaona.entities.users.Pendinguser;
import org.json.JSONArray;

import com.alaona.entities.users.Registereduser;
import com.alaona.entities.users.User;

public interface UserServices {

	/*
	 * Service for registering a user
	 */
	public void userRegistration(String username, String password, String firstname
			, String lastname, String email, String trn, String phonenumber, 
			String city, String street, String region, String zipcode);
	
	/*
	 * Gets max address ID
	 */
	public void getAddressID();
	
	/*
	 * Service for the login of the user
	 */
	public int Login(String username, String password);
	
	/*
	 * Checks whether the user already exists
	 */
	public boolean userExists(String username);
	
	/*
	 * Returns an instance of the user
	 */
	public User getUser(String username);
	
	/*
	 * Service for accepting the pending user
	 * It is being triggered from the administrator
	 */
	public void accept_user(String username);
	
	/*
	 * Returns the number of registered users
	 */
	public int count_registered();
	
	/*
	 * Returns the number of pending users
	 */
	public int count_pending();
	
	/*
	 * Returns a list of pending users with pagination
	 */
	public List<Pendinguser>  getPendingUsers(int startpage, int pagesize);
	
	/*
	 * Returns a list of registered users with pagination
	 */
	public List<Registereduser> getGroupsOfUsers(int startpage, int pagesize);
	
	/*
	 * Returns a list of possible recipients
	 * For the conversation...
	 */
	public List<Registereduser> getRecipients();
	
	/*
	 * Returns the number of user auctions 
	 * Type parameter indicates whether the auctions that are being checked
	 * are active, expired or all
	 */
	public int count_user_auctions(String username,String type);
	
	/*
	 * Returns a list of user auctions 
	 * Type parameter indicates whether the auctions that are being checked
	 * are active, expired or all
	 */
	public List<Auction> get_user_auctions(String username, int startpage, int pagesize, String type);
	
	/*
	 * Submits rating. A seller can have a different rating as a user an as  a bidder
	 */
	public void submitRating(JSONArray data);
	
	/*
	 * Returns a list of the bids of the user with pagination enabled
	 */
	public List<Object[]> getUserBids(String username, int startpage, int endpage);
	
	/*
	 * Returns the number of user bids
	 */
	public int getUserBidsNum(String username);
}
