package kaitech.database;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.String.format;

public class DatabaseHandler {
    private static final String[] setupScripts = {
            "/sql/manage/setupIngredientsTbl.sql",
            "/sql/manage/setupIngredientSuppliersTbl.sql",
            "/sql/manage/setupMenuContentsTbl.sql",
            "/sql/manage/setupMenuItemsTbl.sql",
            "/sql/manage/setupMenusTbl.sql",
            "/sql/manage/setupRecipeIngredientsTbl.sql",
            "/sql/manage/setupRecipesTbl.sql",
            "/sql/manage/setupSalesTbl.sql",
            "/sql/manage/setupSuppliersTbl.sql"
    };

    private final Connection dbConn;

    public DatabaseHandler() {
        try {
            dbConn = DriverManager.getConnection("jdbc:sqlite:kaitech.db");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database.", e);
        }
    }

    public void setup() {
        for (String script : setupScripts) {
            PreparedStatement stmt = prepareResource(script);
            try {
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(format("Unable to run setup script: %s.", script), e);
            }
        }
    }

    public PreparedStatement prepareResource(String resource) {
        return prepareStream(DatabaseHandler.class.getResourceAsStream(resource));
    }

    public PreparedStatement prepareStream(InputStream inputStream) {
        try (InputStream is = inputStream) {
            return prepareStatement(IOUtils.toString(is, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse InputStream to String.", e);
        }
    }

    public PreparedStatement prepareStatement(String sql) {
        try {
            return dbConn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to prepare statement.", e);
        }
    }
}
