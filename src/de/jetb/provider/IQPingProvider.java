package de.jetb.provider;

import de.jetb.packet.IQPing;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

public class IQPingProvider implements IQProvider {

	public final static String NS="urn:xmpp:ping";

    static{
        ProviderManager.getInstance().addIQProvider("ping", NS, new IQPingProvider());
    }

	public IQ parseIQ(XmlPullParser arg0) throws Exception {
		IQPing result = new IQPing();
		return result;
	}

}