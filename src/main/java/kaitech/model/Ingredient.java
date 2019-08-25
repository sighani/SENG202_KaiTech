package kaitech.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;

import java.util.List;
import java.util.Objects;
import org.joda.money.Money;

/**
 * Class to represent ingredients.
 */

public class Ingredient {
    /**
     * A list of all the suppliers that supply this ingredient.
     */
    List<Supplier> suppliers;

    /**
     * A short name to use in menus and elsewhere.
     */
    private String code;

    /**
     * The full name.
     */
    private String name;

    /**
     * The unit that the quantity of this ingredient is measured in.
     */
    private UnitType unit;

    /**
     * The cost of the ingredient.
     */
    private Money price;

    private ThreeValueLogic isVeg;
    private ThreeValueLogic isVegan;
    private ThreeValueLogic isGF;

    public Ingredient(String code, String name, UnitType unit, Money price, ThreeValueLogic isVeg,
                      ThreeValueLogic isVegan, ThreeValueLogic isGF) {
        this.code = code;
        this.name = name;
        this.unit = unit;
        this.price = price;
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
        return isVegan;
    }

    public ThreeValueLogic isGF() {
        return isGF;
    }

    public Money getPrice() {
        return price;
    }

    /**
     * Overrides the default equals such that comparisons of Ingredient objects compare the code instead.
     *
     * @param o The Ingredient to compare to which must be casted from an Object
     * @return A boolean, true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(code, that.code);
    }

    /**
     * A necessary override for the equals override to work properly. Hashes the code of the Ingredient instead.
     *
     * @return An int hash of the code
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}