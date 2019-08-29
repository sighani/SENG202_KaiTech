package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.api.model.Supplier;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

import java.util.List;
import java.util.Objects;

/**
 * Class to represent ingredients.
 */

public class IngredientImpl implements Ingredient {
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

    public IngredientImpl(String code, String name, UnitType unit, Money price, ThreeValueLogic isVeg,
                          ThreeValueLogic isVegan, ThreeValueLogic isGF) {
        this.code = code;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.isVeg = isVeg;
        this.isVegan = isVegan;
        this.isGF = isGF;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UnitType getUnit() {
        return unit;
    }

    @Override
    public ThreeValueLogic getIsVeg() {
        return isVeg;
    }

    @Override
    public ThreeValueLogic getIsVegan() {
        return isVegan;
    }

    @Override
    public ThreeValueLogic getIsGF() {
        return isGF;
    }

    @Override
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
        IngredientImpl that = (IngredientImpl) o;
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