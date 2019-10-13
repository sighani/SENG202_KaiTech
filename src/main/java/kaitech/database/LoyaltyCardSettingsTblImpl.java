package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.LoyaltyCardSettingsTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoyaltyCardSettingsTblImpl extends AbstractTable implements LoyaltyCardSettingsTable {

    private int percentageReturned;

    /**
     * Constructor for the Loyalty settings table, takes a Database Handler
     * @param dbHandler
     */
    public LoyaltyCardSettingsTblImpl(DatabaseHandler dbHandler){
        super(dbHandler);
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT percentage_returned FROM loyalty_settings WHERE id=1");
        ResultSet results;
        try{
            results = stmt.executeQuery();
            if(results.next() == false){
                //create new record with id 1
                PreparedStatement insertStmt = dbHandler.prepareStatement("INSERT INTO loyalty_settings (id, percentage_returned) VALUES (?, ?);");
                insertStmt.setInt(1, 1);
                insertStmt.setInt(2, 0);
                insertStmt.executeUpdate();
                percentageReturned = 0;
            }else{
                while(results.next()) {
                    percentageReturned = results.getInt("percentage_returned");
                    System.out.println(percentageReturned);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Failed to load loyalty card settings " + e);
        }
    }


    @Override
    public int getCurrentPercentage() {
        //need to get from db - no is stored in temp memory
        try{
            PreparedStatement stmt = dbHandler.prepareStatement("SELECT percentage_returned FROM loyalty_settings WHERE id=1");
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                percentageReturned = result.getInt("percentage_returned");
            }
        }catch (SQLException e){
            throw new RuntimeException("Couldnt load percentage from database " + e);
        }
        return percentageReturned;
    }

    @Override
    public void setCurrentPercentage(int newPercentage) {
        try{
            //now we need to sync up to the database
            PreparedStatement stmt = dbHandler.prepareStatement("UPDATE loyalty_settings SET percentage_returned = (?) WHERE id=1;");
            stmt.setInt(1, newPercentage);
            stmt.execute();
            this.percentageReturned = newPercentage;
        }catch (SQLException e){
            throw new RuntimeException("Cant set new loyalty card percentage " + e);
        }
    }
}
