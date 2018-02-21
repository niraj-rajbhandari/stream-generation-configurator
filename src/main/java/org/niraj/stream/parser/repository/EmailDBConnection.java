package org.niraj.stream.parser.repository;

import org.niraj.stream.parser.configuration.ConfigReader;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EmailDBConnection {
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String DB_HOST_KEY = "db-host";
    public static final String DB_PORT_KEY = "db-port";
    public static final String DB_NAME_KEY = "db-name";

    Connection connection;

    public static EmailDBConnection instance;

    private EmailDBConnection() throws SQLException, FileNotFoundException {
        try {
            ConfigReader configReader = ConfigReader.getInstance();
            String username = configReader.getProperty(EmailDBConnection.USERNAME_KEY);
            String password = configReader.getProperty(EmailDBConnection.PASSWORD_KEY);
            String hostname = configReader.getProperty(EmailDBConnection.DB_HOST_KEY);
            int port = Integer.parseInt(configReader.getProperty(EmailDBConnection.DB_PORT_KEY));
            String database = configReader.getProperty(EmailDBConnection.DB_NAME_KEY);
            Class.forName("com.mysql.jdbc.Driver");
//            String connectionString = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
            String connectionString = "jdbc:mysql://db.csc.tntech.edu:3306/enron";

            connection = DriverManager.getConnection(connectionString,"enron","dEvrPgPKk7g6BNVBEYSbBdRQNgebuFFD7MQmhkhnfyNyyCaA2PgacfC5R85m4bN8");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found");
        }
    }

    public static Connection getConnection() throws SQLException, FileNotFoundException {
        if (instance == null) {
            instance = new EmailDBConnection();
        }

        return instance.connection;
    }

    public static void closeConnection() throws SQLException {
        if (instance != null) {
            instance.connection.close();
        }
    }

}
