/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.clientutils;

import java.util.HashMap;

/**
 *
 * @author Till Klocke
 */
public class Configuration {

    private static HashMap<String,String> options = 
            new HashMap<String,String>();

    public static int getInt(String name, int def){
        if(options.containsKey(name)){
            try{
                int retVal = Integer.parseInt(options.get(name));
                return retVal;
            } catch (Exception e){
                return def;
            }
        }
        return def;
    }

    public static String getString(String name, String def){
        if(options.containsKey(name)){
            return options.get(name);
        }

        return def;
    }

}
