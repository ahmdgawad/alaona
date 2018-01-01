package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the bidderrating database table.
 * 
 */
@Entity
@NamedQuery(name="Bidderrating.findAll", query="SELECT b FROM Bidderrating b")
public class Bidderrating implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BidderratingPK id;

	private float rate;

	//bi-directional many-to-one association to Registereduser
	@MapsId("username")
	@ManyToOne
	@JoinColumn(name="Username")
	private Registereduser registereduser;

	public Bidderrating() {
	}

	public BidderratingPK getId() {
		return this.id;
	}

	public void setId(BidderratingPK id) {
		this.id = id;
	}

	public float getRate() {
		return this.rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public Registereduser getRegistereduser() {
		return this.registereduser;
	}

	public void setRegistereduser(Registereduser registereduser) {
		this.registereduser = registereduser;
	}

}