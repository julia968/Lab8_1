package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class SQLConnection {
    private final Connection connection;

    public SQLConnection() {
        try {
            String url = PropertiesProvider.getAppProperties().getProperty("datasource.url");
            String username = PropertiesProvider.getAppProperties().getProperty("datasource.username");
            String password = PropertiesProvider.getAppProperties().getProperty("datasource.password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
