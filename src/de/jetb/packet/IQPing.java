package de.jetb.packet;

import org.jivesoftware.smack.packet.IQ;

public class IQPing extends IQ {

	public IQPing(){
		this.setType(IQ.Type.GET);
	}

	@Override
	public String getChildElementXML() {
		return "<ping xmlns='urn:xmpp:ping'/>";
	}

}
