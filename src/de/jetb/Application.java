/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb;

/**
 *
 * @author Till Klocke
 */
public class Application {

    private static Application instance;

    private String einsatznummer;
    private User benutzer;

    public void setEinsatznummer(String einsatznummer){
        this.einsatznummer = einsatznummer;
    }

    public String getEinsatznummer(){
        return einsatznummer;
    }

    public void setUser(User benutzer){
        this.benutzer = benutzer;
    }

    public User getUser(){
        return benutzer;
    }


    public static Application getInstance(){
        if(instance == null){
            instance = new Application();
        }
        return instance;
    }

}
