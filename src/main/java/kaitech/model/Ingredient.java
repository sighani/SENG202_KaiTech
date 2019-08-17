package kaitech.model;

import java.util.List;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;

/**
 * Class to represent ingredients. Mostly a data class, but that's the breaks...
 */

public class Ingredient {
    /**
     * A list of all the suppliers that supply this ingredient
     */
    List<Supplier> suppliers;

    /** A short name to use in menus and elsewhere */
    private String code;

    /** The full name */
    private String name;

    /**
     * The unit that the quantity of this ingredient is measured in.
     */
    private UnitType unit;
    private ThreeValueLogic isVeg;
    private ThreeValueLogic isVegan;
    private ThreeValueLogic isGF;

    private int price;

    public Ingredient(String code, String name, UnitType unit, ThreeValueLogic isVeg, ThreeValueLogic isVegan,
            ThreeValueLogic isGF) {
        this.code = code;
        this.name = name;
        this.unit = unit;
        this.isVeg = isVeg;
        this.isVegan = isVegan;
        this.isGF = isGF;
    }

    public String code() {
        return code;
    }

    public String name() {
        return name;
    }

    public UnitType unit() {
        return unit;
    }

    public ThreeValueLogic isVeg() {
        return isVeg;
    }

    public ThreeValueLogic isVegan() {
        return isVeg;
    }

    public ThreeValueLogic isGF() {
        return isGF;
    }

    public int getPrice() {
        return price;
    }
}