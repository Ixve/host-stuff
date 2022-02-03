package de.aggromc.confighubhost.auth;

import java.sql.*;
import de.aggromc.confighubhost.*;
import java.util.logging.*;
import java.util.*;
import java.io.*;
import com.smattme.*;

public class MySQL
{
    public Connection connection;
    
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection("jdbc:mysql://161.97.72.246:3306/host?autoReconnect=true", "root", "funkygames2311funky!");
        }
        catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex2) {
            final Exception ex;
            final Exception throwables = ex;
            throwables.printStackTrace();
        }
    }
    
    public void disconnect() {
        if (this.isConnected()) {
            try {
                this.connection.close();
                ConfigHost.getLogger().log(Level.FINE, "MySQL was stopped!");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private boolean isConnected() {
        try {
            if (this.connection != null && !this.connection.isClosed() && this.connection.isValid(10)) {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void saveDB() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("DB_NAME", "host");
        properties.setProperty("DB_USERNAME", "root");
        properties.setProperty("JDBC_CONNECTION_STRING", "jdbc:mysql://161.97.72.246:3306/host?autoReconnect=true");
        properties.setProperty("DB_PASSWORD", "funkygames2311funky!");
        properties.setProperty("JDBC_DRIVER_NAME", "com.mysql.jdbc.Driver");
        properties.setProperty("EMAIL_HOST", "91.200.103.0");
        properties.setProperty("EMAIL_PORT", "25");
        properties.setProperty("EMAIL_USERNAME", "teamaggro@teamaggro.dev");
        properties.setProperty("EMAIL_PASSWORD", "ICHZOCKEGERN01lol!");
        properties.setProperty("EMAIL_FROM", "teamaggro@teamaggro.dev");
        properties.setProperty("EMAIL_TO", "admin@confighub.host");
        properties.setProperty("EMAIL_SUBJECT", "Daily Database Backup");
        properties.setProperty("EMAIL_MESSAGE", "The Daily Database Backup for this day is attached in the Message. This Email was automatically generated.");
        properties.setProperty("TEMP_DIR", new File("tmp").getPath());
        final MysqlExportService mysqlExportService = new MysqlExportService(properties);
        mysqlExportService.export();
    }
}
