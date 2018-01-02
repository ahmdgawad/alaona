package com.alaona.dao;

import java.util.List;

import com.alaona.entities.auctions.Auction;
import com.alaona.entities.users.Bidderrating;
import com.alaona.entities.users.Pendinguser;
import com.alaona.entities.users.Registereduser;
import com.alaona.entities.users.RegistereduserBidsinAuction;
import com.alaona.entities.users.Sellerrating;
import com.alaona.entities.users.User;

public interface QueryUser {

	/*
	 * Return true if user exists in database
	 */
	public boolean userExists(String username);
	
	/*
	 * Return true if user is in pending user table
	 */
	public boolean fetchPendingByUsername(String username);
	
	/*
	 * Return object of given user
	 */
	public User getUser(String username);
	
	/*
	 * Return number of registered users
	 */
	public int count_registered();
	
	/*
	 * Return number of pending users
	 */
	public int count_pending();
	
	/*
	 * Return true if given user with given password is in the database
	 */
	public boolean user_validator(String username, String password);
	
	/*
	 * Return list of pending users with use of pagination
	 */
	public List<Pendinguser>  getPendingUsers(int startpage, int pagesize);
	
	//Return max id of address
	public int getAddressMaxID();
	
	/*
	 * Return list of all registered users with use of pagination
	 */
	public List<Registereduser> getGroupsOfUsers(int startpage, int pagesize);
	
	/*
	 * Return list of all registered users 
	 */
	public List<Registereduser> getRecipients();
	
	/*
	 * Return number of all registered users
	 */
	public int registeredNumber();
	
	/*
	 * Delete user from pending users' table in database
	 */
	public void accept_user(String username);
	
	/*
	 * Insert user to registered users' table in database
	 */
	public int registerUser(User ruser);
	
	/*
	 * Return number of given user's auctions
	 */
	public int count_all_user_auctions(String username);
	
	/*
	 * Return number of given user's auctions, where end time > current
	 */
	public int count_active_user_auctions(String username);
	
	/*
	 * Return number of given user's auctions, where end time < current and aucion has at least 1 bid
	 */
	public int countClosedAuctions(String username);
	
	/*
	 * Return number of given user's auctions, where end time < current and aucion has no bid
	 */
	public int count_expired_user_auctions(String username);
	
	/*
	 * Return list of user's auction with use of pagination
	 */
	public List<Auction> get_all_user_auctions(String username,int startpage, int pagesize);
	
	/*
	 * Return list of user's auction with use of pagination, where end time > current
	 */
	public List<Auction> get_active_user_auctions(String username,int startpage, int pagesize);
	
	/*
	 * Return list of user's auction with use of pagination, where end time < current and aucion has no bid
	 */
	public List<Auction> get_expired_user_auctions(String username,int startpage, int pagesize);
	
	/*
	 * Delete given bidder from given auction
	 */
	public void deleteBidderFromAuction(String username, int auctionID);
	
	/*
	 * Insert bid of given user to given auction
	 */
	public int createBidInUser(RegistereduserBidsinAuction rba);
	
	/*
	 * Insert given user and given item to history table
	 */
	public int appendBuyerHistory(String username, int itemID);
	
	/*
	 * Return max id of bidder's rating
	 */
	public int maxBidderRatingID();
	
	/*
	 * Return max id of seller's rating
	 */
	public int maxSellerRatingID();
	
	/*
	 * Insert given bidder rating
	 */
	public void submitBidderRating(Bidderrating bidder_rate);
	
	/*
	 * Insert given seller rating
	 */
	public void submitSellerRating(Sellerrating seller_rate);
	
	/*
	 * Return list of all users
	 */
	public List<User> getUsers();
	
	/*
	 * Return list of all bidder by average rate
	 */
	public List<String> getBiddersbyRate();
	
	/*
	 * Return list of all seller by average rate
	 */
	public List<String> getSellersbyRate();
	
	/*
	 * Return user's bid with use of pagination
	 */
	public List<Object[]> getUserBids(String username, int startpage, int endpage);
	
	/*
	 * Return number of user's bids
	 */
	public int getUserBidsNum(String username);
	
}
