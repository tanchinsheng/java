package it.txt.afs.packet;

import it.txt.general.utils.GeneralStringUtils;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 18-gen-2006
 * Time: 13.35.37
 * this class represent a list of packets
 */

public class PacketList {
    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private Hashtable packetList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the packet list
    private Enumeration packetEnum = null;

    /**
     * default contructor
     */
    public PacketList() {
        packetList = new Hashtable();
    }

    /**
     * add packet to the list
     *
     * @param packet the packet to be added
     * @return KO_RESULT if the given packet is null or do not have an id, OK_RESULT otherwirse
     */
    public int addPacket(Packet packet) {
        if (packet == null || GeneralStringUtils.isEmptyString(packet.getId()))
            return KO_RESULT;
        else {
            packetList.put(packet.getId(), packet);
            packetEnum = null;
        }
        return OK_RESULT;
    }

    /**
     * remove a packet from the packet list
     *
     * @param packetKey the key that identifies a packet in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given packet key is null or empty)
     */
    public int removePacket(String packetKey) {
        if (!GeneralStringUtils.isEmptyString(packetKey)) {
            packetList.remove(packetKey);
            packetEnum = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a packet from the packet list
     *
     * @param packet the packet in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given packet key is null or empty)
     */
    public int removePacket(Packet packet) {
        if (packet != null && !GeneralStringUtils.isEmptyString(packet.getId())) {
            return this.removePacket(packet.getId());
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the packet identified by packetId is present in the list, false otherwise
     *
     * @param packetId the id of the packet to look for.
     * @return true if the packet identified by packetId is present in the list, false otherwise
     */
    public boolean containsPacket(String packetId) {
        if (!GeneralStringUtils.isEmptyString(packetId) && packetList != null) {
            return packetList.containsKey(packetId);
        }
        return false;
    }

    /**
     * true if the given packet is present in the list, false otherwise
     *
     * @param packet the packet to look for.
     * @return true if the given packet is present in the list, false otherwise
     */
    public boolean containsPacket(Packet packet) {
        if (packet != null && packetList != null) {
            return packetList.containsValue(packet);
        }
        return false;
    }

    /**
     * return the packet identified by the given packet id
     *
     * @param packetId
     * @return the packet identified by the given packet id
     */
    public Packet getPacket(String packetId) {
        if (packetList != null && !GeneralStringUtils.isEmptyString(packetId)) {
            return (Packet) packetList.get(packetId);
        }
        return null;
    }

    /**
     * Tests if this packetList contains more elements
     *
     * @return true if and only if this packetList contains at least one more packet to provide; false otherwise.
     */
    public boolean hasMorePackets() {
        if (packetEnum == null) {
            packetEnum = packetList.elements();
        }
        return packetEnum.hasMoreElements();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next packet of this packet list.
     */
    public Packet nextPacket() {
        if (packetEnum == null) {
            packetEnum = packetList.elements();
        }
        return (Packet) packetEnum.nextElement();
    }

    /**
     * @return Returns the number of packets in this packetList.
     */
    public int size() {
        return packetList.size();
    }

    /**
     * @return Tests if this packetList contains no packets.
     */
    public boolean isEmpty() {
        return packetList.isEmpty();
    }
}