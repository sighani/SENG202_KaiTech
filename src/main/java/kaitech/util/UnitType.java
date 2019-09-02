package kaitech.util;

/**
 * Simple enum to illustrate XML attribute parsingin SAX example
 */
public enum UnitType {
    GRAM,
    ML,
    COUNT,
    UNKNOWN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}