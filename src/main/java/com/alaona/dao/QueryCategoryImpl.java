package com.alaona.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.alaona.entities.items.Category;
import com.alaona.jpautils.EntityManagerHelper;

public class QueryCategoryImpl implements QueryCategory{

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllCategories(){
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT c.name,COUNT(*) FROM category c, item_has_category ihc "
				+ "where c.categoryID = ihc.categoryID GROUP by c.categoryID");
		List<Object[]> categoryList = query.getResultList();
		
		return categoryList;
	}

	@Override
	public int maxCategoryID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		int maxID;
		Object idSet  =  em.createNamedQuery("Category.categoryMaxID").getResultList().get(0);
		if(idSet == null) {
			maxID = 0;
		} else {
			maxID = Integer.parseInt(idSet.toString()) + 1;
		}
		return maxID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> fetchCategories() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM category",Category.class);
		List<Category> cat_list = query.getResultList();
		return cat_list;
	}

	@Override
	public List<Object[]> getActiveCategories() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		Query query = em.createNativeQuery("SELECT c.name,COUNT(*) FROM auction a, category c, item_has_category ihc "
				+ "where c.categoryID = ihc.categoryID and a.ItemID = ihc.itemID and a.EndTime >= NOW() GROUP by c.categoryID");
		List<Object[]> categoryList = query.getResultList();
		
		return categoryList;
	}
}
