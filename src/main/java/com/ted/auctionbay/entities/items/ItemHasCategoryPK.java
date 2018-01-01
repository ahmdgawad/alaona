package com.ted.auctionbay.entities.items;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the item_has_category database table.
 * 
 */
@Embeddable
public class ItemHasCategoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column
	private int itemID;

	@Column
	private int categoryID;

	public ItemHasCategoryPK() {
	}
	public int getItemID() {
		return this.itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public int getCategoryID() {
		return this.categoryID;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ItemHasCategoryPK)) {
			return false;
		}
		ItemHasCategoryPK castOther = (ItemHasCategoryPK)other;
		return 
			(this.itemID == castOther.itemID)
			&& (this.categoryID == castOther.categoryID);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.itemID;
		hash = hash * prime + this.categoryID;
		
		return hash;
	}
}