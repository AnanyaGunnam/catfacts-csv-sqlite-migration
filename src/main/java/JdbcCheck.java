import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Connects to catfacts.db and reports on it. Run it whenever you want to
 * check on the database. You do not need to change this file.
 */
public class JdbcCheck {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:catfacts.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery("SELECT sqlite_version()")) {
                rs.next();
                System.out.println("Connected. SQLite version " + rs.getString(1));
            }

            // Every SQLite database lists its own tables in sqlite_schema.
            String findTable = "SELECT COUNT(*) FROM sqlite_schema WHERE type = 'table' AND name = 'facts'";
            try (ResultSet rs = stmt.executeQuery(findTable)) {
                rs.next();
                if (rs.getInt(1) == 0) {
                    System.out.println("No facts table yet.");
                    return;
                }
            }

            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM facts")) {
                rs.next();
                System.out.println("The facts table has " + rs.getInt(1) + " rows.");
            }
        }
    }
}
