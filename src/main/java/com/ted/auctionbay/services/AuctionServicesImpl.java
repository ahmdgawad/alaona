package com.ted.auctionbay.services;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ted.auctionbay.dao.QueryAuction;
import com.ted.auctionbay.dao.QueryCategory;
import com.ted.auctionbay.dao.QueryConversation;
import com.ted.auctionbay.dao.QueryItem;
import com.ted.auctionbay.dao.QueryUser;
import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.auctions.Auctionhistory;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.entities.users.Registereduser;
import com.ted.auctionbay.entities.users.RegistereduserBidsinAuction;
import com.ted.auctionbay.entities.users.RegistereduserBidsinAuctionPK;
import com.ted.auctionbay.entities.users.User;

@Service("auctionServices")
public class AuctionServicesImpl implements AuctionServices{

	@Autowired
	QueryCategory queryCategory;
	
	@Autowired
	QueryAuction queryAuction;
	
	@Autowired
	QueryItem queryItem;
	
	@Autowired
	QueryUser queryUser;
	
	@Autowired
	ConversationServices conversationServices;
	
	private static int auctionID;
	private static int itemID;
	private static int categoryID;
	
	
	@Override
	public List<Auction> getAuctions(int startpage,int endpage, String type) {
		if(type.equals("active")){
			return queryAuction.getActiveAuctions(startpage, endpage);
		}
		return queryAuction.getAuctions(startpage, endpage);
	}

	@Override
	public int numOfAuctions(String type) {
		if(type.equals("active")){
			return queryAuction.numOfActiveAuctions();
		}
		else if(type.equals("closed")){
			return queryAuction.numOfClosedAuctions();
		}
		return queryAuction.numOfAuctions();
	}

	@Override
	public Auction getDetails(int AuctionID) {
		return queryAuction.getDetails(AuctionID);
	}

	@Override
	public int getNumOfBids(int auction_id) {
		return queryAuction.getNumOfBids(auction_id);
	}

	@Override
	public float getHighestBid(int auction_id) {
		return queryAuction.getHighestBid(auction_id);
	}

	@Override
	public List<Auction> getAuctionsByCategory(int start, int end, String Category, String type) {
		if(type.equals("active")){
			return queryAuction.getActiveAuctionsByCategory(start, end, Category);
		}
		return queryAuction.getAuctionsByCategory(start,end,Category);
	}

	@Override
	public List<Object[]> getCategories(String type) { 
		if(type.equals("active")){
			return queryCategory.getActiveCategories();
		}
		return queryCategory.getAllCategories();
	}

	@Override
	public int createAuction(String username, JSONObject auction_params) {
		
		auctionID = queryAuction.maxAuctionID();
		itemID = queryItem.maxItemID();
		categoryID = queryCategory.maxCategoryID();
	
		User user = queryUser.getUser(username);
		if(user == null) {
			return -1;
		}
		Item i = new Item();
		i.setItemID(itemID);
		try {
			i.setName(auction_params.getString("auction_name"));
			i.setDescription(auction_params.getString("auction_desc"));
			float lat = Float.parseFloat(auction_params.get("lat").toString());
			i.setLatitude(lat);
			float lon = Float.parseFloat(auction_params.get("lon").toString());
			i.setLongitute(lon);
			i.setLocation(auction_params.getString("auction_country"));
			
			
		} catch (JSONException e) {
			System.out.println("Could not get data from json object, 101");
			e.printStackTrace();
		}
		
		
		List<Category> cat_list = queryCategory.fetchCategories();
		HashMap<String,Category> cat_map = new HashMap<String,Category>();
		for(Category c:cat_list){
			cat_map.put(c.getName(), c);
		}
		
		
		try {
			JSONArray categories_arr = auction_params.getJSONArray("auction_category");
			
			for(int j=0; j<categories_arr.length(); j++) {
				Category category = null;
				String cat_name = categories_arr.getString(j);
				if(cat_map.containsKey(cat_name)) {
					category = cat_map.get(cat_name);
				} else {
					//System.out.println("New category: " + cat_name);
					//System.out.println("Category ID: " + categoryID);
					category = new Category();
					category.setCategoryID(categoryID);
					category.setName(cat_name);
					categoryID++;
				}
				i.insertCategory(category);
				
			}
		} catch (JSONException e) {
			System.out.println("Could not get categories JSONArray");
			e.printStackTrace();
		}
		
		Auction auction = new Auction();
		auction.setAuctionID(auctionID);
		try {
			auction.setTitle(auction_params.getString("auction_name"));
			float buyPrice = Float.parseFloat(auction_params.get("buyPrice").toString());
			float firstBid = Float.parseFloat(auction_params.get("first_bid").toString());
			auction.setBuyPrice(buyPrice);
			auction.setFirstBid(firstBid);
			auction.setItem(i);
			
			
			Calendar calendar = Calendar.getInstance();
			Date start =  calendar.getTime();
	        auction.setStartTime(start);
	        
	        String deadline = auction_params.getString("deadline");
	        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	        org.joda.time.DateTime dt = formatter.parseDateTime(deadline);
	        Date endDate = dt.toDate();
	        auction.setEndTime(endDate);
	        //System.out.println("startTime: " + start + " --- EndTime: " + endDate);
	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		auction.setRegistereduser(user.getRegistereduser());
		if(queryAuction.submitAuction(auction) == -1){
			System.out.println("Could not register auction");
			return -2;
		}
	
		return 0;
	}

	@Override
	public int deleteAuction(String username, int auctionID, int itemID) {
		
		queryAuction.deleteAuction(username,itemID,auctionID);
		Item item = queryItem.getDetails(itemID);
		return queryItem.deleteItem(itemID);	
	}

	@Override
	public int getAuctionIDByItem(int item_id) {
		return queryAuction.getAuctionIDByItem(item_id);
	}

	@Override
	public int submitBid(String username, int itemID, float bid_amount) {
		// Check if the user has already bidded on the product, then update it
		if(queryAuction.alreadyBidded(username, itemID)) {
			// update row
			//System.out.println("Updating " + username + " Auction!! ");
			queryAuction.updateBid(username, itemID, bid_amount);
			return 0;
		} else {
			
			// else register a new bid for the user
			
			Auction auction = queryAuction.getDetails(itemID);
			Registereduser reg_user = queryUser.getUser(username).getRegistereduser();
			//System.out.println("Registered user: " + reg_user.getUsername() + " bids for auction with id: "+auction.getAuctionID() +
			//		" and item: " + auction.getItem().getItemID()+"/" + itemID);
			
			RegistereduserBidsinAuctionPK rbaPK = new RegistereduserBidsinAuctionPK();
			rbaPK.setBidder_Username(username);
			rbaPK.setAuctionID(auction.getAuctionID());
			
			RegistereduserBidsinAuction rba = new RegistereduserBidsinAuction();
			rba.setBidPrice(bid_amount);
			rba.setId(rbaPK);
			Date current_time = new Date();
			rba.setBidTime(current_time);
			
			if(queryUser.createBidInUser(rba) == 0){
				//System.out.println("Bid created");
				return 0;
			}
				
		}
		
		return -1;
	}

	@Override
	public JSONArray getBidHistory(int auctionID) {
		List<Object[]> bidsList = queryAuction.getBidHistory(auctionID);
		JSONArray bidsHistory = new JSONArray();
		for(Object[] obj : bidsList) {
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("Bidder", obj[0].toString());
				jobj.put("BidPrice", obj[1].toString());
				bidsHistory.put(jobj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return bidsHistory;
	}

	@Override
	public List<Auction> advancedSearch(String keywords, String description,
			List<String> Categories, String Location, String minBid,
			String maxBid, int startpage, int endpage) {
		return queryAuction.advancedSearch(keywords, description, Categories, Location, minBid, maxBid, startpage, endpage);
	}

	@Override
	public List<Auction> getExpiredAuctions() {
		return queryAuction.getExpiredAuctions();
	}

	@Override
	public int buyItem(String username, int itemID) {
		Auction a = queryAuction.getDetails(itemID);
		queryAuction.updateDeadline(a.getAuctionID());
		if(queryUser.appendBuyerHistory(username, itemID) == 0){
			return 0;	
		}
		return 1;
	}

	@Override
	public List<Object[]> BidderExpiredAuctions(String username) {
		
		return queryAuction.BidderExpiredAuction(username);
	}

	@Override
	public List<RegistereduserBidsinAuction> getBidsOfAllUsers() {
		return queryAuction.getBidsOfAllUsers();
	}

	@Override
	public Auction getAuctionByID(int AuctionID) {
		return queryAuction.getAuctionByID(AuctionID);
	}

	@Override
	public List<Object[]> getAuctionsForExport(int startpage, int endpage) {
		return queryAuction.getAuctionsForExport(startpage, endpage);
	}

	@Override
	public boolean auctionCanBeEdited(int auctionID) {
		return queryAuction.auctionCanBeEdited(auctionID);
	}

	@Override
	public int updateAuction(int auctionID, JSONObject auction_params) {
		float buyprice = -1, firstbid = -1;
		String title="", name="", description="", location = "";
		Double latitude = null, longitude = null;
		Date endtime = null;
		List<Integer> categories = new ArrayList<Integer>();
		Auction auction = queryAuction.getAuctionByID(auctionID);
		Item item = auction.getItem();
		JSONArray categories_arr = null;
		try {
		auctionID = Integer.parseInt(auction_params.getString("auctionID"));
		title = auction_params.getString("auction_name");
		buyprice = Float.parseFloat(auction_params.getString("buyPrice"));
		firstbid = Float.parseFloat(auction_params.getString("first_bid"));
		String deadline = auction_params.getString("deadline");
        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        org.joda.time.DateTime dt = formatter.parseDateTime(deadline);
        endtime = dt.toDate();
		name = auction_params.getString("auction_name");
		description = auction_params.getString("auction_desc");
		location = auction_params.getString("auction_country");
		latitude = Double.parseDouble(auction_params.get("lat").toString());
		longitude = Double.parseDouble(auction_params.get("lon").toString());
		categories_arr = auction_params.getJSONArray("auction_category");
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//Get all categories of the item
		List<Category> old_categories = item.getCategories();
		
		int categoryID = queryCategory.maxCategoryID();
		List<Category> cat_list = queryCategory.fetchCategories();
		HashMap<String,Category> cat_map = new HashMap<String,Category>();
		
		//For every category given, add new categories to item and database, if not exists.
		for(Category c:cat_list){
			cat_map.put(c.getName(), c);
		}
		try {
			for(int j=0; j<categories_arr.length(); j++) {
				Category category = null;
				String cat_name;
			
				cat_name = categories_arr.getString(j);
			
				if(cat_map.containsKey(cat_name)) {
					category = cat_map.get(cat_name);
				} else {
					System.out.println("New category: " + cat_name);
					System.out.println("Category ID: " + categoryID);
					category = new Category();
					category.setCategoryID(categoryID);
					category.setName(cat_name);
					categoryID++;
				}
				System.out.print(category.getCategoryID());
				if (!old_categories.contains(category))
					item.insertCategory(category);
				categories.add(category.getCategoryID());
			}
			
			//Check if old item categories exists in new categories list. If does not exists, then delete.
			List<Category> temp = new ArrayList<Category>();
			for (int i=0;i<old_categories.size();i++){
				Category c = old_categories.get(i);
				System.out.println("Checking Category: "+c.getCategoryID()+" | "+c.getName());
				if (!categories.contains(c.getCategoryID())){
					System.out.println("Deleting Category: "+c.getCategoryID()+" | "+c.getName());
					temp.add(c);
				}
			}
			//For every category in temp delete from item
			for (Category c : temp){
				item.deleteCategory(c);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//Auction details
		auction.setAuctionID(auctionID);
		auction.setBuyPrice(buyprice);
		auction.setEndTime(endtime);
		auction.setFirstBid(firstbid);
		auction.setItem(item);
		auction.setTitle(title);
	
		//Item of the Auction details
		item.setDescription(description);
		item.setLatitude(latitude);
		item.setLongitute(longitude);
		item.setLocation(location);
		item.setName(name);
		if(queryAuction.submitAuction(auction) == -1){
			System.out.println("Could not register auction");
			return -2;
		}
		
        return 0;
	}

	@Override
	public int numOfadvancedSearch(String keywords, String description, List<String> Categories, String Location, String minBid,
			String maxBid) {
		
		return queryAuction.numOfadvancedSearch(keywords, description, Categories, Location, minBid, maxBid);
	}

	@Override
	public List<Object[]> checkUserClosedAuctions(String username,
			int startpage, int endpage) {
		List<Object[]> auctionlist = queryAuction.getUserClosedAuctions(username, startpage, endpage);
		for (Object[] auction : auctionlist){
			int found = queryAuction.auctionInHistory(Integer.parseInt(auction[1].toString()));
			if (found == 0){
				Auctionhistory ah = new Auctionhistory();
				ah.setUsername(auction[3].toString());
				ah.setItemID(Integer.parseInt(auction[1].toString()));
				queryAuction.updateAuctionHistory(ah);
				String buyer, seller, bsubject, ssubject, bbody, sbody;
				buyer = auction[3].toString();
				bsubject = "Auction "+ auction[0].toString() +" Won!!!";
				bbody = "Dear " + buyer+ ", \n\nCongratulations you have won item: "+auction[2].toString()+
						" !!!\n\nDo not reply to this message.\n\nYours truly\nAuctionbay Team";
				
				seller = username;
				ssubject = "Auction "+ auction[0].toString() +" Closed!!!";
				sbody = "Dear " + seller +",\n\nAuction: "+auction[2].toString()+" closed with final price "+auction[4].toString()+"\nPlease contact the winner "+auction[3].toString()+
						"\n\nDo not reply to this message.\n\nYours truly\nAuctionbay Team";
				
				conversationServices.submitMessage("system", seller, ssubject, sbody);
				conversationServices.submitMessage("system", buyer, bsubject, bbody);
				System.out.println("Messages to buyer and seller were sent!!");

			}
			else
				break;
		}
		return auctionlist;
	}
}
