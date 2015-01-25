package it.txt.afs.packet.utils;

import it.txt.afs.packet.Packet;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 18-gen-2006
 * Time: 13.35.37
 * This class is itended to group together a set of utilities related to packet
 */
public class PacketUtils {
    // represent the max dimension of the afs_packet.id db field
    //and it's used in order to do not exeed the max lenght for that field
    private static final int MAX_ID_LENGTH = 64;
    //represet the number of attributes that uniquely identify a packet.
    private static final int PACKET_KEY_ATTRIBUTES_COUNT = 5;

    /**
     * This method calculate a uniquey id starting from a source string and the result max length
     *
     * @param src       the string that shoul be hided
     * @param keyLength the max length of the resulting string
     * @return return a uniquey id
     */
    private static String hashThis(String src, int keyLength) {

        StringBuffer sb = new StringBuffer();
        int m = src.length() / keyLength;
        int r = src.length() % keyLength;

        for (int j = 0; j < keyLength; j++) {
            int n = 0;
            for (int i = 0; i < m; i++) {
                n = n + (int) src.charAt(i * keyLength + j);
            }
            if (j < r) n = n + (int) src.charAt(m * keyLength + j);
            n = n % 13 + (n + j) % 3;
            n = (n < 10 ? n + ((int) '0') : n - 10 + ((int) 'A'));
            sb.append((char) n);
        }
        return sb.toString();
    }

    /**
     * This method generate the unique package id. It keeps in input the set of attributes that uniquely
     * identify a package. In particular this method is usefull to recalculate the id of an existing packet.
     *
     * @param name      the name of the package
     * @param fromPlant the source plant
     * @param toPlant   the destination plant
     * @param owner     the sender (i.e. the owner) of the packet
     * @return the packet id
     */
    public static String generatePacketId(String name, String fromPlant, String toPlant, String owner) {
        StringBuffer result = new StringBuffer();
        //this represent the lenght of the the single parts of id that uniquely identify a package
        int subIdMaxLength = MAX_ID_LENGTH % PACKET_KEY_ATTRIBUTES_COUNT;

        result.append(hashThis(name, subIdMaxLength));
        result.append(hashThis(fromPlant, subIdMaxLength));
        result.append(hashThis(toPlant, subIdMaxLength));
        result.append(hashThis(owner, subIdMaxLength));
        result.append(hashThis(Long.toString(System.currentTimeMillis()), subIdMaxLength));

        if (result.length() <= MAX_ID_LENGTH)
            return result.toString();
        else {
            return hashThis(result.toString(), MAX_ID_LENGTH);
        }
    }

    /**
     * This method generate the unique package id. It keeps in input the set of attributes that uniquely
     * identify a package. In particular this method is usefull to recalculate the id of an existing packet.
     *
     * @return the packet id
     */
    public static String generatePacketId(Packet packet) {
        String result = "";
        if (packet != null) {
            result = generatePacketId(packet.getName(), packet.getFromPlant(), packet.getDestinationPlant(), packet.getSenderLogin());
        }
        return result;
    }


}


