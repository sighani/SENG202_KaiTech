package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.LoyaltyCardSettingsTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoyaltyCardSettingsTblImpl extends AbstractTable implements LoyaltyCardSettingsTable {

    private int percentageReturned;

    /**
     * Constructor for the Loyalty settings table
     * @param dbHandler
     */
    public LoyaltyCardSettingsTblImpl(DatabaseHandler dbHandler){
        super(dbHandler);
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT percentage FROM loyalty_settings");
        ResultSet results;
        try{
            results = stmt.executeQuery();
            percentageReturned = results.getInt("percentage");
        } catch (SQLException e){
            throw new RuntimeException("Failed to load loyalty card settings " + e);
        }
    }

    @Override
    public int getCurrentPercentage() {
        //need to get from db?
        return percentageReturned;

    }

    @Override
    public void setCurrentPercentage() {
        try{
            PreparedStatement stmt = dbHandler.prepareStatement("SELECT perce");
            ResultSet results = stmt.executeQuery();
        }catch (SQLException e){
            throw new RuntimeException("Cant set pin " + e);
        }
    }
}
