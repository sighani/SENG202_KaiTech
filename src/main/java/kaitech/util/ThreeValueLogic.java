package kaitech.util;

/**
 * Simple enum to illustrate three value logic for attributes
 */
public enum ThreeValueLogic {
    YES, NO, UNKNOWN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}