package im.mctop.bot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author electronrussia
 */
public class DB {
    private static Connection conn = null;

    public Connection DbConnect() {
        String user = "root";
        String password = "root";

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/db_name", user, password);
            System.out.println("Connected to DB");
        } catch (SQLException ex) {
            System.out.println("[error] Failed to connect to MySQL server!");
        }
        return null;
    }

    public void reconnect() {
        if (conn == null) {
            this.DbConnect();
        } else {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
                /* do nothing */
            }
            this.DbConnect();
        }
    }
}
