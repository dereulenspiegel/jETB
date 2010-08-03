package de.jetb.interfaces;

import java.util.List;

import de.jetb.enums.Priority;
import de.jetb.util.TacticalDate;

public interface Message {
	
	public String getBody();
	public String getSender();
	public String getReceiver();
	public List<String> getAdditionalReceivers();
	public TacticalDate getCreationTime();
	public List<RelayHeader> getRelayHeaders();
	public TacticalDate getReceivedTime();
	public String getAuthor();
	public Priority getPriority();
	public String getMessageID();
	public TacticalDate getDeliveredTime();
	
	public void setBody(String body);
	public void setSender(String sender);
	public void setReceiver(String receiver);
	public void addAdditionalReceiver(String receiver);
	public void removeAdditionalReceiver(String receiver);
	public void setDeliveredTime();
	public void setDeliveredTime(TacticalDate time);
	public void setAuthor(String author);
	public void setPriority(Priority prio);
	public void addRelayHeader(RelayHeader header);
	
	public String toXML();
}
