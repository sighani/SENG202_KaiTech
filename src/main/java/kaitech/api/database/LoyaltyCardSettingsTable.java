package kaitech.api.database;

public interface LoyaltyCardSettingsTable {
    /**
     * Get currrent retruns percentage
     */
    int getCurrentPercentage();

    /**
     * Set the current returns percentage
     */
    void setCurrentPercentage();
}
