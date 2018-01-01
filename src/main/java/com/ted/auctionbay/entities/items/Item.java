package com.ted.auctionbay.entities.items;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the item database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i"),
	@NamedQuery(name="Item.findItem", query="SELECT i FROM Item i WHERE i.itemID = :id"),
	@NamedQuery(name="Item.itemMaxID", query="SELECT MAX(i.itemID) FROM Item i")
})
//@SequenceGenerator(initialValue = 1, name = "itemSeq", sequenceName = "ITEM_SEQ")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO, generator = "itemSeq")
	private int itemID;

	@Lob
	private String description;

	private double latitude;

	private String location;

	private double longitute;

	private String name;

	//bi-directional many-to-many association to Category
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(
		name="item_has_category"
		, joinColumns={
			@JoinColumn(name="ItemID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="CategoryID")
			}
		)
	private List<Category> categories;

	public Item() {
	}

	public int getItemID() {
		return this.itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLongitute() {
		return this.longitute;
	}

	public void setLongitute(double longitute) {
		this.longitute = longitute;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	/*
	 * Inserts a new category to the item
	 */
	public void insertCategory(Category category) {
		if(this.categories == null) {
			this.categories = new ArrayList<Category>();
		}
		this.categories.add(category);
		category.insertItem(this);
		
	}
	
	public void deleteCategory(Category category) {
		this.categories.remove(category);
		category.deleteItem(null);
		
	}

}