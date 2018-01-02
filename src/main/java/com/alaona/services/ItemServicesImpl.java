package com.alaona.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.alaona.dao.QueryAuction;
import com.alaona.dao.QueryItem;
import com.alaona.dao.QueryUser;
import com.alaona.entities.auctions.Auction;
import com.alaona.entities.items.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.springframework.beans.factory.annotation.Autowired;

import com.alaona.entities.items.Category;
import com.alaona.entities.users.User;

public class ItemServicesImpl  implements ItemServices{
	
	@Autowired
    QueryAuction queryAuction;
	
	@Autowired
    QueryItem queryItem;
	
	@Autowired
    QueryUser queryUser;
	
	public static HashMap<String,Integer> bidderRanking;
	public static HashMap<String,Integer> sellerRanking;
	static HashMap<Integer,Auction> auctions;
	static HashMap<String,User> users;
	
	@Override
	public List<Item> getAllItems() {
		return queryItem.getItems();
	}

	@Override
	public Item getDetails(int ItemID) {
		return queryItem.getDetails(ItemID);
	}

	@Override
	public int getNumberofItems() {
		return queryItem.getNumberofItems();
	}

	@Override
	public List<String> getCategories(int ItemID) {
		return queryItem.getCategories(ItemID);
	}

	@Override
	public List<Double> getCoordinates(int ItemID) {
		return queryItem.getCoordinates(ItemID);
	}

	@Override
	public String getLocation(int ItemID) {
		return queryItem.getLocation(ItemID);
	}
	
	@Override
	public void exportToXML(String ItemID) throws IOException {
		initializeRatingData();
		Document doc = XMLExporter(ItemID);
		String filename = ItemID;
		WriteToXMLFile(doc,filename);	
	}

	private static void WriteToXMLFile(Document doc, String filename) throws IOException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		    DOMSource source = new DOMSource(doc);

		    File exportFile = new File("C:\\AuctionBayXML\\item"+filename+".xml");
			try {
				exportFile.getParentFile().mkdirs();
				exportFile.createNewFile();
				System.out.print("File: C:\\AuctionBayXML\\item"+filename+".xml exported.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    try {
				try {
					transformer.transform(source, 
					     new StreamResult(new FileWriter(exportFile)));
				} catch (TransformerException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (TransformerConfigurationException e2) {
			e2.printStackTrace();
		}
		
	}

	@Override
	public void exportAllToXML() throws IOException {
		
		List<Integer> itemIDs = queryItem.getItemIDs();
		//System.out.print("Size of list: "+itemIDs.size()+"\n");
		if (itemIDs.size() == 0){
			return;
		}
		
		
		int count = 0;
		Document doc;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document fileDoc = null;
		Element rootElement = null;
		int fileNumber = 0;
		
		initializeRatingData();
		
		int i = 0;
		for(i=0;i < itemIDs.size() ;i++){/**/
			
			if( i%100 == 0){
				if(i != 0){
					WriteToXMLFile(fileDoc,Integer.toString(fileNumber));
					//System.out.println("writing file "+fileNumber);
					fileNumber++;
				}
					
				try {
					docBuilder = docFactory.newDocumentBuilder();
					fileDoc = docBuilder.newDocument();
					
					rootElement = fileDoc.createElement("Items");
					fileDoc.appendChild(rootElement);
					
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}		
			}
			//System.out.println("store item "+i+ "to file "+i/100);
			doc = XMLExporter(itemIDs.get(i).toString());
		    Element e = doc.getDocumentElement();
		    Node n = (Node) doc.getChildNodes().item(0);
		    Node newNode = (Node) fileDoc.importNode(n, true);
		    rootElement.appendChild(newNode);
		}
		WriteToXMLFile(fileDoc,Integer.toString(fileNumber));
		//System.out.println("writing file "+fileNumber);
	}
	
	
	@Override
	public Document XMLExporter(String ItemID){
					
		try {
			Auction auction = queryAuction.getDetails(Integer.parseInt(ItemID));
			Item item = queryItem.getDetails(Integer.parseInt(ItemID));
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Item");
			doc.appendChild(rootElement);
			rootElement.setAttribute("ItemID",Integer.toString(item.getItemID()));
			
			
			Element name = doc.createElement("Name");
			name.appendChild(doc.createTextNode(item.getName()));
			rootElement.appendChild(name);
			
			List<Category> categories = item.getCategories();
			for(Category c:categories){
				Element cat = doc.createElement("Category");
				cat.appendChild(doc.createTextNode(c.getName()));
				rootElement.appendChild(cat);
			}
			
			Element currently = doc.createElement("Currently");
			
			currently.appendChild(doc.createTextNode("$" + queryAuction.getHighestBid(auction.getAuctionID())));
			rootElement.appendChild(currently);
			
			Element fb = doc.createElement("First_Bid");
			fb.appendChild(doc.createTextNode("$" +auction.getFirstBid()));
			rootElement.appendChild(fb);
			
			int numberOfBids = queryAuction.getNumOfBids(auction.getAuctionID());
			
			Element nb = doc.createElement("Number_of_Bids");
			nb.appendChild(doc.createTextNode(""+numberOfBids));
			rootElement.appendChild(nb);
			
			Element bids = doc.createElement("Bids");
			rootElement.appendChild(bids);
			
			if( numberOfBids != 0 ){
				List<Object[]> resultSet = queryAuction.getBidHistory(auction.getAuctionID());
				for(Object[] r:resultSet){
					Element bid = doc.createElement("Bid");
					
					User u = queryUser.getUser(r[0].toString());
					Element bidder = doc.createElement("Bidder");
					
					int rating;
					if(bidderRanking.containsKey(u.getUsername()))
						rating = bidderRanking.get(u.getUsername());	
					else
						rating = 0;
					
					bidder.setAttribute("Rating",Integer.toString(rating) );
					bidder.setAttribute("UserID",u.getUsername());
					
					Element s = doc.createElement("Street");
					s.appendChild(doc.createTextNode(u.getAddress().getStreet()));
					
					Element c = doc.createElement("City");
					c.appendChild(doc.createTextNode(u.getAddress().getCity()));
					
					bidder.appendChild(s);
					bidder.appendChild(c);
					
					Element time = doc.createElement("Time");
					time.appendChild(doc.createTextNode(r[2].toString()));
					
					Element amount = doc.createElement("Amount");
					amount.appendChild(doc.createTextNode(r[1].toString()));
					
					bid.appendChild(bidder);
					bid.appendChild(time);
					bid.appendChild(amount);
					bids.appendChild(bid);
				}
				
			}
			
			
			Element l = doc.createElement("Location");
			l.appendChild(doc.createTextNode("Latitude=" + item.getLatitude() + " " + "Longitude=" + item.getLongitute()));
			rootElement.appendChild(l);
			
			
			Element c = doc.createElement("Country");
			c.appendChild(doc.createTextNode(item.getLocation()));
			rootElement.appendChild(c);
			
			Element st = doc.createElement("Started");
			st.appendChild(doc.createTextNode(auction.getStartTime().toString()));
			rootElement.appendChild(st);
			
			Element ends = doc.createElement("Ends");
			ends.appendChild(doc.createTextNode(auction.getEndTime().toString()));
			rootElement.appendChild(ends);
			
			Element seller = doc.createElement("Seller");
			
			
			int rating;
			if(sellerRanking.containsKey( auction.getRegistereduser().getUsername()) ){
				//System.out.println(item.getItemID() + "/" +auction.getRegistereduser().getUsername() );
				rating = sellerRanking.get( auction.getRegistereduser().getUsername());
			}else
				rating = 0;
			
			seller.setAttribute("Rating",Integer.toString(rating) );
			seller.setAttribute("UserID",auction.getRegistereduser().getUsername());
			
			
			rootElement.appendChild(seller);
			
			Element d = doc.createElement("Description");
			d.appendChild(doc.createTextNode(item.getDescription()));
			rootElement.appendChild(d);
			
			return doc;
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return null;
		
	}

	@Override
	public void initializeRatingData() throws IOException {
        List<Auction> auctionslist = queryAuction.getAuctions();
		
		auctions = new HashMap<Integer,Auction>();
		for(Auction a:auctionslist){
			auctions.put(a.getItem().getItemID(), a);
		}
		
		//System.out.println("auctions = "+auctions.size());
		List<User> userslist = (List<User>)queryUser.getUsers();
		users = new HashMap<String,User>();
		for(User user:userslist){
			users.put(user.getUsername(), user);
		}
		//System.out.println("users = "+users.size());
		
		List<String> bidders = queryUser.getBiddersbyRate(); 		
		
		bidderRanking = new HashMap<String,Integer>();
		int rankPos = 1;
		for(String user:bidders){
			bidderRanking.put(user, rankPos);
			rankPos++;
		}
			
		
		List<String> sellers = queryUser.getSellersbyRate(); 		
		sellerRanking = new HashMap<String,Integer>();
		rankPos = 1;
		for(String user:sellers){
			sellerRanking.put(user, rankPos);
			rankPos++;
		}
	}

	@Override
	public int addCategory(int categoryID,int itemID) {
		return queryItem.addCategory(categoryID, itemID);
	}

}
