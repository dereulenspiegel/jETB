package de.jetb.interfaces;

import java.util.Iterator;

public interface Nachweisung {
	
	public void addMessage(Message message);
	public Iterator<Message> getMessages();
	public Iterator<Message> getReceivedMessages();
	public Iterator<Message> getSendMessages();
	public Message getMessageById(String messageId);
	public Iterator<Message> getMessagesByFernmelder(String fernmelder);
	public Iterator<Message> getMessagesBySender(String sender);
	public Iterator<Message> getMessagesbyReceiver(String receiver);
	public int getMessageCount();
	public int getSentMessageCount();
	public int getReceivedMessageCount();

}
