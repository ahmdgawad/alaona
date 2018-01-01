package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the registereduser_bidsin_auction database table.
 * 
 */
@Embeddable
public class RegistereduserBidsinAuctionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	//@Column(insertable=false, updatable=false)
	private String bidder_Username;

	//@Column(insertable=false, updatable=false)
	private int auctionID;

	public RegistereduserBidsinAuctionPK() {
	}
	public String getBidder_Username() {
		return this.bidder_Username;
	}
	public void setBidder_Username(String bidder_Username) {
		this.bidder_Username = bidder_Username;
	}
	public int getAuctionID() {
		return this.auctionID;
	}
	public void setAuctionID(int auctionID) {
		this.auctionID = auctionID;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RegistereduserBidsinAuctionPK)) {
			return false;
		}
		RegistereduserBidsinAuctionPK castOther = (RegistereduserBidsinAuctionPK)other;
		return 
			this.bidder_Username.equals(castOther.bidder_Username)
			&& (this.auctionID == castOther.auctionID);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.bidder_Username.hashCode();
		hash = hash * prime + this.auctionID;
		
		return hash;
	}
}