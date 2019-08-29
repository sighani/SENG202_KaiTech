package kaitech.api.model;

import kaitech.util.PhoneType;

public interface Supplier {
    String UNKNOWN_EMAIL = "Unknown email";
    String UNKNOWN_URL = "Unknown URL";

    void setPhoneType(PhoneType pt);

    void setEmail(String email);

    void setURL(String url);

    String toString();

    String getID();

    String getName();

    String getAddress();

    String getPhone();

    PhoneType getPhoneType();

    String getEmail();

    String getURL();
}
