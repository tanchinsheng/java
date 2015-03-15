package ocpjp.pretest;

import java.sql.*;
// Utility class with method connectToDb() that will be used by other programs in this chapter
class DbQuery4 {

    public static void main(String[] args) throws SQLException {
        try (
                Connection connection = DbConnector.connectToDb();
                Statement statement = connection.createStatement();
                ResultSet resultset = statement.executeQuery(
                "SELECT firstName,email FROM contact WHERE firstName =\"Michael\"")) {
            System.out.println("fName \temail");
            while (resultset.next()) {
                System.out.println(resultset.getString("firstName") + "\t"
                        + resultset.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
