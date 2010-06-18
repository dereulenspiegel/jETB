/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.clientutils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.UIManager;

/**
 *
 * @author Till Klocke
 */
public class Utils {

        /**
     * Centers the given component on the screen
     * @param comp The Component to be centered
     */
    public static void centerComponent(final Component comp){
        if(comp!=null){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension compSize = comp.getSize();

            if(compSize.height>screenSize.height){
                compSize.height = screenSize.height;
            }
            if(compSize.width>screenSize.width){
                compSize.width = screenSize.width;
            }

            comp.setLocation((screenSize.width-compSize.width)/2, (screenSize.height-compSize.height)/2);
        }
    }

    public static void setSystemLookAndFeel(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
