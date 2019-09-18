package kaitech.api.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

import java.util.List;

/**
 * Interface to represent ingredients.
 *
 * @author Julia Harrison
 */
public interface Ingredient {
    /**
     * @return The ingredient's unique string code.
     */
    String getCode();

    /**
     * @return The ingredient's name, may be null.
     */
    String getName();

    /**
     * @return The {@link UnitType} which the ingredient is measured in.
     */
    UnitType getUnit();

    /**
     * @return The {@link ThreeValueLogic} value identifying whether the ingredient is vegan.
     */
    ThreeValueLogic getIsVeg();

    /**
     * @return The {@link ThreeValueLogic} value identifying whether the ingredient is vegetarian.
     */
    ThreeValueLogic getIsVegan();

    /**
     * @return The {@link ThreeValueLogic} value identifying whether the ingredient is gluten free.
     */
    ThreeValueLogic getIsGF();

    /**
     * @return The price of the ingredient as a {@link Money} object.
     */
    Money getPrice();

    /**
     * @return A list of all Suppliers of the ingredient.
     */
    List<Supplier> getSuppliers();

    /**
     * Sets the name of the ingredient.
     *
     * @param name The new name of the ingredient.
     */
    void setName(String name);

    /**
     * Sets the unit of the ingredient, as an UnitType enum value.
     *
     * @param unit The {@link UnitType} of the ingredient.
     */
    void setUnit(UnitType unit);

    /**
     * Sets the price of the ingredient, as a Money object.
     *
     * @param price The price of the ingredient, as a {@link Money} object.
     */
    void setPrice(Money price);

    /**
     * Sets whether the ingredient is vegetarian, as a ThreeValueLogic enum value.
     *
     * @param isVeg The {@link ThreeValueLogic} value for whether the ingredient is vegetarian.
     */
    void setIsVeg(ThreeValueLogic isVeg);

    /**
     * Sets whether the ingredient is vegan, as a ThreeValueLogic enum value.
     *
     * @param isVegan The {@link ThreeValueLogic} value for whether the ingredient is vegan.
     */
    void setIsVegan(ThreeValueLogic isVegan);

    /**
     * Sets whether the ingredient is gluten free, as a ThreeValueLogic enum value.
     *
     * @param isGF The {@link ThreeValueLogic} value for whether the ingredient is gluten free.
     */
    void setIsGF(ThreeValueLogic isGF);


    /**
     * Sets the suppliers of the ingredient to be the passed in list.
     *
     * @param suppliers A List of suppliers which supply the ingredient.
     */
    void setSuppliers(List<Supplier> suppliers);

    /**
     * Adds a supplier to the list of suppliers of the ingredient.
     *
     * @param supplier The {@link Supplier} of the ingredient to be added.
     */
    void addSupplier(Supplier supplier);

    /**
     * Removes a supplier from the list of suppliers of the ingredient.
     *
     * @param supplier The {@link Supplier} of the ingredient to be removed.
     */
    void removeSupplier(Supplier supplier);

    /**
     * Gets the display name of the ingredient - if the ingredient's name is null, gets the ingredient's code.
     *
     * @return The String name of the ingredient if it is not null, or the String code if the name is null.
     */
    default String getDisplayName() {
        String name = getName();
        if (name == null) {
            name = getCode();
        }
        return name;
    }
}
