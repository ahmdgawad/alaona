package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the pendinguser database table.
 * 
 */
@Entity
@NamedQuery(name="Pendinguser.findAll", query="SELECT p FROM Pendinguser p")
public class Pendinguser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	//bi-directional one-to-one association to User
	@OneToOne
	@PrimaryKeyJoinColumn(name="Username")
	private User user;

	public Pendinguser() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}