package kaitech.api.database;

public interface LoyaltyCardSettingsTable {
    /**
     * Get current percentage of each sale to be added to the
     * balance of the users loyalty card
     */
    int getCurrentPercentage();

    /**
     * Set the current percentage of each sale to be returned
     * @param newPercentage
     */
    void setCurrentPercentage(int newPercentage);

}
