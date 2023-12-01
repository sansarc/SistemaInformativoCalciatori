import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBconnection {
    private final String dbName;
    DBconnection(String dbName) {
        this.dbName = dbName;
    }

    public Connection connect() {
        Connection connection = null;

        try (FileInputStream input = new FileInputStream("/home/angelo/Documents/OO/Java/ProgettoCalciatori/.config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String dbUrl = prop.getProperty("db.url") + dbName;
            String username = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            connection = DriverManager.getConnection(dbUrl, username, password);
            if (connection != null) System.out.println("connection to " + dbName + " established");
            else System.out.println("Connection failed");
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection to " + dbName + " closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
