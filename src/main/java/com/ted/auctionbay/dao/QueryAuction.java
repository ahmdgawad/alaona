package com.ted.auctionbay.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.auctions.Auctionhistory;
import com.ted.auctionbay.entities.users.RegistereduserBidsinAuction;

public interface QueryAuction {
	
	/*
	 * Get how many auctions are in database
	 */
	public int numOfAuctions();
	
	/*
	 * Get how many active auctions, which endtime > current, are in database
	 */
	public int numOfActiveAuctions();
	
	/*
	 * Get how many auctions, which endtime < current and has at least 1 bid, are in database
	 */
	public int numOfClosedAuctions();
	
	/*
	 * Return list of all auctions
	 */
	public List<Auction> getAuctions();
	
	/*
	 * Return list of all auctions with use of pagination
	 */
	public List<Auction> getAuctions(int startpage, int endpage);
	
	/*
	 * Return list of all active auctions with use of pagination
	 */
	public List<Auction> getActiveAuctions(int startpage, int endpage);
	
	/*
	 * /Return list of all auctions, which endtime < current, with use of pagination
	 */
	public List<Auction> getExpiredAuctions();
	
	/*
	 * Return auction of the given ID
	 */
	public Auction getAuctionByID(int AuctionID);
	
	/*
	 * Return auction of the given item ID
	 */
	public int getAuctionIDByItem(int ItemID);
	
	/*
	 * Return list of auctions of the given category with use of pagination
	 */
	public List<Auction> getAuctionsByCategory(int startpage, int endpage, String category);
	
	/*
	 * Return list of auctions of the given category, which endtime > current, with use of pagination
	 */
	public List<Auction> getActiveAuctionsByCategory(int startpage, int endpage, String category);
	
	/*
	 * Return the number of bids in auction with given ID
	 */
	public int getNumOfBids(int auction_id);
	
	/*
	 * Return highest bid of given auction
	 */
	public float getHighestBid(int auction_id);
	
	/*
	 * Return details of an auction with given item ID
	 */
	public Auction getDetails(int ItemID);
	
	/*
	 * Return the max ID number of auctions
	 */
	public int maxAuctionID();
	
	/*
	 * Insert auction in database
	 */
	public int submitAuction(Auction auction);
	
	/*
	 * Delete auction from database
	 */
	public int deleteAuction(String username, int itemID,int auctionID);
	
	/*
	 * Return if given user has already made a bid for the given item ID
	 */
	public boolean alreadyBidded(String username, int itemID);
	
	/*
	 * Update details of bid in database
	 */
	public void updateBid(String username, int itemID, float bid_amount);
	
	/*
	 * Return bids of given auction ID
	 */
	public List<Object[]> getBidHistory(int auctionID);
	
	/*
	 * Return list of auctions that fit the given criteria
	 */
	public List<Auction> advancedSearch(String keywords, String description, List<String> Categories, String Location, String minBid, String maxBid, int startpage, int endpage);
	
	/*
	 * Return number of auctions that fit the given criteria
	 */
	public int numOfadvancedSearch(String keywords, String description, List<String> Categories, String Location, String minBid, String maxBid);
	
	/*
	 * Return auction details in which user made a bid or was the seller
	 */
	public List<Object[]> BidderExpiredAuction(String username);
	
	/*
	 * Return bids of all users
	 */
	public List<RegistereduserBidsinAuction> getBidsOfAllUsers();
	
	/*
	 * Return all auctions for export with use of pagination
	 */
	public List<Object[]> getAuctionsForExport(int startpage,int endpage);
	
	/*
	 * Return if auction can be edited (no bidder or start time < current)
	 */
	public boolean auctionCanBeEdited(int auctionID);
	
	/*
	 * Return success if end time of auction is updated successfully
	 */
	public int updateDeadline(int auctionID);
	
	/*
	 * Return success if all given fields of auction are updated successfully
	 */
	public int updateAuction(int auctionID, String title, float buyprice, float firstbid, Date endtime);
	
	/*
	 * Return given users auctions, where endtime < current and auction has at least 1 bid
	 */
	public List<Object[]> getUserClosedAuctions(String username, int startpage, int endpage);
	
	/*
	 * Return given users auctions, where endtime < current and auction has no bid
	 */
	public List<Auction> getUserExpiredAuctions(String username, int startpage, int endpage);
	
	/*
	 * Return if auction is in history table in database
	 */
	public int auctionInHistory(int itemID);
	
	/*
	 * Update an auction's history
	 */
	public int updateAuctionHistory(Auctionhistory ah);
}
