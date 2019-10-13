package kaitech.api.database;

public interface LoyaltyCardSettingsTable {
    /**
     * Get current returns percentage
     * @return the current percentage as an integer
     */
    int getCurrentPercentage();

    /**
     * Set the current returns percentage
     * @param newPercentage the new int percentage for the business
     */
    void setCurrentPercentage(int newPercentage);

}
