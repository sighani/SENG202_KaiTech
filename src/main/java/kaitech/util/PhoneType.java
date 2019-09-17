package kaitech.util;

/**
 * Simple enum to illustrate XML attribute parsingin SAX example
 */
public enum PhoneType {
    MOBILE, WORK, HOME, UNKNOWN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}