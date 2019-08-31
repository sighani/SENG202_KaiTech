package kaitech.api.model;

import org.joda.money.Money;

import java.util.Map;

public interface Recipe {
    Money calculateTotalCost();

    Map<Ingredient, Integer> getIngredients();

    boolean addIngredient(Ingredient i, int amt);

    void updateIngredientQuantity(Ingredient i, int amt);

    void removeIngredient(Ingredient i);
}
