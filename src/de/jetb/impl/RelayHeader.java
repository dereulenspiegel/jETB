package de.jetb.impl;

import de.jetb.enums.Media;
import de.jetb.util.TacticalDate;

public class RelayHeader implements de.jetb.interfaces.RelayHeader {
	
	private Media media;
	private String radioOperator;
	private TacticalDate timeOfEvent;
	private boolean isReceived=false;
	
	public RelayHeader(String operator, Media media, boolean isReceived){
		this.radioOperator = operator;
		this.media = media;
		this.timeOfEvent = new TacticalDate();
		this.isReceived = isReceived;
	}

	@Override
	public Media getMedia() {
		return media;
	}

	@Override
	public String getRadioOperator() {
		return radioOperator;
	}

	@Override
	public TacticalDate getTimeOfEvent() {
		return timeOfEvent;
	}

	@Override
	public boolean isReceived() {
		return isReceived;
	}

	@Override
	public void setMedia(Media media) {
		this.media = media;

	}

	@Override
	public void setRadioOperator(String operator) {
		this.radioOperator = operator;

	}

	@Override
	public void setReceived() {
		this.isReceived = true;

	}

	@Override
	public void setSent() {
		this.isReceived = false;

	}

	@Override
	public void setTimeOfEvent(TacticalDate date) {
		this.timeOfEvent = date;
	}

}
