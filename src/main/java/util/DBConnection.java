package util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

      /*  config.setJdbcUrl("jdbc:mysql://localhost:3306/taskflow");
        config.setUsername("root");
        config.setPassword("nanthu_321");
*/
        
        
        config.setJdbcUrl(System.getenv("DB_URL"));
        config.setUsername(System.getenv("DB_USER"));
        config.setPassword(System.getenv("DB_PASSWORD"));
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        config.addDataSourceProperty("useSSL", "false");
        config.addDataSourceProperty("allowPublicKeyRetrieval", "true");
       
        config.setMaximumPoolSize(10);   // max connections
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

DB_URL=jdbc:mysql://your-db-host:3306/taskdb
DB_USER=root
DB_PASSWORD=yourpassword
JWT_SECRET=your_secret_key