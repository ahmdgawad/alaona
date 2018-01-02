package com.alaona.recommendations;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.alaona.dao.QueryAuction;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.alaona.entities.users.RegistereduserBidsinAuction;

@Component("recommendation")
public class RecommendationService{
	
	@Autowired
	private QueryAuction queryAuction;

	private static float[][] similarityMatrix;
	private static int N;
	private static Map<String,Set<Integer>> auctions;
	private static Map<Integer,String> idToUserMap;
	private static Map<String,Integer> userToIDrMap;
	private static Map<String,Set<Integer>> recommendationMap;
	private final static int MAX_NEIGHBORS = 10;
	
	private static boolean INITIALIZED = false;
	

	public void start(){
		System.out.println("Scheduler called Recommendation Service");
		auctions = getBidsPerUser();
		N = auctions.size();
		similarityMatrix = new float[N][N];
		
		//Create idToUserMap
		idToUserMap = new TreeMap<Integer,String>();
		Iterator<String> iteratoruser = auctions.keySet().iterator();
		int k = 0;
		while(iteratoruser.hasNext()){
			String username = iteratoruser.next();
			idToUserMap.put(k,username );
			k++;
		}
		
		//Create userToIdMap
		userToIDrMap = new HashMap<String,Integer>();
		Iterator<Entry<Integer, String>> iteratorid = idToUserMap.entrySet().iterator();
		while(iteratorid.hasNext()){
			Entry<Integer, String> e = iteratorid.next();
			userToIDrMap.put(e.getValue(), e.getKey());
		}
		
		recommendationMap = new HashMap<String,Set<Integer>>();
		
		int j=0,i=0;
		for( i = 0; i < N; i++){
			for(j = 0; j < N; j++){
				similarityMatrix[i][j] = computeSimilarity(i,j);
			}
		}
		System.out.println("RECOMMENDATIONS INITIALIZED NOW");
		INITIALIZED = true;
		//System.out.println("auctions:"+auctions);
		
	}
	
	private HashMap<String, Set<Integer>> getBidsPerUser() {
		
		List<RegistereduserBidsinAuction> bidsOfUsers = queryAuction.getBidsOfAllUsers();
		if(bidsOfUsers == null){
			System.out.println("bidsOfUsers == null");
		}
		//System.out.println("SIZE OF USERS' BIDS: " + bidsOfUsers.size());
		HashMap<String,Set<Integer>> bidmap = new HashMap<String,Set<Integer>>();
		for(RegistereduserBidsinAuction bid : bidsOfUsers){
			Set<Integer> set = new HashSet<Integer>();
			if(bidmap.containsKey(bid.getId().getBidder_Username())){
				Set<Integer> s = bidmap.get(bid.getId().getBidder_Username());
				set.addAll(s);
			}
			set.add(bid.getId().getAuctionID());
			bidmap.put(bid.getId().getBidder_Username(), set);
		}
		return bidmap;
	}
	
	private static float computeSimilarity(int i, int j) {
		
		//Compare similarity between given username_ids and return intersection set
		String username_i = idToUserMap.get(i);
		String username_j = idToUserMap.get(j);
		
		Set<Integer> auctions_i = auctions.get(username_i);
		Set<Integer> auctions_j = auctions.get(username_j);
		return Sets.intersection(auctions_i, auctions_j).size();
	}

	public Set<Integer> getRecommendationForUser(String username){
		
		//If maps are initialized create a sorted array using similarity matrix and comparator, then create recommendations set using the neighbors algorithm
		//System.out.println("Initialized value: " + INITIALIZED);
		if(!INITIALIZED) {
			System.out.println("NOT INITIALIZED YET");
			return null;
		}
		//System.out.println("");
		
		Integer i = userToIDrMap.get(username);
		
		if(i == null){
			//System.out.println("i is null");
			return null;
		}
		
		JSONObject[] array = new JSONObject[N];
		Set<Integer> recommendationsSet = new TreeSet<Integer>();
		
		//Find and keep to an array all similarities of the given user
		for(int j = 0; j < N; j++)
			try {
				array[j] = new JSONObject().put("user", j).put("similarity", similarityMatrix[i][j]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
		//Sort the array with ascending order and get the auctions of given user
		Comparator<JSONObject> comparator = new SimilarityComparator();
		Arrays.sort(array, comparator);
		Set<Integer> auctions_i = auctions.get(username);
		
		//for the 'Max neighbors' users get their auctions and store which auctions the given user has not made a bid wet
		for(int j = 0; j < MAX_NEIGHBORS; j++){
			try {
				if( array[j].getDouble("similarity") > 0 ){
					String username_j;
					username_j = idToUserMap.get(array[j].get("user"));
					Set<Integer> auctions_j = auctions.get(username_j);
					//System.out.println(auctions_j);
					recommendationsSet.addAll(Sets.difference(auctions_j, auctions_i).immutableCopy());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("Printing the SET for the user: " + username);
		//System.out.println("--------------------------------------");
		/*for (int s : recommendationsSet) { 
		    System.out.println(s);
		}
		System.out.println("--------------------------------------");*/
		return  recommendationsSet;
	}
	
	public Map<String,Set<Integer>> getRecommendationForAllUsers(){
		
		//Call getRecommendationForUser for every user
		Iterator<String> it = userToIDrMap.keySet().iterator();
		Map<String,Set<Integer>> map = new HashMap<String,Set<Integer>>();
		while(it.hasNext()){
			String username = it.next();
			Set<Integer> set = getRecommendationForUser(username);
			map.put(username,set);
		}
		return map;
	}



}

class SimilarityComparator implements Comparator<JSONObject>{

	@Override
	public int compare(JSONObject o1, JSONObject o2) {
			
		try {
			double s1 = o1.getDouble("similarity");
			double s2 = o2.getDouble("similarity");
			if( s1 < s2 ) return 1;
			if( s1 == s2 )return 0;
			return -1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
}
