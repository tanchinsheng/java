package it.txt.afs.packet.utils;

import it.txt.afs.packet.PacketList;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-feb-2006
 * Time: 16.32.06
 */
public class PacketsDbSearchResult {

    private Hashtable fieldsValues;
    private PacketList packetList;

    public PacketsDbSearchResult(Hashtable fieldsValues, PacketList queryResult) {
        this.fieldsValues = fieldsValues;
        this.packetList = queryResult;
    }

    public Hashtable getFieldsValues() {
        return fieldsValues;
    }

    public PacketList getPacketList() {
        return packetList;
    }
}
