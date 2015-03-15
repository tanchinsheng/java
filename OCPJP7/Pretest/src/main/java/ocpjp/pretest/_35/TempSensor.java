/*This is a comment */
package ocpjp.pretest._35;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class TempSensor {

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)) {
            connection.setAutoCommit(true);
            try (
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM sakila.tempsensor")) {

                RowSetFactory rowSetFactory = RowSetProvider.newFactory();
                JdbcRowSet rowSet = rowSetFactory.createJdbcRowSet();
                //RowSetProvider rowSetProvider = RowSetFactory.newProvider();
                //JdbcRowSet rowSet2 = rowSetProvider.createJdbcRowSet();

                // assume that the initial value of temp is "0" in the table
                resultSet.moveToInsertRow();
                resultSet.updateString("temp", "100");
                resultSet.insertRow();
                Savepoint firstSavepoint = connection.setSavepoint();
                resultSet.moveToInsertRow();
                resultSet.updateString("temp", "200");
                resultSet.insertRow();
                Savepoint secondSavepoint = connection.setSavepoint();
                resultSet.moveToInsertRow();
                resultSet.updateString("temp", "300");
                resultSet.insertRow();
                Savepoint thirdSavepoint = connection.setSavepoint();
                connection.rollback(secondSavepoint);
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
