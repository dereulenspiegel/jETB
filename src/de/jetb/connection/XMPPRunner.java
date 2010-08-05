/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.connection;

import de.jetb.Application;
import de.jetb.enums.Roles;
import de.jetb.interfaces.Message;
import de.jetb.interfaces.NetworkInterface;
import de.jetb.packet.MessagePacket;
import de.jetb.util.Utils;
import java.util.ArrayList;
import org.jivesoftware.smack.JmDNSService;
import org.jivesoftware.smack.LLPresence;
import org.jivesoftware.smack.LLService;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;

/**
 *
 * @author Till Klocke
 */
public class XMPPRunner extends NetworkInterface implements Runnable, PacketListener{
    
    private LLService llService;
    private LLPresence presence;
    private boolean run=true;
    private Roles role;

    private String einsatzNummer;

    private PeerManager peerManager;

    private final ArrayList<NetworkInterface.IncomingMessageListener> incomingMessageListener =
            new ArrayList<NetworkInterface.IncomingMessageListener>();

    private final ArrayList<NetworkInterface.OutgoingMessageListener> outgoingMessageListener =
            new ArrayList<NetworkInterface.OutgoingMessageListener>();

    private final ArrayList<NetworkInterface.ArchivedMessageListener> archivedMessageListener =
            new ArrayList<NetworkInterface.ArchivedMessageListener>();

    private final ArrayList<NetworkInterface.MessageOpenendListener> messageOpenendListener =
            new ArrayList<NetworkInterface.MessageOpenendListener>();

    public void setPresence(LLPresence presence){
        this.presence = presence;
    }

    public void init() throws XMPPException{
        llService = JmDNSService.create(presence);
        llService.addPacketListener(this, null);
    }

    public void init(LLPresence presence) throws XMPPException{
        setPresence(presence);
        init();
    }

    public void init(Roles role, String einsatznummer){
        this.einsatzNummer = einsatznummer;
        this.role = role;

        //presence = new LLPresence(Utils.generateServiceName(Application.getInstance().getUser().))
    }

    public void run() {
        while(run){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //Ignore
            }
        }
    }

    public void shutdown(){
        run = false;
        try{
            llService.close();
        } catch(Exception e){
            System.out.println("An error occured");
            e.printStackTrace();
        }
        llService = null;
    }

    public LLService getService(){
        return llService;
    }

    public void setEinsatzNummer(String einsatzNummer){
        this.einsatzNummer = einsatzNummer;
    }

    public PeerManager getPeerManager(){
        if(peerManager==null){
            peerManager = new PeerManager(llService);
        }
        return peerManager;
    }

    public void sendMessage(Message message) {
        org.jivesoftware.smack.packet.Message mp =
                new org.jivesoftware.smack.packet.Message();

        mp.addExtension(new MessagePacket(message));
        try{
            if(role == Roles.OPERATOR){
                if(peerManager.getReviewerPresences().size()>0){
                    peerManager.broadcastPacketToReviewer(mp);
                } else {
                    peerManager.broadcastPacketToCommanders(mp);
                }
            }
            if(role == Roles.COMMANDER){
                peerManager.broadcastPacketToOperators(mp);
            }
            if(role == Roles.REVIEWER){
                peerManager.broadcastPacketToCommanders(mp);
            }
            notifyOutgoingMessageListener(message);
        } catch (XMPPException e) {
            //TODO: Handle this unsent Message, reqeue it or something like that
        }
    }

    public void addIncomingMessageListener(IncomingMessageListener listener) {
        if(listener!=null){
            synchronized(incomingMessageListener){
                incomingMessageListener.add(listener);
            }
        }
    }

    private void notifyIncomingMessageListener(Message message){
        synchronized(incomingMessageListener){
            for(IncomingMessageListener l : incomingMessageListener){
                l.messageReceived(message);
            }
        }
    }

    public void addOutgoingMessageListener(OutgoingMessageListener listener) {
        if(listener!=null){
            synchronized(outgoingMessageListener){
                outgoingMessageListener.add(listener);
            }
        }
    }

    private void notifyOutgoingMessageListener(Message message){
        synchronized(outgoingMessageListener){
            for(OutgoingMessageListener l : outgoingMessageListener){
                l.messageSent(message);
            }
        }
    }

    public void addArchivedMessageListener(ArchivedMessageListener listener) {
        if(listener!=null){
            synchronized(archivedMessageListener){
                archivedMessageListener.add(listener);
            }
        }
    }

    public void notifyOtherOperatorsAboutOpeningMessage(String messageId) {
        //TODO: Implement an IQ-Mechanism to notify other operators
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeIncomingMessageListener(IncomingMessageListener listener) {
        if(listener!=null){
            synchronized(incomingMessageListener){
                incomingMessageListener.remove(listener);
            }
        }
    }

    @Override
    public void removeOutgoingMessageListener(OutgoingMessageListener listener) {
        if(listener!=null){
            synchronized(outgoingMessageListener){
                outgoingMessageListener.remove(listener);
            }
        }
    }

    @Override
    public void removeArchivedMessageListener(ArchivedMessageListener listener) {
        if(listener!=null){
            synchronized(archivedMessageListener){
                archivedMessageListener.remove(listener);
            }
        }
    }

    @Override
    public void addMessageOpenendListener(MessageOpenendListener listener) {
        if(listener!=null){
            synchronized(messageOpenendListener){
                messageOpenendListener.add(listener);
            }
        }
    }

    @Override
    public void removeMessageOpenendListener(MessageOpenendListener listener) {
        if(listener!=null){
            synchronized(messageOpenendListener){
                messageOpenendListener.remove(listener);
            }
        }
    }

    @Override
    public void archiveMessage(Message message) {
        //TODO: Mechanism to encapsulate archived messages differently and
        //broadcast them to all
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void processPacket(Packet packet) {
        //We have a message packet, let's see if we have a radio message
        if(packet instanceof org.jivesoftware.smack.packet.Message){
            PacketExtension ext =
                    packet.getExtension(MessagePacket.ELEMENT_NAME,
                    MessagePacket.NS);
            if(ext instanceof MessagePacket){
                Message m = ((MessagePacket)ext).getMessage();
                notifyIncomingMessageListener(m);
            }
        }
        //Implement methods for other packets in the future
    }

}
