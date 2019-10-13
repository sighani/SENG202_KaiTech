package kaitech.api.database;

public interface LoyaltyCardSettingsTable {
    /**
     * Get the current percentage, to be used in the calculation of how much of the value of a sale should
     * be added to the loyalty card's balance.
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
