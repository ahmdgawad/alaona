package com.alaona.services;

import java.util.List;

import com.alaona.entities.auctions.Auction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.alaona.entities.users.RegistereduserBidsinAuction;

public interface AuctionServices {

	/*
	 * Return list of auctions depending on type (active, closed, all) with use of pagination
	 */
	public List<Auction> getAuctions(int startpage, int endpage, String type);
	
	/*
	 * Return list of categories of auctions depending on type (active, closed, all)
	 */
	public List<Object[]> getCategories(String type);
	
	/*
	 * Return the number of auctions depending on type (active, closed, all)
	 */
	public int numOfAuctions(String type);
	
	/*
	 * Return details of auction of the given item
	 */
	public Auction getDetails(int ItemID);
	
	/*
	 * Return number of bids of the given auction
	 */
	public int getNumOfBids(int auction_id);
	
	/*
	 * Return auction's ID of given item
	 */
	public int getAuctionIDByItem(int item_id);
	
	/*
	 * Return highest bid of given auction
	 */
	public float getHighestBid(int auction_id);
	
	/*
	 * Return list of auctions of given category depending on type (active, closed, all)
	 */
	public List<Auction> getAuctionsByCategory(int start, int end, String Category, String type);
	
	/*
	 * Create auction with given parameters and insert it to the database
	 */
	public int createAuction(String username, JSONObject auction_params);
	
	/*
	 * Delete auction with given parameters
	 */
	public int deleteAuction(String username, int auctionID, int itemID);
	
	/*
	 * Insert bid for given item, given bidder and given amount
	 */
	public int submitBid(String username, int itemID, float bid_amount);
	
	/*
	 * End auction and register given item to the given user/buyer
	 */
	public int buyItem(String username, int itemID);
	
	/*
	 * Return bid history of given auction
	 */
	public JSONArray getBidHistory(int auctionID);
	
	/*
	 * Return list of auction that fit given criteria
	 */
	public List<Auction> advancedSearch(String keywords, String description, List<String> Categories, String Location, String minBid, String maxBid, int startpage, int endpage);
	
	/*
	 * Return number of auctions that fit given criteria
	 */
	public int numOfadvancedSearch(String keywords, String description, List<String> Categories, String Location, String minBid, String maxBid);
	
	/*
	 * Return list of auctions that end time < current and have no bid
	 */
	public List<Auction> getExpiredAuctions();
	
	/*
	 * Return auction details in which user made a bid or was the seller
	 */
	public List<Object[]> BidderExpiredAuctions(String username);
	
	/*
	 * Return bids of all users
	 */
	public List<RegistereduserBidsinAuction> getBidsOfAllUsers();
	
	/*
	 * Return auction with given ID
	 */
	public Auction getAuctionByID(int AuctionID);
	
	/*
	 * Return if auction can be edited (no bids or start time > current)
	 */
	public boolean auctionCanBeEdited(int auctionID);
	
	/*
	 * Return list of auctions to export with use of pagination
	 */
	public List<Object[]> getAuctionsForExport(int startpage,int endpage);
	
	/*
	 * Update details of given auction
	 */
	public int updateAuction(int auctionID, JSONObject auction_params);
	
	/*
	 * Return list of closed auctions (end time < current and auction has at least 1 bid) of the given user with use of pagination
	 */
	public List<Object[]> checkUserClosedAuctions(String username, int startpage, int endpage);
	
}
