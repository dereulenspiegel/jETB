package de.jetb.interfaces;

import de.jetb.enums.Media;
import de.jetb.util.TacticalDate;

/**
 * RelayHeader ersetzen den Meldekopf bei Nachrichten. Ein RelayHeader enthält
 * die gleichen Informationen wie ein Meldekopf, allerdings darf ein RelayHeader
 * mehrfach in einer Nachricht vorkommen.
 * @author Till Klocke
 */
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

}
