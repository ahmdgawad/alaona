package com.alaona.entities.users.messages;

import java.io.Serializable;
import javax.persistence.*;

import com.alaona.entities.users.Registereduser;

import java.util.Date;


/**
 * The persistent class for the conversation database table.
 * 
 */
@Entity
@NamedQuery(name="Conversation.findAll", query="SELECT c FROM Conversation c")
public class Conversation implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ConversationPK id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	private byte isRead;

	@Lob
	private String messageText;

	private String sender;

	private String subject;

	//bi-directional many-to-one association to Registereduser
	@MapsId("recipient")
	@ManyToOne
	@JoinColumn(name="Recipient")
	private Registereduser recipient;
	
	
	public Registereduser getRecipient() {
		return recipient;
	}

	public void setRecipient(Registereduser recipient) {
		this.recipient = recipient;
	}

	public Conversation() {
	}

	public ConversationPK getId() {
		return this.id;
	}

	public void setId(ConversationPK id) {
		this.id = id;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public byte getIsRead() {
		return this.isRead;
	}

	public void setIsRead(byte isRead) {
		this.isRead = isRead;
	}

	public String getMessageText() {
		return this.messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}