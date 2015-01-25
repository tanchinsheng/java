package it.txt.afs.packet.manager;

import it.txt.afs.packet.Packet;
import it.txt.afs.packet.PacketList;
import it.txt.afs.packet.utils.PacketsDbSearchResult;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.VectorUtils;
import tpms.TpmsException;
import tpms.CtrlServlet;
import tpms.utils.QueryUtils;
import tpms.utils.DBTrack;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Date;
import java.text.ParseException;

import tol.oneConnDbWrtr;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-feb-2006
 * Time: 13.08.00
 */
public class PacketsDbManager extends QueryUtils {


    /**
     * This method insert packet data into database..
     *
     * @param packet the packet that should be inserted into db
     */
    public static void dbInsertPacketData(Packet packet, String sessionId, String userId) throws TpmsException {

        if (packet != null) {
            StringBuffer queryFields = new StringBuffer("ID, NAME, DESTINATION_PLANT, FROM_PLANT, SENDER_LOGIN, SENDER_EMAIL, FIRST_RECIEVE_LOGIN, FIRST_RECIEVE_EMAIL, " +
                    "SECOND_RECIEVE_LOGIN, SECOND_RECIEVE_EMAIL, STATUS, SENT_DATE, LAST_ACTION_DATE, XFER_PATH, CC_EMAIL, " +
                    "TP_RELEASE, TP_REVISION, EXTRACTION_DATE, EXTRACTION_LOGIN, RECIEVER_COMMENTS, SENDER_COMMENTS");


            String valuesSeparator = ", ";
            StringBuffer queryFieldsValues = new StringBuffer();
            /********************mandatory fields***************************/
            queryFieldsValues.append(getStringValueForQuery(packet.getId()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getName()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getDestinationPlant()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getFromPlant()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getSenderLogin()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getSenderEmail()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getFirstRecieveLogin()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getFirstRecieveEmail()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getSecondRecieveLogin()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getSecondRecieveEmail()));

            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getStatus()));
            queryFieldsValues.append(valuesSeparator).append(getDateForQuery(packet.getSentDate()));
            queryFieldsValues.append(valuesSeparator).append(getDateForQuery(packet.getLastActionDate()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getXferPath()));

            /********************optional fields***************************/
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getCcEmail()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getTpRelease()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getTpRevision()));
            queryFieldsValues.append(valuesSeparator).append(getDateForQuery(packet.getExtractionDate()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getExtractionLogin()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getRecieverComments()));
            queryFieldsValues.append(valuesSeparator).append(getStringValueForQuery(packet.getSenderComments()));


            String query = "insert into AFS_PACKET (" + queryFields + ") values (" + queryFieldsValues + ")";


            boolean queryNotDone = true;


            if (dbWriter == null) {
                try {
                    dbWriter = getDbConnection();
                } catch (Exception e) {
                    errorLog("PacketsDbManager :: dbInsertPacketData : unable to retrieve the connection, execution will continue", e);
                }
            }

            for (int i = 0; (queryNotDone && i < 3); i++) {
                //attempt to insert packet data
                try {
                    dbWriter.submit(query);
                    dbWriter.commit();
                    queryNotDone = false;
                } catch (Exception e) {
                    errorLog("PacketsDbManager :: dbInsertPacketData : error during query execution: attempt number = " + i + " query =" + query, e);
                    try {
                        //attempt to restore the connection....


                        if (dbWriter != null) {
                            //if the connection was not null roll back the last query...
                            dbWriter.rollback();
                            //just wait a second
                            Thread.sleep(1000);
                            //and than check it
                            dbWriter.checkConn();

                        } else {
                            //the conenction object is null...
                            //just wait a second
                            Thread.sleep(1000);
                            //try to retrieve it again...
                            dbWriter = getDbConnection();
                        }
                    } catch (Exception ex) {
                        errorLog("PacketsDbManager :: dbInsertPacketData : error during retriving db connection attempt nubumber = " + i + " query =" + query, e);
                    }
                }
            }

            if (queryNotDone) {
                //if the query was not done track it!!!
                try {
                    DBTrack.trackQuery(sessionId, userId, query);
                } catch (IOException e) {
                    errorLog("PacketsDbManager :: dbInsertPacketData : IOException during query tracking: unable to track query!!!", e);
                }
            }

        } else {
            throw new TpmsException("PacketsDbManager :: dbInsertPacketData : the given packet is null, nothing to insert");
        }
    }


    /**
     * @param packetId
     * @return
     * @throws TpmsException
     */
    public static Packet getPacketDataFromDB(String packetId) throws TpmsException {
        Packet packet;
        if (!GeneralStringUtils.isEmptyString(packetId)) {
            oneConnDbWrtr dbWriter = CtrlServlet.dbWriter;
            if (dbWriter == null) {
                throw new TpmsException("PacketsDbManager :: getPacketData : unable to get db connection no insertion will be done");
            }
            //TO_CHAR(SENT_DATE, '" + ORACLE_DATE_FORMAT + "') as SENT_DATE, TO_CHAR(EXTRACTION_DATE, '" + ORACLE_DATE_FORMAT + "') as EXTRACTION_DATE, EXTRACTION_LOGIN, TO_CHAR(LAST_ACTION_DATE, '" + ORACLE_DATE_FORMAT + "') as LAST_ACTION_DATE
            String query = "select ID, NAME, TP_RELEASE, TP_REVISION, DESTINATION_PLANT, FROM_PLANT, SENDER_LOGIN, SENDER_EMAIL, FIRST_RECIEVE_LOGIN, FIRST_RECIEVE_EMAIL, SECOND_RECIEVE_LOGIN, SECOND_RECIEVE_EMAIL, CC_EMAIL, STATUS, VOB_STATUS, TO_CHAR(SENT_DATE, '" + ORACLE_DATE_FORMAT + "') as SENT_DATE, TO_CHAR(EXTRACTION_DATE, '" + ORACLE_DATE_FORMAT + "') as EXTRACTION_DATE, EXTRACTION_LOGIN, TO_CHAR(LAST_ACTION_DATE, '" + ORACLE_DATE_FORMAT + "') as LAST_ACTION_DATE, RECIEVER_COMMENTS, SENDER_COMMENTS, XFER_PATH " +
                    "from AFS_PACKET " +
                    "where ID = '" + packetId + "' ";
            try {
                ResultSet packetData = dbWriter.getRecordset(query);
                packet = new Packet();
                packetData.next();
                packet.setId(packetData.getString("ID"));
                packet.setName(packetData.getString("NAME"));
                packet.setTpRelease(packetData.getString("TP_RELEASE"));
                packet.setTpRevision(packetData.getString("TP_REVISION"));
                packet.setDestinationPlant(packetData.getString("DESTINATION_PLANT"));
                packet.setFromPlant(packetData.getString("FROM_PLANT"));
                packet.setSenderLogin(packetData.getString("SENDER_LOGIN"));
                packet.setSenderEmail(packetData.getString("SENDER_EMAIL"));
                packet.setFirstRecieveLogin(packetData.getString("FIRST_RECIEVE_LOGIN"));
                packet.setFirstRecieveEmail(packetData.getString("FIRST_RECIEVE_EMAIL"));
                packet.setSecondRecieveLogin(packetData.getString("SECOND_RECIEVE_LOGIN"));
                packet.setSecondRecieveEmail(packetData.getString("SECOND_RECIEVE_EMAIL"));
                packet.setCcEmail(packetData.getString("CC_EMAIL"));
                packet.setStatus(packetData.getString("STATUS"));
                packet.setVobStatus(packetData.getString("VOB_STATUS"));
                packet.setExtractionLogin(packetData.getString("EXTRACTION_LOGIN"));
                packet.setSenderComments(packetData.getString("SENDER_COMMENTS"));
                packet.setRecieverComments(packetData.getString("RECIEVER_COMMENTS"));
                packet.setXferPath(packetData.getString("XFER_PATH"));



                if (!GeneralStringUtils.isEmptyString(packetData.getString("SENT_DATE"))) {
                        try {
                            packet.setSentDate(simpleDateFormat.parse(packetData.getString("SENT_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + packetData.getString("SENT_DATE") + ")";
                            throw new TpmsException(msg, "PacketsDbManager :: getPacketDataFromDB", e);
                        }
                    }

                    if (!GeneralStringUtils.isEmptyString(packetData.getString("EXTRACTION_DATE"))) {
                        try {
                            packet.setExtractionDate(simpleDateFormat.parse(packetData.getString("EXTRACTION_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse extraction date (" + packetData.getString("EXTRACTION_DATE") + ")";
                            throw new TpmsException(msg, "PacketsDbManager :: getPacketDataFromDB", e);
                        }
                    }

                    if (!GeneralStringUtils.isEmptyString(packetData.getString("LAST_ACTION_DATE"))) {
                        try {
                            packet.setLastActionDate(simpleDateFormat.parse(packetData.getString("LAST_ACTION_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse last action date (" + packetData.getString("LAST_ACTION_DATE") + ")";
                            throw new TpmsException(msg, "PacketsDbManager :: getPacketDataFromDB", e);
                        }
                    }
                packetData.close();
            } catch (SQLException e) {
                throw new TpmsException("PacketsDbManager :: getPacketData : unable to execute query ( " + query + " )", "", e);
            }
        } else {
            throw new TpmsException("PacketsDbManager :: getPacketData : the given packetI is empty or null");
        }
        return packet;
    }


    /**
     * This method update packet data into database..
     *
     * @param packet the packet that should be updated into db
     */
    public static void dbUpdatePacketData(Packet packet, String sessionId, String userId) throws TpmsException {

        if (packet != null) {


            String valuesSeparator = ", ";
            StringBuffer queryFieldsValues = new StringBuffer();

            queryFieldsValues.append("NAME = ").append(getStringValueForQuery(packet.getName())).append(valuesSeparator);
            queryFieldsValues.append("TP_RELEASE = ").append(getStringValueForQuery(packet.getTpRelease())).append(valuesSeparator);
            queryFieldsValues.append("TP_REVISION = ").append(getStringValueForQuery(packet.getTpRevision())).append(valuesSeparator);
            queryFieldsValues.append("DESTINATION_PLANT = ").append(getStringValueForQuery(packet.getDestinationPlant())).append(valuesSeparator);
            queryFieldsValues.append("FROM_PLANT = ").append(getStringValueForQuery(packet.getFromPlant())).append(valuesSeparator);
            queryFieldsValues.append("SENDER_LOGIN = ").append(getStringValueForQuery(packet.getSenderLogin())).append(valuesSeparator);
            queryFieldsValues.append("SENDER_EMAIL = ").append(getStringValueForQuery(packet.getSenderEmail())).append(valuesSeparator);
            queryFieldsValues.append("FIRST_RECIEVE_LOGIN = ").append(getStringValueForQuery(packet.getFirstRecieveLogin())).append(valuesSeparator);
            queryFieldsValues.append("FIRST_RECIEVE_EMAIL = ").append(getStringValueForQuery(packet.getFirstRecieveEmail())).append(valuesSeparator);
            queryFieldsValues.append("SECOND_RECIEVE_LOGIN = ").append(getStringValueForQuery(packet.getSecondRecieveLogin())).append(valuesSeparator);
            queryFieldsValues.append("SECOND_RECIEVE_EMAIL = ").append(getStringValueForQuery(packet.getSecondRecieveEmail())).append(valuesSeparator);
            queryFieldsValues.append("CC_EMAIL = ").append(getStringValueForQuery(packet.getCcEmail())).append(valuesSeparator);
            queryFieldsValues.append("STATUS = ").append(getStringValueForQuery(packet.getStatus())).append(valuesSeparator);
            queryFieldsValues.append("EXTRACTION_LOGIN = ").append(getStringValueForQuery(packet.getExtractionLogin())).append(valuesSeparator);
            queryFieldsValues.append("RECIEVER_COMMENTS = ").append(getStringValueForQuery(packet.getRecieverComments())).append(valuesSeparator);
            queryFieldsValues.append("SENDER_COMMENTS = ").append(getStringValueForQuery(packet.getSenderComments())).append(valuesSeparator);
            queryFieldsValues.append("XFER_PATH = ").append(getStringValueForQuery(packet.getXferPath())).append(valuesSeparator);
            queryFieldsValues.append("SENT_DATE = ").append(getDateForQuery(packet.getSentDate())).append(valuesSeparator);
            queryFieldsValues.append("EXTRACTION_DATE = ").append(getDateForQuery(packet.getExtractionDate())).append(valuesSeparator);
            queryFieldsValues.append("LAST_ACTION_DATE = ").append(getDateForQuery(packet.getLastActionDate()));

            String query = "update afs_packet set " + queryFieldsValues + " where id=" + getStringValueForQuery(packet.getId());


            boolean queryNotDone = true;


            if (dbWriter == null) {
                try {
                    dbWriter = getDbConnection();
                } catch (Exception e) {
                    errorLog("PacketsDbManager :: dbUpdatePacketData : unable to retrieve the connection execution will continue", e);
                }
            }

            for (int i = 0; (queryNotDone && i < 3); i++) {
                //attempt to insert packet data
                try {
                    dbWriter.submit(query);
                    dbWriter.commit();
                    queryNotDone = false;
                } catch (Exception e) {
                    errorLog("PacketsDbManager :: dbUpdatePacketData : error during query execution: attempt number = " + i + " query =" + query, e);
                    try {
                        //attempt to restore the connection....


                        if (dbWriter != null) {
                            //if the connection was not null roll back the last query...
                            dbWriter.rollback();
                            //just wait a second
                            Thread.sleep(1000);
                            //and than check it
                            dbWriter.checkConn();

                        } else {
                            //the conenction object is null...
                            //just wait a second
                            Thread.sleep(1000);
                            //try to retrieve it again...
                            dbWriter = getDbConnection();
                        }
                    } catch (Exception ex) {
                        errorLog("PacketsDbManager :: dbUpdatePacketData : error during retriving db connection attempt nubumber = " + i + " query =" + query, e);
                    }
                }
            }

            if (queryNotDone) {
                //if the query was not done track it!!!
                try {
                    DBTrack.trackQuery(sessionId, userId, query);
                } catch (IOException e) {
                    errorLog("PacketsDbManager :: dbUpdatePacketData : IOException during query tracking: unable to track query!!! " + query, e);
                }
            }

        } else {
            throw new TpmsException("PacketsDbManager :: dbUpdatePacketData : the given packet is null, nothing to insert");
        }
    }


    /**
     * This method return a vector of hashtable containing the query resulting data suitable for displaing the result
     *
     * @param sqlSelectStatement the select sql statement that should be executed
     * @return a vector of hashtable
     * @throws TpmsException if the db connection is not availble
     */
    protected static Vector retrieveDbData(String sqlSelectStatement) throws TpmsException {
        ResultSet rs = executeSelectQuery(sqlSelectStatement);
        Vector result = VectorUtils.dumpResultSetToVectorOfHashtable(rs);
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            errorLog("PacketsDbManager :: retrieveDbData : SQLException while closing resultset", e);
        }              
        return result;
    }

    /**
     * @return This method return a vector of hashtable containing each different destination plant present in asf_packet table
     * @throws TpmsException if the db connection is not availble
     */
    public static Vector getDestinationPlantsList() throws TpmsException {
        String query = "select destination_plant as " + AfsServletUtils.DB_SEARCH_DESTINATION_PLANT_FIELDS + " " +
                "from afs_packet " +
                "group by destination_plant";

        return retrieveDbData(query);
    }

    /**
     * @return This method return a vector of hashtable containing each different from plant present in asf_packet table
     * @throws TpmsException if the db connection is not availble
     */
    public static Vector getFromPlantsList() throws TpmsException {
        String query = "select from_plant as " + AfsServletUtils.DB_SEARCH_FROM_PLANT_FIELDS + " " +
                "from afs_packet " +
                "group by from_plant";
        return retrieveDbData(query);
    }

    /**
     * @return This method return a vector of hashtable containing each different sender login present in asf_packet table
     * @throws TpmsException if the db connection is not availble
     */
    public static Vector getSenderLoginsList() throws TpmsException {
        String query = "select sender_login AS " + AfsServletUtils.DB_SEARCH_SENDER_LOGIN_FIELDS + " " +
                "from afs_packet " +
                "group by sender_login";
        return retrieveDbData(query);
    }


    public static Vector getFirstRecieveLoginsList() throws TpmsException {
        String query = "select first_recieve_login as " + AfsServletUtils.DB_SEARCH_FIRST_RECIEVE_LOGIN_FIELDS + " " +
                "from afs_packet " +
                "group by first_recieve_login";
        return retrieveDbData(query);
    }


    public static Vector getSecondRecieveLoginsList() throws TpmsException {
        String query = "select second_recieve_login as " + AfsServletUtils.DB_SEARCH_SECOND_RECIEVE_LOGIN_FIELDS + " " +
                "from afs_packet " +
                "group by second_recieve_login";
        return retrieveDbData(query);
    }

    /**
     * @return This method return a vector of hashtable containing each different extraction login present in asf_packet table
     * @throws TpmsException if the db connection is not availble
     */
    public static Vector getExtractorsLoginsList() throws TpmsException {
        String query = "select extraction_login as " + AfsServletUtils.DB_SEARCH_EXTRACTION_LOGIN_FIELDS + " " +
                "from afs_packet " +
                "group by extraction_login";
        return retrieveDbData(query);
    }


    public static PacketsDbSearchResult searchForPackets(Hashtable searchFieldsValues) throws TpmsException {
        return searchForPackets(searchFieldsValues, null);
    }


    /**
     * Search for packets in database.
     * The input hashtable MUST have key equal to afs_packet table columns and values equal to values to look for.
     * if tpmsLogin in not null or empty retrieve only those packets where the user identified by tpmsLogin is involved
     * (i.e. the current user is the sender or first receive or second receive or extraction login)
     *
     * @param searchFieldsValues
     * @param tpmsLogin
     * @return PacketsDbSearchResult which contains in fieldsValues attribute the searchFieldsValues in input
     *         and in packetList the ruslting packet list
     * @throws TpmsException is the db connection is invalid or if it's unable to execute the search query...
     */
    public static PacketsDbSearchResult searchForPackets(Hashtable searchFieldsValues, String tpmsLogin) throws TpmsException {
        debugLog("PacketsDbManager :: searchForPackets : (searchFieldsValues == null or searchFieldsValues.isEmpty) = " + (searchFieldsValues == null || searchFieldsValues.isEmpty()) + " tpmsLogin = " + tpmsLogin);
        PacketList packetList = new PacketList();
        String query = "select ID, NAME, TP_RELEASE, TP_REVISION, DESTINATION_PLANT, FROM_PLANT, SENDER_LOGIN, SENDER_EMAIL, " +
                "FIRST_RECIEVE_LOGIN, FIRST_RECIEVE_EMAIL, SECOND_RECIEVE_LOGIN, SECOND_RECIEVE_EMAIL, CC_EMAIL, " +
                "STATUS, VOB_STATUS, TO_CHAR(SENT_DATE, '" + ORACLE_DATE_FORMAT + "') as SENT_DATE, TO_CHAR(EXTRACTION_DATE, '" + ORACLE_DATE_FORMAT + "') as EXTRACTION_DATE, EXTRACTION_LOGIN, TO_CHAR(LAST_ACTION_DATE, '" + ORACLE_DATE_FORMAT + "') as LAST_ACTION_DATE, " +
                "RECIEVER_COMMENTS, SENDER_COMMENTS, XFER_PATH  " +
                "from AFS_PACKET ";

        String whereCondition = "";
        if (!GeneralStringUtils.isEmptyString(tpmsLogin)) {
            //this is the filter for engineers or aided ftp user role in order to show only those packages where he user is involved.
            whereCondition = "where (sender_login = '" + tpmsLogin + "' or first_recieve_login = '" + tpmsLogin + "' or second_recieve_login = '" + tpmsLogin + "'  or extraction_login = '" + tpmsLogin + "')";
        }
        if (searchFieldsValues != null && !searchFieldsValues.isEmpty()) {
            Enumeration searchKeys = searchFieldsValues.keys();
            String currentSearchKey;
            //manage single value search fields ...
            while (searchKeys.hasMoreElements()) {

                currentSearchKey = (String) searchKeys.nextElement();
                debugLog("PacketsDbManager :: searchForPackets : currentSearchKey = " + currentSearchKey + " = " + searchFieldsValues.get(currentSearchKey));
                if (!currentSearchKey.equals(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS) && !currentSearchKey.equals(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_TO_FIELDS) &&
                        !currentSearchKey.equals(AfsServletUtils.DB_SEARCH_SENT_DATE_FROM_FIELDS) && !currentSearchKey.equals(AfsServletUtils.DB_SEARCH_SENT_DATE_TO_FIELDS) &&
                        !currentSearchKey.equals(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS) && !currentSearchKey.equals(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS)) {
                    if (GeneralStringUtils.isEmptyString(whereCondition)) {
                        whereCondition = "where " + currentSearchKey + " like " + getStringValueForQuery((String) searchFieldsValues.get(currentSearchKey));
                    } else {
                        whereCondition += " and " + currentSearchKey + " like " + getStringValueForQuery((String) searchFieldsValues.get(currentSearchKey));
                    }
                }
            }

            //manage date search fields...

            String tmpDateCondition = getDateInInterval("SENT_DATE", (Date) searchFieldsValues.get(AfsServletUtils.DB_SEARCH_SENT_DATE_FROM_FIELDS), (Date) searchFieldsValues.get(AfsServletUtils.DB_SEARCH_SENT_DATE_TO_FIELDS));
            if (!GeneralStringUtils.isEmptyString(tmpDateCondition)) {
                if (GeneralStringUtils.isEmptyString(whereCondition)) {
                    whereCondition = "where " + tmpDateCondition;
                } else {
                    whereCondition += " and " + tmpDateCondition;
                }
            }

            tmpDateCondition = getDateInInterval("EXTRACTION_DATE", (Date) searchFieldsValues.get(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS), (Date) searchFieldsValues.get(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_TO_FIELDS));


            if (!GeneralStringUtils.isEmptyString(tmpDateCondition)) {
                if (GeneralStringUtils.isEmptyString(whereCondition)) {
                    whereCondition = "where " + tmpDateCondition;
                } else {
                    whereCondition += " and " + tmpDateCondition;
                }
            }

            tmpDateCondition = getDateInInterval("LAST_ACTION_DATE", (Date) searchFieldsValues.get(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS), (Date) searchFieldsValues.get(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS));
            if (!GeneralStringUtils.isEmptyString(tmpDateCondition)) {
                if (GeneralStringUtils.isEmptyString(whereCondition)) {
                    whereCondition = "where " + tmpDateCondition;
                } else {
                    whereCondition += " and " + tmpDateCondition;
                }
            }
        }
        query += whereCondition;
        debugLog("PacketsDbManager :: searchForPackets : query = " + query);
        //execute the builded query...
        ResultSet foundPackets = executeSelectQuery(query);

        if (foundPackets != null) {
            Packet tmpPacket;
            try {
                while (foundPackets.next()) {
                    tmpPacket = new Packet();
                    tmpPacket.setId(foundPackets.getString("ID"));
                    tmpPacket.setName(foundPackets.getString("NAME"));
                    tmpPacket.setTpRelease(foundPackets.getString("TP_RELEASE"));
                    tmpPacket.setTpRevision(foundPackets.getString("TP_REVISION"));
                    tmpPacket.setDestinationPlant(foundPackets.getString("DESTINATION_PLANT"));
                    tmpPacket.setFromPlant(foundPackets.getString("FROM_PLANT"));
                    tmpPacket.setSenderLogin(foundPackets.getString("SENDER_LOGIN"));
                    tmpPacket.setSenderEmail(foundPackets.getString("SENDER_EMAIL"));
                    tmpPacket.setFirstRecieveLogin(foundPackets.getString("FIRST_RECIEVE_LOGIN"));
                    tmpPacket.setFirstRecieveEmail(foundPackets.getString("FIRST_RECIEVE_EMAIL"));
                    tmpPacket.setSecondRecieveLogin(foundPackets.getString("SECOND_RECIEVE_LOGIN"));
                    tmpPacket.setSecondRecieveEmail(foundPackets.getString("SECOND_RECIEVE_EMAIL"));
                    tmpPacket.setCcEmail(foundPackets.getString("CC_EMAIL"));
                    tmpPacket.setStatus(foundPackets.getString("STATUS"));
                    tmpPacket.setVobStatus(foundPackets.getString("VOB_STATUS"));
                    tmpPacket.setExtractionLogin(foundPackets.getString("EXTRACTION_LOGIN"));
                    tmpPacket.setSenderComments(foundPackets.getString("SENDER_COMMENTS"));
                    tmpPacket.setRecieverComments(foundPackets.getString("RECIEVER_COMMENTS"));
                    tmpPacket.setXferPath(foundPackets.getString("XFER_PATH"));

                    if (!GeneralStringUtils.isEmptyString(foundPackets.getString("SENT_DATE"))) {
                        try {
                            tmpPacket.setSentDate(simpleDateFormat.parse(foundPackets.getString("SENT_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + foundPackets.getString("SENT_DATE") + ")";
                            throw new TpmsException(msg, "PacketsDbManager :: searchForPackets", e);
                        }
                    }

                    if (!GeneralStringUtils.isEmptyString(foundPackets.getString("EXTRACTION_DATE"))) {
                        try {
                            tmpPacket.setExtractionDate(simpleDateFormat.parse(foundPackets.getString("EXTRACTION_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse extraction date (" + foundPackets.getString("EXTRACTION_DATE") + ")";
                            throw new TpmsException(msg, "PacketsDbManager :: searchForPackets", e);
                        }
                    }

                    if (!GeneralStringUtils.isEmptyString(foundPackets.getString("LAST_ACTION_DATE"))) {
                        try {
                            tmpPacket.setLastActionDate(simpleDateFormat.parse(foundPackets.getString("LAST_ACTION_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse last action date (" + foundPackets.getString("LAST_ACTION_DATE") + ")";
                            throw new TpmsException(msg, "PacketsDbManager :: searchForPackets", e);
                        }
                    }

                    packetList.addPacket(tmpPacket);
                }
            } catch (SQLException e) {
                String msg = "Error during managing query result";
                throw new TpmsException(msg, "PacketsDbManager :: searchForPackets", e);

            } finally {
                debugLog("PacketsDbManager :: searchForPackets : close the resultset");
                if (foundPackets != null)
                    try {
                        foundPackets.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
        return new PacketsDbSearchResult(searchFieldsValues, packetList);
    }

}
