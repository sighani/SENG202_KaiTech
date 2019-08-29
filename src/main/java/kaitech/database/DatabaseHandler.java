package kaitech.database;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.String.format;

public class DatabaseHandler {
    private static final String[] setupScripts = {
            "/sql/setup/setupIngredientsTbl.sql",
            "/sql/setup/setupIngredientSuppliersTbl.sql",
            "/sql/setup/setupInventoryTbl.sql",
            "/sql/setup/setupMenuContentsTbl.sql",
            "/sql/setup/setupMenuItemsTbl.sql",
            "/sql/setup/setupMenusTbl.sql",
            "/sql/setup/setupRecipeIngredientsTbl.sql",
            "/sql/setup/setupRecipesTbl.sql",
            "/sql/setup/setupSalesTbl.sql",
            "/sql/setup/setupSuppliersTbl.sql"
    };

    private final Connection dbConn;
    private final File dbFile;

    public DatabaseHandler(File dbFile) {
        this.dbFile = dbFile;
        try {
            dbConn = DriverManager.getConnection(format("jdbc:sqlite:%s", dbFile.getAbsolutePath()));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database.", e);
            //TODO: Handle errors so the app doesn't crash.
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
        InputStream is = DatabaseHandler.class.getResourceAsStream(resource);
        if (is == null) {
            throw new RuntimeException("Unable to find resource: " + resource);
        }
        return prepareStream(is);
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

    public Connection getConn() {
        return dbConn;
    }

    public File getDbFile() {
        return dbFile;
    }
}
