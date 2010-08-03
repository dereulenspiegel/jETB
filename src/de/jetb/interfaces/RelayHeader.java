package de.jetb.interfaces;

import de.jetb.enums.Media;
import de.jetb.util.TacticalDate;

public interface RelayHeader {
	
	public Media getMedia();
	public boolean isReceived();
	public String getRadioOperator();
	public TacticalDate getTimeOfEvent();
	
	public void setMedia(Media media);
	public void setReceived();
	public void setSent();
	public void setRadioOperator(String operator);
	public void setTimeOfEvent(TacticalDate date);
	
	public String toXML();

}
