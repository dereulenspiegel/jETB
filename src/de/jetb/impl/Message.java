package de.jetb.impl;

import java.util.ArrayList;
import java.util.List;

import de.jetb.enums.Priority;
import de.jetb.interfaces.RelayHeader;
import de.jetb.util.TacticalDate;
import de.jetb.util.Utils;

public class Message implements de.jetb.interfaces.Message {
	
	private ArrayList<String> additionalReceiver = new ArrayList<String>();
	private ArrayList<RelayHeader> relayHeader = new ArrayList<RelayHeader>();
	
	private String author;
	private String body;
	private TacticalDate creationTime;
	private String messageID;
	private Priority priority;
	private TacticalDate receivedTime;
	private String receiver;
	private String sender;
	private TacticalDate deliveredTime;
	
	public Message(String sender, String receiver, String body){
		creationTime = new TacticalDate();
		this.sender = sender;
		this.receiver = receiver;
		this.body = body;
		generateMessageID();
	}
	
	public Message(String sender, String receiver, String body, TacticalDate creationTime){
		this(sender,receiver,body);
		this.creationTime = creationTime;
	}

	@Override
	public void addAdditionalReceiver(String receiver) {
		if(receiver!=null){
			additionalReceiver.add(receiver);
		}

	}

	@Override
	public void addRelayHeader(RelayHeader header) {
		if(header!=null){
			relayHeader.add(header);
		}

	}

	@Override
	public List<String> getAdditionalReceivers() {
		return additionalReceiver;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public TacticalDate getCreationTime() {
		return creationTime;
	}

	@Override
	public String getMessageID() {
		return messageID;
	}

	@Override
	public Priority getPriority() {
		return priority;
	}

	@Override
	public TacticalDate getReceivedTime() {
		return receivedTime;
	}

	@Override
	public String getReceiver() {
		return receiver;
	}

	@Override
	public List<RelayHeader> getRelayHeaders() {
		return relayHeader;
	}

	@Override
	public String getSender() {
		return sender;
	}

	@Override
	public void removeAdditionalReceiver(String receiver) {
		if(receiver!=null && additionalReceiver.contains(receiver)){
			additionalReceiver.remove(receiver);
		}

	}

	@Override
	public void setAuthor(String author) {
		this.author = author;

	}

	@Override
	public void setBody(String body) {
		this.body = body;

	}

	@Override
	public void setDeliveredTime() {
		this.deliveredTime = new TacticalDate();

	}

	@Override
	public void setDeliveredTime(TacticalDate time) {
		this.deliveredTime = time;

	}

	@Override
	public void setPriority(Priority prio) {
		this.priority = prio;

	}

	@Override
	public void setReceiver(String receiver) {
		this.receiver = receiver;

	}

	@Override
	public void setSender(String sender) {
		this.sender = sender;

	}

	@Override
	public String toXML() {
		//TODO: Generate valid XML to attach to XMPP-Packets
		return null;
	}
	
	private void generateMessageID(){
		String input = sender+"/"+receiver+"/"+creationTime.getTime();
		try {
			messageID = Utils.SHA1(input);
		} catch (Exception e) {
			messageID = input;
		}
	}

	@Override
	public TacticalDate getDeliveredTime() {
		return deliveredTime;
	}

}
