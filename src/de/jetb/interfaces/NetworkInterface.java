/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jetb.interfaces;

import de.jetb.enums.Roles;

/**
 * Ein Interface für Netzwerkmodelle. Jeder Netzwerkstack für jETB muss dieses
 * Interface implementieren.
 * @author Till Klocke
 */
public abstract class NetworkInterface {

    public abstract void init(Roles role, String einsatznummer);

    /**
     * Sendet eine Nachricht und verteilt diese an alle nötigen Stellen. Ist man
     * in der Rolle des Funkers werden die Nachrichten entweder an alle
     * Führungspersonen oder an alle Sichter gesendet.
     * Ist man in der Rolle einer Führungsperson wird die Nachricht an alle
     * Fernmelder gesendet und ist man in der Rolle eines Sichters wird die
     * Nachricht an alle Führungspersonen gesendet
     * @param message Die zu sendende Nachricht
     */
    public abstract void sendMessage(Message message);

    /**
     * Diese Funktion wird nur in der Rolle des Fernmelders benötigt. Sobald eine
     * Nachricht zur weiterbearbeitung geöffnet wurde, wird diese Funktion
     * aufgerufen um allen anderen Fernmeldern mitzuteilen, dass die Nachricht
     * bearbeitet wird.
     * @param messageId Die einzigartige ID der Nachricht
     */
    public abstract void notifyOtherOperatorsAboutOpeningMessage(String messageId);
    /**
     * Eine Listener für eingehende Nachrichten hinzufügen. Damit sind über das
     * Netzwerk eingehende Nachrichten gemeint und nicht in der Fernmeldestelle
     * eingehende Nachrichten.
     * @param listener
     */
    public abstract void addIncomingMessageListener(IncomingMessageListener listener);
    /**
     * Einen Listener für über das Netzwerk ausgehende Nachrichten hinzufügen.
     * @param listener
     */
    public abstract void addOutgoingMessageListener(OutgoingMessageListener listener);
    /**
     * Ist im Netzwerk eine Nachricht bereit archiviert zu werden weil sie
     * vollständig bearbeitet wurde, wird sie an alle verschickt. Mit dieser
     * Methode kann ein Listener um auf solche Ereignisse zu lauschen hinzugefügt
     * werden
     * @param listener
     */
    public abstract void addArchivedMessageListener(ArchivedMessageListener listener);
    /**
     * Diese Methode erlaubt es einen Listener hinzuzufügen der auf Nachrichten
     * hört die signalisieren, dass eine bestimme Nachricht bereits von einem
     * anderen Fernmelder bearbeitet wird.
     * @param listener
     */
    public abstract void addMessageOpenendListener(MessageOpenendListener listener);

    public abstract void removeIncomingMessageListener(IncomingMessageListener listener);
    public abstract void removeOutgoingMessageListener(OutgoingMessageListener listener);
    public abstract void removeArchivedMessageListener(ArchivedMessageListener listener);
    public abstract void removeMessageOpenendListener(MessageOpenendListener listener);

    /**
     * Mit dieser Funktionen werden Nachrichten der automatischen Nachweisung
     * übergeben. Sie sollen nicht nur lokal gespeichert werden, sondern an alle
     * Clients gesendet werden, damit diese ebenfalls eine lokale Kopie speichern
     * können.
     * @param message
     */
    public abstract void archiveMessage(Message message);

    public static interface IncomingMessageListener{
        public void messageReceived(Message message);
    }

    public static interface OutgoingMessageListener{
        public void messageSent(Message message);
    }

    public static interface ArchivedMessageListener{
        //TODO: Create a class for archived messages
        public void archivedMessageReceived();
    }

    public static interface MessageOpenendListener{
        public void messageOpenedByOtherOperator(String messageId);
    }

}
