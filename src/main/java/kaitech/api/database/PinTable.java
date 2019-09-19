package kaitech.api.database;

import java.util.Set;

/**
 * The PinTable interface declares required functionality to store, retrieve, and modify PINs in the database.
 *
 * @author Julia Harrison
 */
public interface PinTable {

    /**
     * @return A set of all String names of pins in the database.
     */
    Set<String> getAllNames();

    /**
     * Retrieves a pin from the cache. If the pin does not currently exist in the cache, query the
     * database for the pin. If the query is successful, add the pin to the cache and return it.
     *
     * @param name The name associated with the pin to be retrieved.
     * @return The char array pin if it exists, null if it does not or cannot be retrieved.
     */
    String getHashedPin(String name);

    /**
     * Saves a given String name and String pin to the database.
     *
     * @param name      The String name for the pin.
     * @param hashedPin The hashed and salted new String pin to be added.
     * @param salt      The salt for the pin.
     */
    void putPin(String name, String hashedPin, String salt);

    /**
     * Removes the pin from the database.
     *
     * @param name The String name of the pin to be deleted.
     */
    void removePin(String name);

    /**
     * Updates the pin of the associated name with the new hashed pin value.
     *
     * @param name         The String name of the pin to be changed.
     * @param newHashedPin The new String hashed pin for the associated name.
     */
    void updatePin(String name, String newHashedPin);

    /**
     * Gets the salt associated with the name.
     *
     * @param name The name of the pin to get the salt of.
     * @return The String salt.
     */
    String getSalt(String name);

    /**
     * Updates the salt of the given name.
     *
     * @param name    The name of the pin to update the salt of.
     * @param newSalt The new salt.
     */
    void updateSalt(String name, String newSalt);

    /**
     * Generates a randomized String salt of given length.
     *
     * @param length The length of the random salt to generate.
     * @return A new random salt of given length.
     */
    String generateSalt(int length);

    /**
     * Hashes a given pin with the salt.
     *
     * @param pin  The pin to hash.
     * @param salt The salt to hash the pin with.
     * @return A salted pin.
     */
    String hashPin(CharSequence pin, String salt);
}
