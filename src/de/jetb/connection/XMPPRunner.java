/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.connection;

import org.jivesoftware.smack.JmDNSService;
import org.jivesoftware.smack.LLPresence;
import org.jivesoftware.smack.LLService;
import org.jivesoftware.smack.XMPPException;

/**
 *
 * @author Till Klocke
 */
public class XMPPRunner implements Runnable{
    
    private LLService llService;
    private LLPresence presence;
    private boolean run=true;
    
    private String einsatzNummer;

    private PeerManager peerManager;

    public void setPresence(LLPresence presence){
        this.presence = presence;
    }

    public void init() throws XMPPException{
        llService = JmDNSService.create(presence);
    }

    public void init(LLPresence presence) throws XMPPException{
        setPresence(presence);
        init();
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

}
