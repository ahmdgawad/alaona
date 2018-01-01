package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the bidderrating database table.
 * 
 */
@Embeddable
public class BidderratingPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int bidderRatingID;

	@Column(insertable=false, updatable=false)
	private String username;

	public BidderratingPK() {
	}
	public int getBidderRatingID() {
		return this.bidderRatingID;
	}
	public void setBidderRatingID(int bidderRatingID) {
		this.bidderRatingID = bidderRatingID;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BidderratingPK)) {
			return false;
		}
		BidderratingPK castOther = (BidderratingPK)other;
		return 
			(this.bidderRatingID == castOther.bidderRatingID)
			&& this.username.equals(castOther.username);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.bidderRatingID;
		hash = hash * prime + this.username.hashCode();
		
		return hash;
	}
}