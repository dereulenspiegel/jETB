/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.connection;

import de.jetb.packet.IQPing;
import de.jetb.clientutils.Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jivesoftware.smack.LLPresence;
import org.jivesoftware.smack.LLPresenceListener;
import org.jivesoftware.smack.LLService;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

/**
 *
 * @author Till Klocke
 */
public class PeerManager implements LLPresenceListener{

    public final static String PRESENCE_FIELD_FUNCTION = "FUNCTION";
    
    public final static String FUNCTION_OPERATOR = "OPERATOR";
    public final static String FUNCTION_COMMANDER = "COMMANDER";
    public final static String FUNCTION_REVIEWER = "REVIEWER";

    public final static String OPTION_KEEP_ALIVE_INTERVAL="KEEP_ALIVE_INTERVAL";

    private LLService service;

    private Thread keepAliveThread;
    private KeepAlive keepAlive;

    private ArrayList<PresenceListener> listeners =
            new ArrayList<PresenceListener>();

    private String mEinsatzNummer;

    private ArrayList<LLPresence> operators = new ArrayList<LLPresence>();
    private ArrayList<LLPresence> commander = new ArrayList<LLPresence>();
    private ArrayList<LLPresence> reviewer = new ArrayList<LLPresence>();
    private ArrayList<LLPresence> others = new ArrayList<LLPresence>();

    PeerManager(LLService service){
        this.service = service;
        keepAlive = new KeepAlive();
        keepAliveThread = new Thread(keepAlive);
        service.addPacketListener(keepAlive, new PacketTypeFilter(IQPing.class));
    }

    public void presenceNew(LLPresence presence) {
        String function = presence.getValue(PRESENCE_FIELD_FUNCTION);
        if(function!=null){
            if(keepAliveThread!=null && !keepAliveThread.isAlive()){
                keepAliveThread.start();
            }
            if(function.equals(FUNCTION_OPERATOR)){
                operators.add(presence);
            }
            if(function.equals(FUNCTION_COMMANDER)){
                commander.add(presence);
            }
            if(function.equals(FUNCTION_REVIEWER)){
                reviewer.add(presence);
            }
        } else {
            others.add(presence);
        }

        for(PresenceListener l : listeners){
            l.presenceNew(presence);
        }
    }

    public void presenceRemove(LLPresence presence) {
        if(operators.contains(presence)){
            operators.remove(presence);
        }

        if(commander.contains(presence)){
            commander.remove(presence);
        }

        if(reviewer.contains(presence)){
            reviewer.remove(presence);
        }

        if(others.contains(presence)){
            others.remove(presence);
        }

        for(PresenceListener l : listeners){
            l.presenceRemoved(presence);
        }
    }

    public void addPresenceListener(PresenceListener listener){
        listeners.add(listener);
    }

    public void removePresenceListener(PresenceListener listener){
        listeners.remove(listener);
    }
    
    public void broadcastPacketToOperators(Packet packet) throws XMPPException{
        for(LLPresence p : operators){
            if(p.getMsg().equals(mEinsatzNummer)){
                packet.setTo(p.getServiceName());
                packet.setFrom(service.getLocalPresence().getServiceName());
                service.sendPacket(packet);
            }
        }
    }

    public void broadcastPacketToCommanders(Packet packet) throws XMPPException{
        for(LLPresence p : commander){
            if(p.getMsg().equals(mEinsatzNummer)){
                packet.setTo(p.getServiceName());
                packet.setFrom(service.getLocalPresence().getServiceName());
                service.sendPacket(packet);
            }
        }
    }

    public void broadcastPacketToReviewer(Packet packet) throws XMPPException{
        for(LLPresence p : reviewer){
            if(p.getMsg().equals(mEinsatzNummer)){
                packet.setTo(p.getServiceName());
                packet.setFrom(service.getLocalPresence().getServiceName());
                service.sendPacket(packet);
            }
        }
    }

    public void broadcastPacketToAll(Packet packet) throws XMPPException{
        broadcastPacketToOperators(packet);
        broadcastPacketToCommanders(packet);
        broadcastPacketToReviewer(packet);
    }

    public List<LLPresence> getCommanderPresences(){
        return commander;
    }

    public List<LLPresence> getOperatorPresences(){
        return operators;
    }

    public List<LLPresence> getReviewerPresences(){
        return reviewer;
    }

    public List<LLPresence> getOtherPresences(){
        return others;
    }

    public boolean isOperator(LLPresence presence){
        if(operators.contains(presence)){
            return true;
        }
        return false;
    }

    public boolean isCommander(LLPresence presence){
        if(commander.contains(presence)){
            return true;
        }
        return false;
    }

    public boolean isReviewer(LLPresence presence){
        if(reviewer.contains(presence)){
            return true;
        }

        return false;
    }

    public void setEinsatzNummer(String einsatzNummer){
        this.mEinsatzNummer = einsatzNummer;
    }

    private class KeepAlive implements Runnable,PacketListener{

        private HashMap<String,Long> lastKeepAlive =
                new HashMap<String,Long>();

        public void run() {
            int counter = 0;
            while(commander.size()>0 || operators.size()>0){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    //Ignore
                }
                counter++;
                if(counter==Configuration.getInt(OPTION_KEEP_ALIVE_INTERVAL, 50)){
                    System.out.println("Sending keep alive");
                    for(LLPresence p : operators){
                        sendKeepAlive(p.getServiceName());
                    }

                    for(LLPresence p : commander){
                        sendKeepAlive(p.getServiceName());
                    }
                }
            }
        }

        private void sendKeepAlive(String serviceName){
            if(lastKeepAlive.containsKey(serviceName) &&
                    lastKeepAlive.get(serviceName)<System.currentTimeMillis()+
                    (Configuration.getInt(OPTION_KEEP_ALIVE_INTERVAL, 50)*100)){
                try {
                    IQPing ping = new IQPing();
                    ping.setTo(serviceName);
                    ping.setFrom(service.getLocalPresence().getServiceName());
                    service.sendPacket(ping);
                    lastKeepAlive.put(serviceName, System.currentTimeMillis());
                } catch (XMPPException ex) {
                    //TODO: Handle Exception
                }
            }
        }

        public void processPacket(Packet packet) {
            if(packet instanceof IQPing){
                try {
                    IQPing ping = (IQPing) packet;
                    IQ pong = new IQ() {

                        @Override
                        public String getChildElementXML() {
                            return "";
                        }
                    };
                    pong.setFrom(service.getLocalPresence().getServiceName());
                    pong.setTo(ping.getFrom());
                    pong.setType(IQ.Type.RESULT);
                    pong.setPacketID(ping.getPacketID());
                    lastKeepAlive.put(ping.getFrom(), System.currentTimeMillis());
                    service.sendPacket(pong);
                } catch (XMPPException ex) {
                    //TODO: Handle Exception
                }
            }
        }
    }

    public static interface PresenceListener{
        public void presenceNew(LLPresence presence);
        public void presenceRemoved(LLPresence presence);
    }
}
