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

/**
 * The DatabaseHandler class controls the setup of database tables, connection to the database, and handles the creation
 * of PreparedStatements for the database to run.
 *
 * @author Julia Harrison
 */
public class DatabaseHandler {

    /**
     * An array of setup script filepaths for the database handler to loop over and run individually.
     */
    private static final String[] setupScripts = {
            "/sql/setup/setupIngredientsTbl.sql",
            "/sql/setup/setupIngredientSuppliersTbl.sql",
            "/sql/setup/setupIngredNamesTbl.sql",
            "/sql/setup/setupInventoryTbl.sql",
            "/sql/setup/setupMenuContentsTbl.sql",
            "/sql/setup/setupMenuItemsTbl.sql",
            "/sql/setup/setupMenusTbl.sql",
            "/sql/setup/setupRecipeIngredientsTbl.sql",
            "/sql/setup/setupRecipesTbl.sql",
            "/sql/setup/setupSaleItemsTbl.sql",
            "/sql/setup/setupSalesTbl.sql",
            "/sql/setup/setupSuppliersTbl.sql"
    };

    /**
     * The {@link Connection} to the database.
     */
    private final Connection dbConn;

    /**
     * The database file.
     */
    private final File dbFile;

    public DatabaseHandler(File dbFile) { //TODO: Throw exception GUI can catch
        this.dbFile = dbFile;
        try {
            dbConn = DriverManager.getConnection(format("jdbc:sqlite:%s", dbFile.getAbsolutePath()));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database.", e);
        }
    }

    /**
     * Runs the setup of all tables in the database from the setup scripts declared in
     * {@link DatabaseHandler#setupScripts}.
     */
    public void setup() { //TODO: Throw exception GUI can catch
        for (String script : setupScripts) {
            PreparedStatement stmt = prepareResource(script);
            try {
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(format("Unable to run setup script: %s.", script), e);
            }
        }
    }

    /**
     * Creates a PreparedStatement from the given SQL script file.
     *
     * @param resource The filepath of the SQL script.
     * @return A {@link PreparedStatement} from the given SQL script.
     */
    public PreparedStatement prepareResource(String resource) { //TODO: Throw exception GUI can catch
        InputStream is = DatabaseHandler.class.getResourceAsStream(resource);
        if (is == null) {
            throw new RuntimeException("Unable to find resource: " + resource);
        }
        return prepareStream(is);
    }

    /**
     * Creates a PreparedStatement from the given input stream.
     *
     * @param inputStream The input stream to create a PreparedStatement from.
     * @return A {@link PreparedStatement} from the given input stream.
     */
    public PreparedStatement prepareStream(InputStream inputStream) { //TODO: Throw exception GUI can catch
        try (InputStream is = inputStream) {
            return prepareStatement(IOUtils.toString(is, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse InputStream to String.", e);
        }
    }

    /**
     * Creates a PreparedStatement from the given String SQL query.
     *
     * @param sql The SQL query, as a String.
     * @return A {@link PreparedStatement} from the given String SQL query.
     */
    public PreparedStatement prepareStatement(String sql) { //TODO: Throw exception GUI can catch
        try {
            return dbConn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to prepare statement.", e);
        }
    }

    /**
     * @return The database {@link Connection}.
     */
    public Connection getConn() {
        return dbConn;
    }

    /**
     * @return The database file.
     */
    public File getDbFile() {
        return dbFile;
    }

    /**
     * Drops all database tables.
     * WARNING: This action is irreversible, and completely erases all data.
     */
    public void dropAllTables() {
        String[] tables = {"ingredients", "ingredient_suppliers", "ingredient_names","inventory", "menu_contents",
                "menu_items", "menus", "recipe_ingredients", "recipes", "sale_items", "sales", "suppliers"};
        try {
            for (String table : tables) {
                prepareStatement(String.format("DROP TABLE IF EXISTS %s;", table)).executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to drop all database tables.", e);
        }
    }
}
