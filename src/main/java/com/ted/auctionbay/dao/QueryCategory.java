package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.items.Category;

public interface QueryCategory {
	
	/*
	 * Return list of all categories and the number of auctions they are used
	 */
	public List<Object[]> getAllCategories();
	
	/*
	 * Return list of all categories of active auctions (endtime > current) and the number of auctions they are used
	 */
	public List<Object[]> getActiveCategories();
	
	/*
	 * Return max id of categories
	 */
	public int maxCategoryID();
	
	/*
	 * Return list of all categories
	 */
	public List<Category> fetchCategories();
}
