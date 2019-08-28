package kaitech.api.model;

import kaitech.model.Business;
import kaitech.model.Recipe;
import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.util.List;

public interface MenuItem {
    boolean addIngredientToRecipe(Ingredient i, int amt);

    List<String> ingredientNames();

    String ingredients();

    boolean checkSufficientIngredients(Business toCheck);

    int calculateNumServings(Business toCheck);

    Recipe getRecipe();

    Money getPrice();

    List<String> getIngredients();

    void setIngredients(List<String> ingredients);

    String getName();

    void setName(String name);

    MenuItemType getType();

    String getCode();
}
