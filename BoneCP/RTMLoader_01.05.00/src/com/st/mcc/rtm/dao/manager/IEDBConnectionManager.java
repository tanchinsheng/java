package com.st.mcc.rtm.dao.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import static com.st.mcc.rtm.util.AppConstants.APP_CONFIG_PATH;
import java.sql.Connection;

/*
 * http://tunatore.wordpress.com/2011/11/07/how-to-use-bonecp-java-database-connection-pool-jdbc-pool-library/
 */
public class IEDBConnectionManager {

    private static BoneCP connectionPool = null;
    /**
     * LOGGER...
     */
    private static final Logger LOGGER =
            Logger.getLogger(IEDBConnectionManager.class.getName());
    /**
     * mainDBProperties ....
     */
    private static String mainDBProperties;
    /**
     * dbDriverClass ...
     */
    private static String dbDriverClass;
    /**
     * dbUrl ...
     */
    private static String dbUrl;
    /**
     * dbUser ...
     */
    private static String dbUser;
    private static String dbPassword;
    private static int minConnectionsPerPartition;
    private static int partitionCount;
    private static String dbTimeout;
    private static Properties prop;

    /**
     * setPropoerties.
     */
    private static void setProperties(final String fileName) throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream(fileName);
        prop.loadFromXML(fis);
        dbDriverClass = prop.getProperty("DbDriverClass");
        dbUrl = prop.getProperty("DbUrl");
        dbUser = prop.getProperty("DbUser");
        dbPassword = prop.getProperty("DbPassword");
        minConnectionsPerPartition = Integer.parseInt(prop.getProperty("MinConnectionsPerPartition"));
        partitionCount = Integer.parseInt(prop.getProperty("PartitionCount"));
        dbTimeout = prop.getProperty("DbTimeout");
    }

    public static void configureConnPool() {

        try {
            mainDBProperties = APP_CONFIG_PATH + "connectionManager_IEDB.xml";
            //mainDBProperties = APP_CONFIG_PATH + "connectionManager.xml";
            setProperties(mainDBProperties);
            Class.forName(dbDriverClass);
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPassword);
            config.setMinConnectionsPerPartition(minConnectionsPerPartition); //if you say 5 here, there will be 10 connection available   config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(partitionCount); //2*5 = 10 connection will be available
            //config.setLazyInit(true); //depends on the application usage you should chose lazy or not
            //setting Lazy true means BoneCP won't open any connections before you request a one from it.
            connectionPool = new BoneCP(config); // setup the connection pool
            //System.out.println("IEDB contextInitialized.....Connection Pooling is configured");
            //System.out.println("IEDB Total connections ==> " + connectionPool.getTotalCreatedConnections());
            IEDBConnectionManager.setConnectionPool(connectionPool);

        } catch (Exception e) {
            LOGGER.error("RTM IEDB Exception : " + e.getMessage()); //you should use exception wrapping on real-production code
            //System.out.println(e.getMessage());
        }

    }

    public static void shutdownConnPool() {

        try {
            BoneCP connectionPool = IEDBConnectionManager.getConnectionPool();
            //System.out.println("IEDB contextDestroyed....");
            if (connectionPool != null) {
                connectionPool.shutdown(); //this method must be called only once when the application stops.
                //you don't need to call it every time when you get a connection from the Connection Pool
                //System.out.println("IEDB contextDestroyed.....Connection Pooling shut downed!");
            }

        } catch (Exception e) {
            LOGGER.error("RTM IEDB Exception : " + e.getMessage());
        }
    }

    public static Connection getConnection() {

        Connection conn = null;
        try {
            conn = getConnectionPool().getConnection();
            //will get a thread-safe connection from the BoneCP connection pool.
            //synchronization of the method will be done inside BoneCP source
        } catch (Exception e) {
            LOGGER.error("RTM IEDB Exception : " + e.getMessage());
        }
        return conn;

    }

    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
            LOGGER.error("RTM IEDB Exception : " + e.getMessage());
        }

    }

    public static void closeResultSet(ResultSet rSet) {
        try {
            if (rSet != null) {
                rSet.close();
            }
        } catch (Exception e) {
            LOGGER.error("RTM IEDB Exception : " + e.getMessage());
        }

    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close(); //release the connection - the name is tricky but connection is not closed it is released
            }   //and it will stay in pool
        } catch (SQLException e) {
            LOGGER.error("RTM IEDB Exception : " + e.getMessage());
        }

    }

    public static BoneCP getConnectionPool() {
        return connectionPool;
    }

    public static void setConnectionPool(BoneCP connectionPool) {
        IEDBConnectionManager.connectionPool = connectionPool;
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        configureConnPool();
        try {
            conn = IEDBConnectionManager.getConnection();

            final String query = "select PASSWORD from users where login='IVANHO'";
           
            statement = conn.createStatement();
            rs = statement.executeQuery(query);

            if (!rs.next()) {
                System.out.println("No records!");
            } else {
                System.out.println(rs.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                IEDBConnectionManager.closeConnection(conn);
                IEDBConnectionManager.shutdownConnPool();
            } catch (SQLException sqlex) {
                System.out.println("IEDB sql exception");
            }

        }
    }
}
