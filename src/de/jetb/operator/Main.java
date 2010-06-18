/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.operator;

import de.jetb.clientutils.Utils;
import java.awt.Component;

/**
 *
 * @author Till Klocke
 */
public class Main {

    public static void main(String[] argv){
        Utils.setSystemLookAndFeel();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Component login = new Login();
                login.setVisible(true);
                Utils.centerComponent(login);
            }
        });
    }

}
