/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.packet;

import de.jetb.enums.Media;
import de.jetb.enums.Priority;
import de.jetb.interfaces.RelayHeader;
import de.jetb.interfaces.Message;
import de.jetb.util.TacticalDate;
import java.util.ArrayList;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
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
    
    private static Provider provider = new Provider();

    static{
        ProviderManager.getInstance().addExtensionProvider(ELEMENT_NAME, NS, provider);
    }
    
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

            String sender = null;
            String receiver = null;
            String body = null;
            String messageId = null;
            Priority prio = null;
            String author = null;
            TacticalDate creationTime = null;
            TacticalDate deliveredTime = null;
            ArrayList<String> additionalReceiver = new ArrayList<String>();
            ArrayList<RelayHeader> relayHeader = new ArrayList<RelayHeader>();

            while(!done){

                if(xpp.getEventType() == XmlPullParser.START_TAG){
                    if(xpp.getName().equals("message")){
                        sender = xpp.getAttributeValue(null, "sender");
                        receiver = xpp.getAttributeValue(null, "receiver");
                        messageId = xpp.getAttributeValue(null, "messageid");
                        prio = Priority.valueOf(
                                xpp.getAttributeValue(null, "priority"));
                    }
                    if(xpp.getName().equals("author")){
                        author = xpp.getText();
                    }

                    if(xpp.getName().equals("creationtime")){
                        creationTime =
                                new TacticalDate(Long.parseLong(xpp.getText()));
                    }

                    if(xpp.getName().endsWith("deliveredtime")){
                        deliveredTime =
                                new TacticalDate(Long.parseLong(xpp.getText()));
                    }

                    if(xpp.getName().equals("body")){
                        body = xpp.getText();
                    }

                    if(xpp.getName().equals("receiver")){
                        additionalReceiver.add(xpp.getText());
                    }

                    if(xpp.getName().equals("relay")){
                        RelayHeader header = parseRelayHeader(xpp);
                        if(header!=null){
                            relayHeader.add(header);
                        }
                    }
                }

                if(xpp.getEventType() == XmlPullParser.END_TAG &&
                        xpp.getName().equals(ELEMENT_NAME)){
                        done = true;
                } else {
                    xpp.next();
                }
            }

            Message message =
                    new de.jetb.impl.Message(sender, receiver, body, creationTime, messageId);

            message.setAuthor(author);
            message.setDeliveredTime(deliveredTime);
            message.setPriority(prio);
            for(String s : additionalReceiver){
                message.addAdditionalReceiver(s);
            }

            for(RelayHeader r : relayHeader){
                message.addRelayHeader(r);
            }

            MessagePacket extension = new MessagePacket(message);

            return extension;
        }

        private RelayHeader parseRelayHeader(XmlPullParser xpp) throws Exception{
            boolean done = false;

            boolean received = true;
            Media media = null;
            String operator = null;
            TacticalDate time = null;
            
            if(xpp.getName().equals("relay")){
                received = xpp.getAttributeValue(null,"received").equals("true");

                while(!done){
                    if(xpp.getEventType() == XmlPullParser.START_TAG){
                        if(xpp.getName().equals("media")){
                            media = Media.valueOf(xpp.getText());
                        }

                        if(xpp.getName().equals("operator")){
                            operator = xpp.getText();
                        }

                        if(xpp.getName().equals("time")){
                            time = new TacticalDate(Long.parseLong(xpp.getText()));
                        }
                    }

                    if(xpp.getEventType() == XmlPullParser.END_TAG &&
                            xpp.getName().equals("relay")){
                        done = true;
                    }
                }

                RelayHeader ret = new de.jetb.impl.RelayHeader(operator, media, received);
                ret.setTimeOfEvent(time);

                return ret;
            }

            return null;
        }
        
    }

}
