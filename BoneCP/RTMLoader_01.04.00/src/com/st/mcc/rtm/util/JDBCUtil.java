/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.mcc.rtm.util;

import static com.st.mcc.rtm.util.AppConstants.APP_CONFIG_PATH;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author cstan
 */
public class JDBCUtil {

    /**
     * LOGGER...
     */
    private static final Logger LOGGER =
            Logger.getLogger(JDBCUtil.class.getName());
    /**
     * mainDBProperties ....
     */
    private String mainDBProperties;
    /**
     * dbDriverClass ...
     */
    private String dbDriverClass;
    /**
     * dbUrl ...
     */
    private String dbUrl;
    /**
     * dbUser ...
     */
    private String dbUser;
    private String dbPassword;
    private String dbMinConns;
    private String dbMaxConns;
    private String dbTimeout;
    private Properties prop;

    /**
     * Constructor..
     *
     * @param dBInstance
     */
    public JDBCUtil(final String dBInstance) {
        super();
        this.mainDBProperties = APP_CONFIG_PATH + dBInstance + ".xml";
    }

    /**
     * setPropoerties.
     */
    private void setProperties(final String fileName) throws IOException {
        this.prop = new Properties();
        FileInputStream fis = new FileInputStream(fileName);
        prop.loadFromXML(fis);
        this.dbDriverClass = this.prop.getProperty("DbDriverClass");
        this.dbUrl = this.prop.getProperty("DbUrl");
        this.dbUser = this.prop.getProperty("DbUser");
        this.dbPassword = this.prop.getProperty("DbPassword");
        this.dbMinConns = this.prop.getProperty("DbMinConns");
        this.dbMaxConns = this.prop.getProperty("DbMaxConns");
        this.dbTimeout = this.prop.getProperty("DbTimeout");
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        try {
            this.setProperties(this.mainDBProperties);
        } catch (InvalidPropertiesFormatException e) {
            LOGGER.error("RTM InvalidPropertiesFormatException: " + e.getMessage());
        } catch (FileNotFoundException e) {
            LOGGER.error("RTM FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            LOGGER.error("RTM IOException : " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("RTM Exception : " + e.getMessage());
        }
        connectionProps.put("user", this.dbUser);
        connectionProps.put("password", this.dbPassword);
        Class.forName(this.dbDriverClass);
        conn = DriverManager.getConnection(this.dbUrl, connectionProps);
        return conn;
    }

    public static void closeConnection(Connection connArg) {
        try {
            if (connArg != null) {
                connArg.close();
                connArg = null;
            }
        } catch (SQLException sqle) {
            LOGGER.error("RTM DB connection close failed: " + sqle.getMessage());
        } finally {
            connArg = null;
        }
    }

    public static void main(String[] args) {

        Connection myConnection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            myConnection = new JDBCUtil("connectionManager").getConnection();
            String query = "select PASSWORD from users where login='IVANHO'";
            Statement stmt = myConnection.createStatement();
            rs = stmt.executeQuery(query);

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
            } catch (SQLException sqlex) {
                System.out.println("sql exception");
            }
            JDBCUtil.closeConnection(myConnection);
        }
    }
}
