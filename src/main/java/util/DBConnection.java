package util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();

            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASSWORD");

            System.out.println("DB_URL = " + url);
            System.out.println("DB_USER = " + user);

            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(pass);

            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            dataSource = new HikariDataSource(config);

            System.out.println("✅ DB CONNECTED SUCCESS");

        } catch (Exception e) {
            System.out.println("❌ DB CONNECTION FAILED");
            e.printStackTrace();
            throw e; // this is crashing your app
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
