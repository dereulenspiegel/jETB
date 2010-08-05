/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb;

import de.jetb.enums.Roles;

/**
 * Eine Klasse die den momentanen Benutzer von jETB darstellt
 * @author Till Klocke
 */
public class User {

    private String firstName;
    private String lastName;
    private String location;
    private Roles role;

    public User(String firstName, String lastName, Roles role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }
}
