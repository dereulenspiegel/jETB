package de.jetb.interfaces;

public interface RadioStation {
	
	public String getFunkName();
	public String getDescritpion();
	public String getCoordinates();
	
	public void setFunkName(String name);
	public void setDescription(String description);
	public void setCoordinates(String coords);
	
	public String toXML();
}
