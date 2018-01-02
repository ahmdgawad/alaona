package com.alaona.entities.users;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	private int bidderRating;

	@Column(name="Email")
	private String email;

	private String firstName;

	private String lastname;

	private String password;
	
	@Column(name="PhoneNumber")
	private String phoneNumber;

	private int sellerRating;

	@Column(name="TRN")
	private String trn;

	//bi-directional one-to-one association to Pendinguser
	@OneToOne(mappedBy="user", cascade=CascadeType.ALL)
	private Pendinguser pendinguser;

	//bi-directional one-to-one association to Registereduser
	@OneToOne(mappedBy="user", cascade=CascadeType.ALL)
	private Registereduser registereduser;

	//bi-directional many-to-one association to Address
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="AddressID")
	private Address address;

	public User() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getBidderRating() {
		return this.bidderRating;
	}

	public void setBidderRating(int bidderRating) {
		this.bidderRating = bidderRating;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getSellerRating() {
		return this.sellerRating;
	}

	public void setSellerRating(int sellerRating) {
		this.sellerRating = sellerRating;
	}

	public String getTrn() {
		return this.trn;
	}

	public void setTrn(String trn) {
		this.trn = trn;
	}

	public Pendinguser getPendinguser() {
		return this.pendinguser;
	}

	public void setPendinguser(Pendinguser pendinguser) {
		this.pendinguser = pendinguser;
	}

	public Registereduser getRegistereduser() {
		return this.registereduser;
	}

	public void setRegistereduser(Registereduser registereduser) {
		this.registereduser = registereduser;
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}