package DB;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBconnection {
    private static final File CONFIG_FILE = new File(".config.properties");  

    public static Connection connect() {
        Connection connection = null;

        try (FileInputStream input = new FileInputStream(CONFIG_FILE.getAbsolutePath())) {
            Properties prop = new Properties();
            prop.load(input);
            String dbName = prop.getProperty("db.name");
            String dbUrl = prop.getProperty("db.url") + dbName;
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");
            
            connection = DriverManager.getConnection(dbUrl, user, password);
            if (connection != null) System.out.println("Connection to " + dbName + " established");
            else System.out.println("Connection failed");
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
