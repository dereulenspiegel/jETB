package de.jetb.interfaces;

import java.util.List;

import de.jetb.enums.Priority;
import de.jetb.util.TacticalDate;
/**
 * Dieses Interface soll die grundlage für Nachrichten innerhalb und zwischen
 * Fernmeldestellen sein. Genaugenommen soll dieses Interface Vierfachvordrucke
 * weitesgehend repräsentieren. Der einzige Unterschied zu den Vordrucken
 * in Papierform ist, dass hier mehrere Meldeköpfe (RelayHeader) existieren können.
 * @author Till Klocke
 */
public interface Message {

        /**
         * Liefert den eigentlich Inhalt der Nachricht zurück
         * @return String
         */
	public String getBody();
        /**
         * Liefert den Absender zurück
         * @return String
         */
	public String getSender();
        /**
         * Liefert den Empfänger zurück
         * @return String
         */
	public String getReceiver();
        /**
         * Liefert eine Liste mit weiteren Empfängern, die z.B. durch den Sichter
         * bestimmt wurden zurück
         * @return List<String>
         */
	public List<String> getAdditionalReceivers();
        /**
         * Gibt die Abfassungszeit zurück
         * @return TacticalDate
         */
	public TacticalDate getCreationTime();
        /**
         * Liefert eine Liste mit RelayHeadern (Meldeköpfen) zurück
         * @return List<RelayHeader>
         */
	public List<RelayHeader> getRelayHeaders();
        /**
         * Gibt den Zeitpunkt an zu dem die Nachricht vom Fernmelder empfangen
         * wurde.
         * @return TacticalDate
         */
	public TacticalDate getReceivedTime();
        /**
         * Liefert den ursprünglichen Autor zurück, da dieser nicht identisch
         * mit dem Absender sein muss.
         * @return String
         */
	public String getAuthor();
        /**
         * Liefert die Vorrangstufe zurück
         * @return Vorrangstufe
         */
	public Priority getPriority();
        /**
         * Liefert eine eindeutige ID der Nachricht zurück. Jede Implementation
         * dieses Interfaces hat dafür Sorge zu tragen, dass diese ID dauerhaft
         * eindeutig ist.
         * @return ID
         */
	public String getMessageID();
        /**
         * Liefert die Quittungszeit zurück
         * @return TacticalDate
         */
	public TacticalDate getDeliveredTime();
	
	public void setBody(String body);
	public void setSender(String sender);
	public void setReceiver(String receiver);
	public void addAdditionalReceiver(String receiver);
	public void removeAdditionalReceiver(String receiver);
	public void setDeliveredTime();
	public void setDeliveredTime(TacticalDate time);
	public void setAuthor(String author);
	public void setPriority(Priority prio);
	public void addRelayHeader(RelayHeader header);
	
	public String toXML();
}
