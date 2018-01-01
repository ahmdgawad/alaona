package com.ted.auctionbay.entities.auctions;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the auctionhistory database table.
 * 
 */
@Entity
@NamedQuery(name="Auctionhistory.findAll", query="SELECT a FROM Auctionhistory a")
public class Auctionhistory implements Serializable {
	private static final long serialVersionUID = 1L;

	private int itemID;
	
	@Id
	private String username;

	public Auctionhistory() {
	}

	public int getItemID() {
		return this.itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}