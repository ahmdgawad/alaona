package com.alaona.entities.users;

import java.io.Serializable;
import javax.persistence.*;

import com.alaona.entities.auctions.Auction;
import com.alaona.entities.users.messages.Conversation;

import java.util.List;


/**
 * The persistent class for the registereduser database table.
 * 
 */
@Entity
@NamedQuery(name="Registereduser.findAll", query="SELECT r FROM Registereduser r")
public class Registereduser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	//bi-directional many-to-one association to Bidderrating
	@OneToMany(mappedBy="registereduser")
	private List<Bidderrating> bidderratings;


	//bi-directional many-to-one association to Sellerrating
	@OneToMany(mappedBy="registereduser")
	private List<Sellerrating> sellerratings;
	
	
	//bi-directional many-to-one association to Conversation
	@OneToMany(mappedBy="recipient")
	private List<Conversation> conversations;
	
	

	public List<Conversation> getConversations() {
		return conversations;
	}

	public void setConversations(List<Conversation> conversations) {
		this.conversations = conversations;
	}

	//bi-directional many-to-one association to Auction
	@OneToMany(mappedBy="registereduser")
	private List<Auction> auctionsList;
	
	public List<Auction> getAuctionsList() {
		return auctionsList;
	}

	public void setAuctionsList(List<Auction> auctionsList) {
		this.auctionsList = auctionsList;
	}

	public List<Auction> getAuctionsBids() {
		return auctionsBids;
	}

	public void setAuctionsBids(List<Auction> auctionsBids) {
		this.auctionsBids = auctionsBids;
	}

	

	
	
	
	//bi-directional one-to-one association to User
	@OneToOne
	@PrimaryKeyJoinColumn(name="Username")
	private User user;

	/*
	 * 
	 * Info: 
	 * http://stackoverflow.com/questions/28755832/jpa-many-to-many-relation-not-inserting-into-generated-table
	 * 
	 * */
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
		name="registereduser_bidsin_auction"
		, joinColumns={
			@JoinColumn(name="bidder_Username")
			}
		, inverseJoinColumns={
			@JoinColumn(name="AuctionID")
			}
		)
	private List<Auction> auctionsBids;
	
	

	public Registereduser() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Bidderrating> getBidderratings() {
		return this.bidderratings;
	}

	public void setBidderratings(List<Bidderrating> bidderratings) {
		this.bidderratings = bidderratings;
	}

	public Bidderrating addBidderrating(Bidderrating bidderrating) {
		getBidderratings().add(bidderrating);
		bidderrating.setRegistereduser(this);

		return bidderrating;
	}

	public Bidderrating removeBidderrating(Bidderrating bidderrating) {
		getBidderratings().remove(bidderrating);
		bidderrating.setRegistereduser(null);

		return bidderrating;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

	public List<Sellerrating> getSellerratings() {
		return this.sellerratings;
	}

	public void setSellerratings(List<Sellerrating> sellerratings) {
		this.sellerratings = sellerratings;
	}

	public Sellerrating addSellerrating(Sellerrating sellerrating) {
		getSellerratings().add(sellerrating);
		sellerrating.setRegistereduser(this);

		return sellerrating;
	}

	public Sellerrating removeSellerrating(Sellerrating sellerrating) {
		getSellerratings().remove(sellerrating);
		sellerrating.setRegistereduser(null);

		return sellerrating;
	}

}