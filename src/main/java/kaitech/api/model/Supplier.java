package kaitech.api.model;

import kaitech.util.PhoneType;

public interface Supplier {
    String UNKNOWN_EMAIL = "Unknown email";
    String UNKNOWN_URL = "Unknown URL";

    void setPhoneType(PhoneType pt);

    void setEmail(String email);

    void setURL(String url);

    String toString();

    // Bunch of getters
    String id();

    String name();

    String address();

    String phone();

    PhoneType phoneType();

    String email();

    String url();
}
