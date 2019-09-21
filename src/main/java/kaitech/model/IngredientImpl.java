package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.api.model.Supplier;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the {@link Ingredient} interface; used to store details about an Ingredient.
 */
public class IngredientImpl implements Ingredient {
    /**
     * A short name to use in menus and elsewhere.
     */
    protected final String code;

    /**
     * The full name.
     */
    protected String name;

    /**
     * The unit that the quantity of this ingredient is measured in.
     */
    protected UnitType unit;

    /**
     * The cost of the ingredient.
     */
    protected Money price;

    /**
     * ThreeValueLogic values for whether the ingredient is vegetarian, vegan, and/or gluten free.
     */
    protected ThreeValueLogic isVeg;
    protected ThreeValueLogic isVegan;
    protected ThreeValueLogic isGF;

    /**
     * A list of all the suppliers that supply this ingredient.
     */
    protected final List<Supplier> suppliers = new ArrayList<>();

    public IngredientImpl(String code) {
        this.code = code;
        this.unit = UnitType.UNKNOWN;
        this.price = Money.parse("USD -1");
        this.isVeg = ThreeValueLogic.UNKNOWN;
        this.isVegan = ThreeValueLogic.UNKNOWN;
        this.isGF = ThreeValueLogic.UNKNOWN;
    }

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
    public Money getPrice() {
        return price;
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
    public List<Supplier> getSuppliers() {
        //Unmodifiable so database can easily track changes.
        return Collections.unmodifiableList(suppliers);
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

    @Override
    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers.clear();
        this.suppliers.addAll(suppliers);
    }

    @Override
    public void addSupplier(Supplier supplier) {
        this.suppliers.add(supplier);
    }

    @Override
    public void removeSupplier(Supplier supplier) {
        this.suppliers.remove(supplier);
    }

    /**
     * Overrides the default equals.
     *
     * @param obj The Ingredient to compare to which must be cast from an Object
     * @return A boolean, true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof Ingredient)) return false;
        Ingredient other = (Ingredient) obj;
        return Objects.equals(other.getCode(), getCode());
    }

    /**
     * Hashes the Ingredient.
     *
     * @return An int hash of the code
     */
    @Override
    public int hashCode() {
        int i = 0;
        i = 31 * i + (getCode() == null ? 0 : getCode().hashCode());
        return i;
    }

}