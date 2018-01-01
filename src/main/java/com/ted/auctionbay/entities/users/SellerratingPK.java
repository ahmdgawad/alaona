package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the sellerrating database table.
 * 
 */
@Embeddable
public class SellerratingPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int sellerRatingID;

	@Column(insertable=false, updatable=false)
	private String username;

	public SellerratingPK() {
	}
	public int getSellerRatingID() {
		return this.sellerRatingID;
	}
	public void setSellerRatingID(int sellerRatingID) {
		this.sellerRatingID = sellerRatingID;
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
		if (!(other instanceof SellerratingPK)) {
			return false;
		}
		SellerratingPK castOther = (SellerratingPK)other;
		return 
			(this.sellerRatingID == castOther.sellerRatingID)
			&& this.username.equals(castOther.username);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.sellerRatingID;
		hash = hash * prime + this.username.hashCode();
		
		return hash;
	}
}