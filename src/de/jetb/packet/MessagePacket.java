/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.packet;

import de.jetb.interfaces.RelayHeader;
import de.jetb.interfaces.Message;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

/**
 * This class implements an extension to the message packet in which
 * radio messages can be transported
 * @author Till Klocke
 */
public class MessagePacket implements PacketExtension{

    public final static String NS = "http://jetb.de/message";
    public final static String ELEMENT_NAME = "x";
    
    private Message message;

    public MessagePacket(Message message){
        this.message = message;
    }

    public Message getMessage(){
        return this.message;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NS;
    }

    public String toXML() {
        StringBuilder out = new StringBuilder();
        out.append("<");
        out.append(ELEMENT_NAME);
        out.append(" xmlns='");
        out.append(NS);
        out.append("'>");

        out.append("<message ");
        if(message.getSender()!=null){
            out.append("sender='");
            out.append(StringUtils.escapeForXML(message.getSender()));
            out.append("' ");
        }
        if(message.getReceiver()!=null){
            out.append("receiver='");
            out.append(StringUtils.escapeForXML(message.getReceiver()));
            out.append("' ");
        }

        out.append("priority='");
        out.append(message.getPriority().toString());
        out.append("' ");

        out.append("messageid='");
        out.append(StringUtils.escapeForXML(message.getMessageID()));
        out.append("'>");

        if(message.getAuthor()!=null){
            out.append("<author>");
            out.append(StringUtils.escapeForXML(message.getAuthor()));
            out.append("</author>");
        }

        out.append("<creationtime>");
        out.append(message.getCreationTime().getTime());
        out.append("</creationtime>");

        if(message.getDeliveredTime()!=null){
            out.append("<deliveredtime>");
            out.append(message.getDeliveredTime().getTime());
            out.append("</deliveredtime>");
        }

        out.append("<body>");
        out.append(StringUtils.escapeForXML(message.getBody()));
        out.append("</body>");

        for(String s : message.getAdditionalReceivers()){
            out.append("<receiver>");
            out.append(StringUtils.escapeForXML(message.getReceiver()));
            out.append("</receiver>");
        }

        for(RelayHeader h : message.getRelayHeaders()){
            out.append(relayHeaderToXML(h));
        }

        out.append("</message>");

        out.append("</");
        out.append(ELEMENT_NAME);
        out.append(">");

        return out.toString();
    }

    private String relayHeaderToXML(RelayHeader header){
        StringBuilder out = new StringBuilder();
        out.append("<relay received='");
        out.append(header.isReceived()?"true":"false");
        out.append("'>");

        out.append("<media>");
        out.append(StringUtils.escapeForXML(header.getMedia().toString()));
        out.append("</media>");

        out.append("<operator>");
        out.append(StringUtils.escapeForXML(header.getRadioOperator()));
        out.append("</operator>");

        out.append("<time>");
        out.append(header.getTimeOfEvent().getTime());
        out.append("</time>");

        out.append("</relay>");

        return out.toString();
    }

    public static class Provider implements PacketExtensionProvider{

        public PacketExtension parseExtension(XmlPullParser xpp) throws Exception {
            boolean done = false;

            while(!done){


                if(xpp.getEventType() == XmlPullParser.END_TAG &&
                        xpp.getName().equals(ELEMENT_NAME)){
                        done = true;
                } else {
                    xpp.next();
                }
            }

            return null;
        }
        
    }

}
