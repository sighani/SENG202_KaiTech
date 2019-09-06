package kaitech.model;

import kaitech.api.model.Supplier;
import kaitech.util.PhoneType;

import java.util.Objects;

/**
 * Implementation of the {@link Supplier} interface.
 */
public class SupplierImpl implements Supplier {

    private String sid;
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
     */
    public SupplierImpl(String s, String name, String addr, String ph) {
        this(s, name, addr, ph, PhoneType.UNKNOWN, Supplier.UNKNOWN_EMAIL, Supplier.UNKNOWN_URL);
    }

    /**
     * Constructor for class. All elements.
     *
     * @param sid     Integer supplier ID
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
    public void setURL(String url) {
        this.url = url;
    }


    // Bunch of getters

    @Override
    public String getID() {
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
    public String getURL() {
        return this.url;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof Supplier)) return false;
        Supplier other = (Supplier) obj;
        return Objects.equals(other.getID(), getID()) //
                && Objects.equals(other.getName(), getName()) //
                && Objects.equals(other.getAddress(), getAddress()) //
                && Objects.equals(other.getPhone(), getPhone()) //
                && Objects.equals(other.getPhoneType(), getPhoneType()) //
                && Objects.equals(other.getEmail(), getEmail()) //
                && Objects.equals(other.getURL(), getURL());
    }

    @Override
    public int hashCode() {
        int i = 0;
        i = 31 * i + (getID() == null ? 0 : getID().hashCode());
        i = 31 * i + (getName() == null ? 0 : getName().hashCode());
        i = 31 * i + (getAddress() == null ? 0 : getAddress().hashCode());
        i = 31 * i + (getPhone() == null ? 0 : getPhone().hashCode());
        i = 31 * i + (getPhoneType() == null ? 0 : getPhoneType().hashCode());
        i = 31 * i + (getEmail() == null ? 0 : getEmail().hashCode());
        i = 31 * i + (getURL() == null ? 0 : getURL().hashCode());
        return i;
    }

    public String toString() {
        return "[Supplier: " + sid + ", " + name + ", " + address + ", " + phone + ", " + email + ", " + url + "]";
    }
}