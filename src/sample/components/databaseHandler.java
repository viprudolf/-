package sample.components;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class databaseHandler {

    public static Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        Properties props = new Properties();

        try (FileInputStream in = new FileInputStream("src/sample/components/database.properties")) {
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);

    }
}

