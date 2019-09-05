package kaitech.api.model;

import kaitech.util.PhoneType;

public interface Supplier {
    String UNKNOWN_EMAIL = "Unknown email";
    String UNKNOWN_URL = "Unknown URL";

    void setSid(String sid);

    void setName(String name);

    void setAddress(String address);

    void setPhone(String phone);

    void setPhType(PhoneType phType);

    void setEmail(String email);

    void setUrl(String url);

    String toString();

    // Bunch of getters
    String getId();

    String getName();

    String getAddress();

    String getPhone();

    PhoneType getPhoneType();

    String getEmail();

    String getUrl();
}
