package kaitech.api.database;

public interface LoyaltyCardSettingsTable {
    /**
     * Get the current percentage, to be used in the calculation of how much of the value of a sale should
     * be added to the loyalty card's balance.
     * @return the current percentage as an integer
     */
    int getCurrentPercentage();

    /**
     * @param newPercentage the new int percentage for the business
     */
    void setCurrentPercentage(int newPercentage);

}
