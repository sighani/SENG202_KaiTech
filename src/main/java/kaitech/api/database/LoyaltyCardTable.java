package kaitech.api.database;

import kaitech.api.model.LoyaltyCard;
import org.joda.money.Money;

import java.util.Map;
import java.util.Set;

/**
 * The LoyaltyCardTable interface declares required functionality to store, retrieve, and modify loyalty cards in the
 * database.
 *
 * @author Julia Harrison
 */
public interface LoyaltyCardTable {

    /**
     * Retrieves a loyalty card from the cache. If the loyalty card does not currently exist in the cache, query the
     * database for the loyalty card. If the query is successful, add the loyalty card to the cache and return it.
     *
     * @param id The integer id of the loyalty card to retrieve.
     * @return An LoyaltyCard if the loyalty card exists, null if it does not or cannot be retrieved.
     */
    LoyaltyCard getLoyaltyCard(int id);

    /**
     * Get all loyalty card IDs currently in the database.
     *
     * @return A set of loyalty card IDs.
     */
    Set<Integer> getAllLoyaltyCardIDs();

    /**
     * Saves a given loyalty card to the database, and returns a database managed LoyaltyCard which is responsible for
     * the automatic saving of changes on setter calls.
     *
     * @param loyaltyCard The LoyaltyCard to be saved.
     * @return Database managed LoyaltyCard responsible for automatic saving on setter calls.
     */
    LoyaltyCard putLoyaltyCard(LoyaltyCard loyaltyCard);

    /**
     * Remove a loyalty card from the database and cache.
     *
     * @param id The ID of the loyalty card to be removed.
     */
    void removeLoyaltyCard(int id);

    /**
     * Gets all loyalty card objects from the database.
     * Warning: this can be a costly operation when everything isn't already cached, please use with care.
     *
     * @return A map from integer ID to LoyaltyCard.
     */
    Map<Integer, LoyaltyCard> resolveAllLoyaltyCards();
}
