package kaitech.api.model;

import kaitech.util.PhoneType;

/**
 * Basic info about suppliers.
 *
 * @author Julia Harrison
 */
public interface Supplier {
    /**
     * The default value for an email address if one is not provided on Supplier instantiation.
     */
    String UNKNOWN_EMAIL = "Unknown email";

    /**
     * The default value for a URL if one is not provided on Supplier instantiation.
     */
    String UNKNOWN_URL = "Unknown URL";

    void setName(String name);

    void setAddress(String address);

    void setPhone(String phone);

    /**
     * Set the type of the stored phone number as a {@link PhoneType} value.
     *
     * @param pt The {@link PhoneType} of the number.
     */
    void setPhoneType(PhoneType pt);

    /**
     * Set the String email of the supplier.
     *
     * @param email The email of the supplier, as a String.
     */
    void setEmail(String email);

    /**
     * Sets the String URL of the supplier.
     *
     * @param url The URL of the supplier's website, as a String.
     */
    void setURL(String url);

    /**
     * @return The ID of the supplier, as a String.
     */
    String getID();

    /**
     * @return The name of the supplier.
     */
    String getName();

    /**
     * @return The physical address of the supplier.
     */
    String getAddress();

    /**
     * @return The phone number of the supplier, as a String.
     */
    String getPhone();

    /**
     * @return The {@link PhoneType} of the stored phone number.
     */
    PhoneType getPhoneType();

    /**
     * @return The email address of the supplier.
     */
    String getEmail();

    /**
     * @return The URL of the supplier's website, as a String.
     */
    String getURL();
}
