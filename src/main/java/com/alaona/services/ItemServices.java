package com.alaona.services;

import java.io.IOException;
import java.util.List;

import com.alaona.entities.items.Item;
import org.w3c.dom.Document;

public interface ItemServices {

	/*
	 * Return list of all items
	 */
	public List<Item> getAllItems();
	
	/*
	 * Return details of given item
	 */
	public Item getDetails(int ItemID);
	
	/*
	 * Return number of items
	 */
	public int getNumberofItems();
	
	/*
	 * Return list of all categories of given item
	 */
	public List<String> getCategories(int ItemID);
	
	/*
	 * Return coordinates of given item
	 */
	public List<Double> getCoordinates(int ItemID);
	
	/*
	 * Return location of given item
	 */
	public String getLocation(int ItemID);
	
	/*
	 * Ready given item to be exported
	 */
	public Document XMLExporter(String ItemID);
	
	/*
	 * Export all items to xml files
	 */
	public void exportAllToXML() throws IOException;
	
	/*
	 * Export given item to xml file
	 */
	public void exportToXML(String ItemID) throws IOException;
	
	/*
	 * Initialize Rates of users
	 */
	public void initializeRatingData() throws IOException;
	
	/*
	 * Insert given category to given item
	 */
	public int addCategory(int categoryID, int itemID);
	
}
