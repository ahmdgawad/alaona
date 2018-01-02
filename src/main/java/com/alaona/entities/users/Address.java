package com.alaona.entities.users;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the address database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Address.findAll", query="SELECT a FROM Address a"),
	@NamedQuery(name="Address.maxID", query="SELECT MAX(a.addressID) FROM Address a")
})
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int addressID;

	@Column(name="City")
	private String city;

	@Column(name="Region")
	private String region;

	@Column(name="Street")
	private String street;

	@Column(name="ZipCode")
	private String zipCode;

	
	@OneToOne(mappedBy="address")
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

	public Address() {
	}

	public int getAddressID() {
		return this.addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	



}