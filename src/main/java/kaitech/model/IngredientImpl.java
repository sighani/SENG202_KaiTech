package kaitech.model;

import javafx.beans.property.SimpleStringProperty;
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
    public ThreeValueLogic isVeg() {
        return isVeg;
    }

    @Override
    public ThreeValueLogic isVegan() {
        return isVegan;
    }

    @Override
    public ThreeValueLogic isGF() {
        return isGF;
    }

    @Override
    public Money getPrice() {
        return price;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    @Override
    public void setPrice(Money price) {
        this.price = price;
    }

    @Override
    public void setIsVeg(ThreeValueLogic isVeg) {
        this.isVeg = isVeg;
    }

    @Override
    public void setIsVegan(ThreeValueLogic isVegan) {
        this.isVegan = isVegan;
    }

    @Override
    public void setIsGF(ThreeValueLogic isGF) {
        this.isGF = isGF;
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