package it.txt.afs.packet.utils;

import it.txt.afs.packet.PacketList;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 7-feb-2006
 * Time: 16.49.59
 * To change this template use File | Settings | File Templates.
 */
public class PacketQueryResult {
    private boolean result = false;
    private PacketList packetlist = null;

    public PacketQueryResult(boolean isResultPresent, PacketList packetlist) {
        this.result = isResultPresent;
        this.packetlist = packetlist;
    }

    public boolean isResultPresent() {
        return result;
    }

    public PacketList getPacketlist() {
        return packetlist;
    }

}
