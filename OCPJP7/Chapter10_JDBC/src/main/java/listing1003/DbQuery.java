package listing1003;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import Db.DbConnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Program to illustrate how to query a database
class DbQuery {

    public static void main(String[] args) {
        // Get connection, execute query, get the result set
        // and print the entries from the result rest
        try (Connection connection = DbConnector.connectToDb();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM contact")) {
            System.out.println("ID \tfName \tlName \temail \t\tphoneNo");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + "\t"
                        + resultSet.getString("firstName") + "\t"
                        + resultSet.getString("lastName") + "\t"
                        + resultSet.getString("email") + "\t"
                        + resultSet.getString("phoneNo"));
            }
            System.out.println("");
            resultSet.beforeFirst();
            int numOfColumns = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= numOfColumns; i++) {
                    System.out.print(resultSet.getObject(i) + "\t");
                }
                System.out.println("");
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(-1);
        }
    }
}
