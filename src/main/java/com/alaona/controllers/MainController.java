package com.alaona.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alaona.services.AuctionServices;
import com.alaona.services.ConversationServices;
import com.alaona.services.UserServices;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;


/**
 * Handles requests for the application home page 
 * and for the login / register services.
 */
@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	private static String log_status = "offline";

	/* Some dependency injection with the help of autowiring */
	@Autowired
    UserServices userServices;
	
	@Autowired
    AuctionServices auctionServices;
	
	@Autowired
    ConversationServices conversationServices;
	
	@RequestMapping(value = {"","/index"})
	public String indexRedirection() {
	
		return "pages/index.html";
	}
	
	@RequestMapping(value = {"/contact"})
	public static String contactRedirection() {
		
		return "/pages/blank.html";
	}
	
	@RequestMapping(value = {"/login-signup"})
	public static String loginRedirection() {
		
		return "/pages/login.html";
	}
	
	@RequestMapping(value = {"/auctions"})
	public static String auctionsRedirection() {
		
		return "/pages/auctions.html";
	}
	
	@RequestMapping(value = {"/auctions/item/{item_id}"})
	public static String itemRedirection() {
		
		return "/pages/item.html";
	}
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String login( @RequestParam("username") String username,
            								@RequestParam("password") String password,
            								HttpServletRequest request,
            								HttpServletResponse response) {
		
		
		//System.out.println("username: " + username + "password: " + password);
		
		if(username.equals("admin") && password.equals("admin")){
			
			log_status = "admin";			
			return new Gson().toJson("administrator");
			
		} else {
			
			int status = userServices.Login(username,password);
			if(status==0)
			{
				//System.out.println("The user is pending");				
				return new Gson().toJson("user/?status=pending");
			}
			else if(status==1){
					//System.out.println("user entered");					
					return new Gson().toJson("user/"+username);
			}
			else {
				
				return new Gson().toJson("Problem");
			}
				
		}
	}
	

	@RequestMapping(value = "/signup",method = RequestMethod.POST)
	@ResponseBody
	public String signup(@RequestParam("json") String params,
							HttpServletRequest request,
							HttpServletResponse response){
		JSONObject jobj;
		String username="",password="",firstname="",lastname="",email="";
		
		String trn="", phonenumber="", city="", street="", region="", zipcode="";
		
		try {
			jobj = new JSONObject(params);
			
			username = jobj.get("username").toString();
			password = jobj.getString("password").toString();
			firstname = jobj.getString("firstname").toString();
			lastname = jobj.getString("lastname").toString();
			email = jobj.getString("email").toString();
			trn = jobj.getString("trn").toString();
			phonenumber = jobj.getString("phonenumber").toString();
			city = jobj.getString("city").toString();
			street = jobj.getString("street").toString();
			region = jobj.getString("region").toString();
			zipcode = jobj.getString("zipcode").toString();
				
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
		/*query returns true if user exists*/
		boolean userExists = userServices.userExists(username);
		//System.out.println("Get response from userExists : "+userExists);
		
		if( userExists ){
			return new Gson().toJson("exists");
		}
		else{
		
			userServices.userRegistration(username, password, firstname,
					lastname, email, trn, phonenumber, city, street, region, zipcode);

			return new Gson().toJson("user/?status=pending");
		}
	}

	@RequestMapping(value = "/logout")
	public void logout(HttpServletRequest request,
			HttpServletResponse response) {
		
		response.setHeader("Content", "");
	}
	
	
}
