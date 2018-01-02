package com.ted.auctionbay.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.auctions.Auctionhistory;
import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.entities.users.RegistereduserBidsinAuction;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

public class QueryAuctionImpl implements QueryAuction {

	@Override
	public int numOfAuctions() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction");
		int num = Integer.parseInt(query.getResultList().get(0).toString());

		return num;
	}
	
	@Override
	public int numOfActiveAuctions() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction WHERE EndTime >= NOW()");
		int num;
		if(query.getResultList().get(0) == null){
			num = 0;
		} else {
			num = Integer.parseInt(query.getResultList().get(0).toString());
		}
		

		return num;
	}
	
	@Override
	public int numOfClosedAuctions() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction WHERE EndTime <= NOW() and AuctionID IN (SELECT rba.AuctionID FROM registereduser_bidsin_auction as rba)");
		int num;
		if(query.getResultList().get(0) == null){
			num = 0;
		} else {
			num = Integer.parseInt(query.getResultList().get(0).toString());
		}
		

		return num;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getAuctions() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction",
				Auction.class);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getAuctions(int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction",
				Auction.class);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);

		return query.getResultList();

	}

	@Override
	public int getNumOfBids(int auction_id) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery("SELECT count(rba.AuctionID) FROM registereduser_bidsin_auction rba"
						+ " WHERE rba.AuctionID LIKE ?1 GROUP BY rba.AuctionID");
		query.setParameter(1, auction_id).getFirstResult();
		List<?> list = query.getResultList();
		if (!list.isEmpty()) {
			return Integer.parseInt(list.get(0).toString());
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getAuctionsByCategory(int startpage, int endpage,
			String category) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery(
						"SELECT a.AuctionID,a.ItemID,a.Seller,a.Title,a.BuyPrice,a.FirstBid,a.StartTime,a.EndTime "
								+ "FROM auction a,category c,item_has_category ihc, item i"
								+ " where a.ItemID = i.ItemID and i.ItemID = ihc.ItemID and ihc.CategoryID = c.CategoryID"
								+ " and c.Name = ?1", Auction.class);

		query.setParameter(1, category);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Auction getDetails(int ItemID) {
		//System.out.println("getting auction details with id: " + ItemID);
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery(
				"SELECT * FROM auction WHERE ItemID=?", Auction.class);
		query.setParameter(1, ItemID);
		List<Auction> Set = query.getResultList();
		return Set.get(0);
	}

	@Override
	public int maxAuctionID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		int maxID;
		Object idSet = em.createNamedQuery("Auction.auctionMaxID")
				.getResultList().get(0);
		if (idSet == null) {
			maxID = 0;
		} else {
			maxID = Integer.parseInt(idSet.toString()) + 1;
		}
		return maxID;
	}

	@Override
	public int submitAuction(Auction auction) {

		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			em.persist(auction);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return -1;
		}
		return 0;
	}

	@Override
	public int deleteAuction(String username, int itemID, int auctionID) {
		//System.out.print("Ready to delete auction: " +auctionID);
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("DELETE FROM auction WHERE Seller=?1 and AuctionID=?2 and ItemID=?3");
		query.setParameter(1, username);
		query.setParameter(2, auctionID);
		query.setParameter(3, itemID);
		return query.executeUpdate();
	}

	@Override
	public float getHighestBid(int auction_id) {
		// System.out.println("auction_id: " + auction_id);
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery("SELECT MAX(BidPrice) FROM registereduser_bidsin_auction WHERE AuctionID=?");
		query.setParameter(1, auction_id);

		float highestBid;
		List bidList = query.getResultList();
		//System.out.println(bidList.get(0));
		if (bidList.get(0) == null || bidList.isEmpty()) {
			highestBid = 0;
		} else {
			highestBid = Float.parseFloat(bidList.get(0).toString());
		}

		return highestBid;
	}

	@Override
	public boolean alreadyBidded(String username, int itemID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery("SELECT COUNT(*) FROM registereduser_bidsin_auction rba, auction a "
						+ "WHERE rba.AuctionID = a.AuctionID AND rba.Bidder_Username=?1 AND a.ItemID=?2 ");
		query.setParameter(1, username);
		query.setParameter(2, itemID);
		int number = Integer.parseInt(query.getResultList().get(0).toString());

		if (number != 0) {
			return true;
		}

		return false;
	}

	@Override
	public void updateBid(String username, int itemID, float bid_amount) {
		//System.out.print("updateBid");
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery(
				"Select rba.Bidder_Username, rba.auctionID, rba.BidPrice, rba.BidTime "
						+ "from registereduser_bidsin_auction rba,auction a "
						+ "where rba.AuctionID = a.AuctionID and "
						+ "rba.Bidder_Username = ?1 and " + "a.ItemID = ?2",
				RegistereduserBidsinAuction.class);
		query.setParameter(1, username);
		query.setParameter(2, itemID);

		RegistereduserBidsinAuction rba = (RegistereduserBidsinAuction) query
				.getResultList().get(0);
		rba.setBidPrice(bid_amount);
		rba.setBidTime(new Date());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getBidHistory(int auctionID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery("SELECT rba.Bidder_username,rba.BidPrice,rba.BidTime "
						+ "FROM registereduser_bidsin_auction rba "
						+ "WHERE rba.AuctionID = ?");
		query.setParameter(1, auctionID);

		return query.getResultList();
	}

	
	@Override
	public int numOfadvancedSearch(String keywords, String description, List<String> Categories, String Location, String minBid,
			String maxBid) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		String buildquery = "SELECT COUNT(DISTINCT(a.AuctionID)) "
				+ "FROM auction a,category c,item_has_category ihc, item i "
				+ "WHERE a.ItemID = i.ItemID and i.ItemID = ihc.ItemID and ihc.CategoryID = c.CategoryID and a.EndTime >=NOW() ";
		if (!keywords.equals("")){
			buildquery = buildquery + "and (a.Seller LIKE '%"+keywords+"%' or a.Title LIKE '%"+keywords+"%') ";
		}
		if (!description.equals("")){
			buildquery = buildquery + "and i.Description LIKE '%"+description+"%' ";
		}
		if (Categories.size()!=0){
			buildquery = buildquery + "and (";
			int count=0;
			for (String category:Categories){
				if (count==0)
					buildquery = buildquery + "c.Name = '"+category+"' ";
				else
					buildquery = buildquery + "or c.Name = '"+category+"' ";
				count++;
			}
			buildquery = buildquery + ") ";
		}
		if (!Location.equals(""))
			buildquery = buildquery + "and i.Location = '"+Location+"' ";
		if (!minBid.equals(""))
			buildquery = buildquery + "and a.FirstBid >= '"+minBid+"' ";
		if (!maxBid.equals(""))
			buildquery = buildquery + "and a.FirstBid <= '"+maxBid+"' ";
		Query query = em.createNativeQuery(buildquery);
		
		int number;
		if(query.getResultList().get(0) == null){
			number = 0;
		} else {
			number = Integer.parseInt(query.getResultList().get(0).toString());
		}
		return number;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> advancedSearch(String keywords, String description,
			List<String> Categories, String Location, String minBid,
			String maxBid, int startpage, int endpage) {
		//System.out.print("Keyword: "+keywords+"\nCategories: "+Categories+"\nLocations: "+"\nminBid: "+minBid+"\nmaxBid: "+maxBid+"\n");
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		String buildquery = "SELECT DISTINCT(a.AuctionID),a.ItemID,a.Seller,a.Title,a.BuyPrice,a.FirstBid,a.StartTime,a.EndTime "
				+ "FROM auction a,category c,item_has_category ihc, item i "
				+ "WHERE a.ItemID = i.ItemID and i.ItemID = ihc.ItemID and ihc.CategoryID = c.CategoryID and a.EndTime >=NOW() ";
		if (!keywords.equals("")){
			buildquery = buildquery + "and (a.Seller LIKE '%"+keywords+"%' or a.Title LIKE '%"+keywords+"%') ";
		}
		if (!description.equals("")){
			buildquery = buildquery + "and i.Description LIKE '%"+description+"%' ";
		}
		if (Categories.size()!=0){
			buildquery = buildquery + "and (";
			int count=0;
			for (String category:Categories){
				//System.out.print("\nCategory: "+category+"\n");
				if (count==0){
					buildquery = buildquery + "c.Name = '"+category+"' ";
				}
				else
					buildquery = buildquery + "or c.Name = '"+category+"' ";
				count++;
			}
			buildquery = buildquery + ") ";
		}
		if (!Location.equals(""))
			buildquery = buildquery + "and i.Location = '"+Location+"' ";
		if (!minBid.equals(""))
			buildquery = buildquery + "and a.FirstBid >= '"+minBid+"' ";
		if (!maxBid.equals(""))
			buildquery = buildquery + "and a.FirstBid <= '"+maxBid+"' ";
		//System.out.print("\nQuery: "+buildquery+"\n");
		Query query = em.createNativeQuery(buildquery,Auction.class);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		return query.getResultList();
	}

	@Override
	public List<Auction> getActiveAuctions(int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction WHERE EndTime >= NOW()",Auction.class);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		return query.getResultList();
	}

	@Override
	public List<Auction> getExpiredAuctions() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction WHERE EndTime < NOW()",Auction.class);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> BidderExpiredAuction(String username) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT a.AuctionID, a.Seller, a.Title, ruba.Bidder_Username"
				+ " FROM auction a, registereduser_bidsin_auction ruba"
				+ " WHERE EndTime < NOW() AND a.AuctionID = ruba.AuctionID"
				+ " AND(a.Seller = ?1 OR ruba.Bidder_Username = ?2)"
				+ " AND (a.Seller NOT IN (SELECT Username FROM sellerrating WHERE Rate > 0) or ruba.Bidder_Username NOT IN (SELECT Username FROM bidderrating WHERE Rate > 0))");
		query.setParameter(1, username);
		query.setParameter(2, username);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getActiveAuctionsByCategory(int startpage, int endpage, String category) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery(
						"SELECT a.AuctionID,a.ItemID,a.Seller,a.Title,a.BuyPrice,a.FirstBid,a.StartTime,a.EndTime "
								+ "FROM auction a,category c,item_has_category ihc, item i"
								+ " where a.ItemID = i.ItemID and i.ItemID = ihc.ItemID and ihc.CategoryID = c.CategoryID"
								+ " and c.Name = ?1 and a.EndTime >= NOW()", Auction.class);

		query.setParameter(1, category);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistereduserBidsinAuction> getBidsOfAllUsers() {
		//System.out.print("\nentered getauctionofallusers\n");
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM registereduser_bidsin_auction", RegistereduserBidsinAuction.class);
		List<RegistereduserBidsinAuction> rlist = query.getResultList();
		//System.out.print("\nafter query getauctionofallusers\n");
		
		return rlist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Auction getAuctionByID(int AuctionID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction WHERE AuctionID=?",Auction.class);
		query.setParameter(1, AuctionID);
		List<Auction> set = query.getResultList();
		return set.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int getAuctionIDByItem(int ItemID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT AuctionID FROM auction WHERE ItemID=?");
		query.setParameter(1, ItemID);
		List<String> set = query.getResultList();
		return Integer.parseInt(set.get(0).toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAuctionsForExport(int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT AuctionID,ItemID, Title, Seller FROM auction");
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		return query.getResultList();
	}

	@Override
	public boolean auctionCanBeEdited(int auctionID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT IF((SELECT count(a.AuctionID) FROM auction as a, registereduser_bidsin_auction as rba WHERE (a.AuctionID = ?1 and a.AuctionID = rba.AuctionID and a.StartTime <= NOW())) = 0 ,'1','0') as Can_Edit");
		query.setParameter(1, auctionID);
		String can_edit = query.getResultList().get(0).toString();
		if (can_edit.equals("0")){
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public int  updateAuction(int auctionID, String title, float buyprice,
			float firstbid, Date endtime) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("UPDATE auction set Title=?1, BuyPrice=?2, FirstBid=?3, EndTime=?4 WHERE AuctionID=?5");
		query.setParameter(1, title);
		query.setParameter(2, buyprice);
		query.setParameter(3, firstbid);
		query.setParameter(4, endtime);
		query.setParameter(5, auctionID);
		return query.executeUpdate();
	}

	@Override
	public int updateDeadline(int auctionID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("UPDATE auction set EndTime=NOW() WHERE AuctionID=?1");
		query.setParameter(1, auctionID);
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUserClosedAuctions(String username,
			int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery(
						"SELECT a.AuctionID, a.ItemID, a.Title, rba.Bidder_Username, rba.BidPrice, a.EndTime FROM auction as a, registereduser_bidsin_auction as rba"
                        + " WHERE a.AuctionID = rba.AuctionID"
                        + " and rba.BidPrice = (SELECT MAX(rba1.BidPrice) FROM registereduser_bidsin_auction as rba1 WHERE rba1.AuctionID=a.AuctionID)"
                        + " and a.EndTime <= NOW()"
                        + " and a.Seller = ?"
                        + " ORDER BY a.EndTime DESC");

		query.setParameter(1, username);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getUserExpiredAuctions(String username,
			int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery(
						"SELECT a.* FROM auction as a"
                        + " WHERE a.AuctionID NOT IN (SELECT rba.AuctionID FROM registereduser_bidsin_auction as rba)"
                        + " and a.Seller = ? and a.EndTime <= NOW()",Auction.class);

		query.setParameter(1, username);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		return query.getResultList();
	}

	@Override
	public int auctionInHistory(int itemID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery(
						"SELECT IFNULL( (SELECT '1' FROM auction as a, auctionhistory as ah WHERE a.ItemID=ah.ItemID and a.ItemID=? ),'0') as Found");

		query.setParameter(1, itemID);
		int found = Integer.parseInt(query.getResultList().get(0).toString());
		return found;
	}

	@Override
	public int updateAuctionHistory(Auctionhistory ah) {
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			em.persist(ah);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return -1;
		}
		return 0;
	}

}
