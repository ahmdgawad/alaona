package com.alaona.dao;

import java.util.List;

import com.alaona.entities.items.Item;

public interface QueryItem {
	
	/*
	 * Return all items
	 */
	public List<Item> getItems();
	
	/*
	 * Return coordinates of given item ID
	 */
	public List<Double> getCoordinates(int ItemID);

	/*
	 * Return name of categories of given item ID
	 */
	public List<String> getCategories(int ItemID);
	
	/*
	 * Return id of categories of given item ID
	 */
	public List<Integer> getCategories_ID(int ItemID);
	
	/*
	 * Return number of items in database
	 */
	public int getNumberofItems();
	
	/*
	 * Return details of given item ID
	 */
	public Item getDetails(int ItemID);
	
	/*
	 * Return location of given item ID
	 */
	public String getLocation(int ItemID);
	
	/*
	 * Return max id of items
	 */
	public int maxItemID();
	
	/*
	 * Delete given item from database
	 */
	public int deleteItem(int itemID);
	
	/*
	 * Return all items' ID
	 */
	public List<Integer> getItemIDs();
	
	/*
	 * Update the entry of given item in database
	 */
	public int updateItem(int itemID, String name, String description, String location, Double latitude, Double longitude);
	
	/*
	 * Add given category to given item
	 */
	public int addCategory(int categoryID, int itemID);
	
	/*
	 * Remove given category from given item
	 */
	public int removeCategory(int categoryID, int itemID);

}
