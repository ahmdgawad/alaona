package com.alaona.filters;
import java.io.IOException;

import javax.persistence.EntityTransaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;


import com.alaona.jpautils.EntityManagerHelper;

/**
 * Servlet Filter implementation class EntityManagerInterceptor
 * This class intercepts the requests for the DB utilities
 * Auctomatically begins the transactions requested with the database
 */
@WebFilter("/EntityManagerInterceptor")
public class EntityManagerInterceptor implements Filter {


	    
    public EntityManagerInterceptor() {
       // System.out.println("<<<<< EntityManagerInterceptor Constructor >>>>");
     
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		//System.out.println("<<<<< EntityManagerInterceptor Destructor >>>>");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//System.out.println("<<<<< EntityManagerInterceptor doFilter >>>>");
		try {
			EntityManagerHelper.beginTransaction();
			chain.doFilter(request, response);
			EntityManagerHelper.commit();
		} 
		catch (RuntimeException e) 
		{
			EntityTransaction tx = EntityManagerHelper.getTransaction();
			if (tx != null && tx.isActive()) 
				EntityManagerHelper.rollback();
		    throw e;

			
		} 
		finally {
			EntityManagerHelper.closeEntityManager();
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		//System.out.println("<<<<< EntityManagerInterceptor Init Filter >>>>");
	}

}

