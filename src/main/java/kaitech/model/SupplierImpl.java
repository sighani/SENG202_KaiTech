package kaitech.model;

import kaitech.api.model.Supplier;
import kaitech.util.PhoneType;

import java.util.Objects;

/**
 * Implementation of the {@link Supplier} interface; used to store details about a Supplier.
 */
public class SupplierImpl implements Supplier {

    private final String sid;
    private String name;
    private String address;
    private String phone;
    private PhoneType phType;
    private String email = Supplier.UNKNOWN_EMAIL;
    private String url = Supplier.UNKNOWN_URL;

    public SupplierImpl(String s) {
        sid = s;
    }

    public SupplierImpl(String s, String name) {
        sid = s;
        this.name = name;
    }

    /**
     * Constructor for class. All the required elements
     *
     * @param s    String supplier ID
     * @param name Supplier name
     * @param addr Physical address
     * @param ph   Phone number as a string
     */
    public SupplierImpl(String s, String name, String addr, String ph) {
        this(s, name, addr, ph, PhoneType.UNKNOWN, Supplier.UNKNOWN_EMAIL, Supplier.UNKNOWN_URL);
    }

    /**
     * Constructor for class. All elements.
     *
     * @param sid     String supplier ID
     * @param name    Supplier name
     * @param address Physical address
     * @param phone   Phone number as a string
     * @param phType  Type of the phone number {@link PhoneType}
     * @param email   Email address
     * @param url     Website address
     */
    public SupplierImpl(String sid, String name, String address, String phone, PhoneType phType, String email, String url) {
        this.sid = sid;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.phType = phType;
        this.email = email;
        this.url = url;
    }

    @Override
    public String getId() {
        return this.sid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public String getPhone() {
        return this.phone;
    }

    @Override
    public PhoneType getPhoneType() {
        return this.phType;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void setPhoneType(PhoneType pt) {
        phType = pt;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof Supplier)) return false;
        Supplier other = (Supplier) obj;
        return Objects.equals(other.getId(), getId()) //
                && Objects.equals(other.getName(), getName()) //
                && Objects.equals(other.getAddress(), getAddress()) //
                && Objects.equals(other.getPhone(), getPhone()) //
                && Objects.equals(other.getPhoneType(), getPhoneType()) //
                && Objects.equals(other.getEmail(), getEmail()) //
                && Objects.equals(other.getUrl(), getUrl());
    }

    @Override
    public int hashCode() {
        int i = 0;
        i = 31 * i + (getId() == null ? 0 : getId().hashCode());
        i = 31 * i + (getName() == null ? 0 : getName().hashCode());
        i = 31 * i + (getAddress() == null ? 0 : getAddress().hashCode());
        i = 31 * i + (getPhone() == null ? 0 : getPhone().hashCode());
        i = 31 * i + (getPhoneType() == null ? 0 : getPhoneType().hashCode());
        i = 31 * i + (getEmail() == null ? 0 : getEmail().hashCode());
        i = 31 * i + (getUrl() == null ? 0 : getUrl().hashCode());
        return i;
    }

    @Override
    public String toString() {
        return "[Supplier: " + sid + ", " + name + ", " + address + ", " + phone + ", " + email + ", " + url + "]";
    }
}