package com.alaona.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.alaona.entities.users.Pendinguser;
import org.springframework.stereotype.Component;

import com.alaona.entities.auctions.Auction;
import com.alaona.entities.auctions.Auctionhistory;
import com.alaona.entities.users.Bidderrating;
import com.alaona.entities.users.Registereduser;
import com.alaona.entities.users.RegistereduserBidsinAuction;
import com.alaona.entities.users.Sellerrating;
import com.alaona.entities.users.User;
import com.alaona.jpautils.EntityManagerHelper;

@Component
public class QueryUserImpl implements QueryUser{
	
	@Override
	public boolean userExists(String username) {	
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT Username FROM user WHERE Username =?");
		query.setParameter(1, username); 
		boolean res = !query.getResultList().isEmpty(); //then user exists already
		//System.out.println("user exist: " + res);
		return res;
	}
	
	@Override
	public boolean fetchPendingByUsername(String username){
		
		boolean result = false;
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT Username FROM pendinguser WHERE Username =?");
		query.setParameter(1, username); 
		result = !query.getResultList().isEmpty(); //then the user is pending 		
		return result;
	}
	
	@Override
	public int count_registered() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT count(*) FROM registereduser");
		int number = Integer.parseInt(q.getResultList().get(0).toString());
		
		return number;
		
	}
	
	@Override
	public int count_pending() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT count(*) FROM pendinguser");
		int number = Integer.parseInt(q.getResultList().get(0).toString());
		
		return number;
		
	}
	
	@Override
	public boolean user_validator(String username, String password){
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT Username,Password FROM user WHERE Username =? AND Password=?");
		q.setParameter(1,username);
		q.setParameter(2, password);
		if(!q.getResultList().isEmpty()){
			return true;
		}
		return false;
		
	}
	
	@Override
	public List<Pendinguser>  getPendingUsers(int startpage, int pagesize){
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM pendinguser",Pendinguser.class);
		query.setFirstResult(startpage);
		query.setMaxResults(pagesize);
		List<Pendinguser> resultSet = query.getResultList();
		return resultSet;
	}
	
	@Override
	public int getAddressMaxID(){
		//System.out.println("address Max");
		EntityManager em = EntityManagerHelper.getEntityManager();
		List<?> resultSet  =  em.createNamedQuery("Address.maxID").getResultList();
		int id;
		if( resultSet.get(0) == null)
			id = 0;
		else
			id = Integer.parseInt(resultSet.get(0).toString());
		 
		return id;
	
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Registereduser> getGroupsOfUsers(int startpage, int pagesize){
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		//System.out.println("Registered -> startpage: " + startpage + " pagesize: " + pagesize);
		Query query = em.createQuery("SELECT r FROM Registereduser r");
		query.setFirstResult(startpage);
		query.setMaxResults(pagesize);
		List<Registereduser> regUsers = query.getResultList();
		
		return regUsers;
		
	
		
	}
	
	@Override
	public int registeredNumber() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		int rn =  Integer.parseInt( em.createNativeQuery("SELECT count(*) FROM registereduser")
				 				.getResultList().get(0).toString());
		return rn;
		
	}
	

	@Override
	public void accept_user(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		//System.out.println("EM is Open = "+em.isOpen());
		
		// Check with native query if it exists
		
		Pendinguser penUser = (Pendinguser) em.createNativeQuery("SELECT Username FROM pendinguser WHERE Username=?",Pendinguser.class).setParameter(1, username).getResultList().get(0);
		//System.out.println("penUser username: " + penUser.getUsername());
		Registereduser regUser = new Registereduser();
		regUser.setUsername(username);
		penUser.getUser().setRegistereduser(regUser);
		regUser.setUser(penUser.getUser());
		
		em.createNativeQuery("DELETE FROM pendinguser WHERE Username=?",Pendinguser.class).setParameter(1,
				username).executeUpdate();
		//System.out.println("regUser: " + regUser.getUsername());
		em.persist(regUser);
		
	}
	
	@Override
	public int registerUser(User ruser){
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			em.persist(ruser);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return 1;
		}
		return 0;
	}

	@Override
	public int count_all_user_auctions(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction WHERE Seller=?");
		query.setParameter(1, username);
		return Integer.parseInt(query.getResultList().get(0).toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> get_all_user_auctions(String username,int startpage, int pagesize) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction WHERE Seller=?",Auction.class);
		query.setFirstResult(startpage);
		query.setMaxResults(pagesize);
		query.setParameter(1, username);
		
		return query.getResultList();
	}

	@Override
	public User getUser(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM user WHERE Username = ?",User.class);
		query.setParameter(1, username);
		User user = (User) query.getResultList().get(0);
		if(user != null) {
			return user;
		}
		return null;
	}

	@Override
	public void deleteBidderFromAuction(String username, int auctionID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("DELETE FROM registereduser_bidsin_auction WHERE Username = ?1 AND AuctionID = ?2",User.class);
		query.setParameter(1, username);
		query.setParameter(2, auctionID);
	}

	@Override
	public int createBidInUser(RegistereduserBidsinAuction rba) {
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			em.persist(rba);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return 1;
		}
		return 0;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Registereduser> getRecipients() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		Query query = em.createNativeQuery("SELECT * FROM registereduser",Registereduser.class);
		return query.getResultList();
	}

	@Override
	public int appendBuyerHistory(String username, int itemID) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		Auctionhistory history = new Auctionhistory();
		history.setItemID(itemID);
		history.setUsername(username);
		
		try {
			
			em.persist(history);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return 1;
		}
		return 0;
	}

	@Override
	public int maxBidderRatingID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT MAX(BidderRatingID) FROM bidderrating");
		List result  =  query.getResultList();
		int maxID;
		if( result.get(0) == null){
			//System.out.println("null id");
			maxID = 0;
		}else {
			maxID = Integer.parseInt(result.get(0).toString())+1;
		}
		return maxID;
	}

	@Override
	public int maxSellerRatingID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT MAX(SellerRatingID) FROM sellerrating");
		List result  =  query.getResultList();
		int maxID;
		if( result.get(0) == null){
			maxID = 0;
		}else {
			maxID = Integer.parseInt(result.get(0).toString()) + 1;
		}
		return maxID;
	}

	@Override
	public void submitBidderRating(Bidderrating bidder_rate) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		try {
			em.persist(bidder_rate);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			
		}
		
	}

	@Override
	public void submitSellerRating(Sellerrating seller_rate) {
	EntityManager em = EntityManagerHelper.getEntityManager();
		
		try {
			em.persist(seller_rate);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM user",User.class);
		List<User> user = query.getResultList();
		if(user != null) {
			return user;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getBiddersbyRate() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT Username FROM bidderrating group by Username order by avg(Rate);");
		List<String> user = query.getResultList();
		if(user != null) {
			return user;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSellersbyRate() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT Username FROM sellerrating group by Username order by avg(Rate);");
		List<String> user = query.getResultList();
		if(user != null) {
			return user;
		}
		return null;
	}

	@Override
	public List<Auction> get_active_user_auctions(String username,int startpage, int pagesize) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction WHERE Seller=? AND EndTime >= NOW()",Auction.class);
		query.setFirstResult(startpage);
		query.setMaxResults(pagesize);
		query.setParameter(1, username);
		
		return query.getResultList();
		
	}

	@Override
	public List<Auction> get_expired_user_auctions(String username,int startpage, int pagesize) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em
				.createNativeQuery(
						"SELECT a.* FROM auction as a"
                        + " WHERE a.AuctionID NOT IN (SELECT rba.AuctionID FROM registereduser_bidsin_auction as rba)"
                        + " and a.Seller = ? and a.EndTime <= NOW()",Auction.class);

		query.setParameter(1, username);
		query.setFirstResult(startpage);
		query.setMaxResults(pagesize);
		return query.getResultList();
	}

	@Override
	public int count_active_user_auctions(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction WHERE Seller=? AND EndTime >= NOW()");
		query.setParameter(1, username);
		return Integer.parseInt(query.getResultList().get(0).toString());
	}

	@Override
	public int count_expired_user_auctions(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction WHERE Seller=? AND EndTime < NOW()");
		query.setParameter(1, username);
		return Integer.parseInt(query.getResultList().get(0).toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUserBids(String username, int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT a.AuctionID, a.ItemID, a.Title, a.Seller, a.Endtime, a.BuyPrice,"
				+ "(SELECT MAX(BidPrice) FROM registereduser_bidsin_auction WHERE Bidder_Username=?1) as Highset_Bid, rba.BidPrice, rba.BidTime "
				+ "FROM auction as a, registereduser_bidsin_auction as rba WHERE a.AuctionID = rba.AuctionID and rba.Bidder_Username=?2 "
				+ "and a.EndTime >= NOW()");
		query.setParameter(1, username);
		query.setParameter(2, username);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		return query.getResultList();
	}

	@Override
	public int getUserBidsNum(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT COUNT(*) FROM auction as a, registereduser_bidsin_auction as rba "
				+ "WHERE a.AuctionID = rba.AuctionID and rba.Bidder_Username=?1 "
				+ "and a.EndTime >= NOW()");
		query.setParameter(1, username);
		int num;
		if( query.getResultList().get(0) == null){
			
			num= 0;
		}else {
			num = Integer.parseInt(query.getResultList().get(0).toString());
		}
		return num;
	}

	@Override
	public int countClosedAuctions(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction WHERE Seller=?1 and EndTime <= NOW() and AuctionID IN (SELECT rba.AuctionID FROM registereduser_bidsin_auction as rba)");
		query.setParameter(1, username);
		int num;
		if(query.getResultList().get(0) == null){
			num = 0;
		} else {
			num = Integer.parseInt(query.getResultList().get(0).toString());
		}
	
		return num;
		
	}
	
}
