/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.operator;

import de.jetb.enums.Priority;
import de.jetb.interfaces.Message;
import de.jetb.interfaces.NetworkInterface;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author Till Klocke
 */
public class IncomingMessageHandler extends AbstractListModel
        implements NetworkInterface.IncomingMessageListener{

    //Einfachnachrichten
    private ArrayList<Message> normalMessages = new ArrayList<Message>();
    //Sofortnachrichten
    private ArrayList<Message> immediateMessages = new ArrayList<Message>();
    //Blitznachrichten
    private ArrayList<Message> urgentMessages = new ArrayList<Message>();
    //Staatsnot
    private ArrayList<Message> staatsNot = new ArrayList<Message>();

    public int getSize() {
        return normalMessages.size() + immediateMessages.size() +
                urgentMessages.size() + staatsNot.size();
    }

    public Object getElementAt(int index) {
        if(index>staatsNot.size()-1){
            if(index-staatsNot.size()>urgentMessages.size()-1){
                if(index-staatsNot.size()-urgentMessages.size()>
                        immediateMessages.size()){
                    if(index-staatsNot.size()-urgentMessages.size()-
                            immediateMessages.size()>normalMessages.size()){
                        //Hä?
                    } else {
                        return normalMessages.get(index - staatsNot.size() -
                                urgentMessages.size()-urgentMessages.size());
                    }

                } else {
                    return immediateMessages.get(index-staatsNot.size()-
                            urgentMessages.size());
                }
            } else {
                return urgentMessages.get(index-staatsNot.size());
            }
        } else {
            return staatsNot.get(index);
        }

        return null;
    }

    public void addMessage(Message message){
        if(message.getPriority() == Priority.EINFACH){
            normalMessages.add(message);
        }
        if(message.getPriority() == Priority.SOFORT){
            immediateMessages.add(message);
        }
        if(message.getPriority() == Priority.BLITZ){
            urgentMessages.add(message);
        }
        if(message.getPriority() == Priority.STAATSNOT){
            staatsNot.add(message);
        }
    }

    public void removeMessage(Message message){
        if(normalMessages.contains(message)){
            normalMessages.remove(message);
        }
        if(immediateMessages.contains(message)){
            immediateMessages.remove(message);
        }
        if(urgentMessages.contains(message)){
            urgentMessages.remove(message);
        }
        if(staatsNot.contains(message)){
            staatsNot.remove(message);
        }
    }

    public void messageReceived(Message message) {
        addMessage(message);
    }

}
