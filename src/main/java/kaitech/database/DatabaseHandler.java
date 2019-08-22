package kaitech.database;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler {

    private final Connection dbConn;

    public DatabaseHandler() {
        try {
            dbConn = DriverManager.getConnection("jdbc:sqlite:kaitech.db");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database.", e);
        }
    }

    public PreparedStatement prepareResource(String resource) {
        return prepareStream(DatabaseHandler.class.getResourceAsStream(resource));
    }

    private PreparedStatement prepareStream(InputStream inputStream) {
        try (InputStream is = inputStream) {
            return prepareStatement(IOUtils.toString(is, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse InputStream to String.", e);
        }
    }

    private PreparedStatement prepareStatement(String sql) {
        try {
            return dbConn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to prepare statement.", e);
        }
    }
}
