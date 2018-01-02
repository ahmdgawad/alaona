package com.alaona.entities.items;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c"),
	@NamedQuery(name="Category.categoryMaxID", query="SELECT MAX(c.categoryID) FROM Category c")
})
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int categoryID;

	private String name;

	//bi-directional many-to-many association to Item
	@ManyToMany(mappedBy="categories")
	private List<Item> items;

	public Category() {
	}

	public int getCategoryID() {
		return this.categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void insertItem(Item item) {
		if(this.items == null) {
			this.items = new ArrayList<Item>();
		}
		this.items.add(item);
	
	}
	
	public void deleteItem(Item item) {
		this.items.remove(item);
		
	}
	
}