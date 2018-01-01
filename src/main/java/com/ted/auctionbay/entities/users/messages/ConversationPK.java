package com.ted.auctionbay.entities.users.messages;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the conversation database table.
 * 
 */
@Embeddable
public class ConversationPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int conversationID;

	@Column(insertable=false, updatable=false)
	private String recipient;

	public ConversationPK() {
	}
	public int getConversationID() {
		return this.conversationID;
	}
	public void setConversationID(int conversationID) {
		this.conversationID = conversationID;
	}
	public String getRecipient() {
		return this.recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ConversationPK)) {
			return false;
		}
		ConversationPK castOther = (ConversationPK)other;
		return 
			(this.conversationID == castOther.conversationID)
			&& this.recipient.equals(castOther.recipient);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.conversationID;
		hash = hash * prime + this.recipient.hashCode();
		
		return hash;
	}
}