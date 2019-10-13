package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.LoyaltyCardSettingsTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * LoyaltyCardSettingsTblImpl extends AbstractTable, implements the LoyaltyCardSettingsTable interface, and permits
 * limited access to the data stored in the loyalty_settings table.
 *
 * @author Michael Freeman
 */
public class LoyaltyCardSettingsTblImpl extends AbstractTable implements LoyaltyCardSettingsTable {

    private int percentageReturned;

    /**
     * Constructor for the Loyalty settings table
     *
     * @param dbHandler Database handler
     */
    public LoyaltyCardSettingsTblImpl(DatabaseHandler dbHandler) {
        super(dbHandler);
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT percentage_returned FROM loyalty_settings " +
                "WHERE id=1;");
        ResultSet results;
        try {
            results = stmt.executeQuery();
            if (!results.next()) {
                //create loyalty card percentage record with id = 1
                PreparedStatement insertStmt = dbHandler.prepareResource("/sql/modify/insert/insertLoyaltyCardPercentage.sql");
                insertStmt.setInt(1, 1);
                insertStmt.setInt(2, 0);
                insertStmt.executeUpdate();
                percentageReturned = 0;
            } else {
                while (results.next()) {
                    percentageReturned = results.getInt("percentage_returned");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load loyalty card settings " + e);
        }
    }


    @Override
    public int getCurrentPercentage() {
        //need to get from db - no is stored in temp memory
        try {
            PreparedStatement stmt = dbHandler.prepareStatement("SELECT percentage_returned FROM loyalty_settings " +
                    "WHERE id=1;");
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                percentageReturned = result.getInt("percentage_returned");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load percentage from database " + e);
        }
        return percentageReturned;
    }

    @Override
    public void setCurrentPercentage(int newPercentage) {
        Map<String, Object> key = singletonMap("id", 1);
        updateColumn("loyalty_settings", key, "percentage_returned", newPercentage);
        this.percentageReturned = newPercentage;
    }
}
