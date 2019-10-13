package kaitech.api.database;

public interface LoyaltyCardSettingsTable {
    /**
     * Get current percentage of each sale to be added to the
     * balance of the users loyalty card
     */
    int getCurrentPercentage();

    /**
     * Set the percentage to be used in the calculation of how much of the value of a sale should be added
     * to the loyalty card's balance.
     *
     * @param newPercentage The new percentage
     */
    void setCurrentPercentage(int newPercentage);

}
