package kaitech.model;

import kaitech.api.model.Supplier;
import kaitech.util.PhoneType;

/**
 * Basic info about out suppliers.
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
     * @param s     Integer supplier ID
     * @param n     Supplier name
     * @param addr  Physical address
     * @param ph    Phone number as a string
     * @param pt    Type of the phone number {@link PhoneType}
     * @param email Email address
     * @param url   Website address
     */
    public SupplierImpl(String s, String n, String addr, String ph, PhoneType pt, String email, String url) {
        sid = s;
        name = n;
        address = addr;
        phone = ph;
        phType = pt;
        this.email = email;
        this.url = url;
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

    @Override
    public String toString() {
        return "[Supplier: " + sid + ", " + name + ", " + address + ", " + phone + ", " + email + ", " + url + "]";
    }

    // Bunch of getters
    @Override
    public String id() {
        return this.sid;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String address() {
        return this.address;
    }

    @Override
    public String phone() {
        return this.phone;
    }

    @Override
    public PhoneType phoneType() {
        return this.phType;
    }

    @Override
    public String email() {
        return this.email;
    }

    @Override
    public String url() {
        return this.url;
    }
}