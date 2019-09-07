package kaitech.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public interface Business extends Observer {
    void setSuppliers(List<Supplier> s);

    void addSupplier(Supplier s);

    void removeSupplier(Supplier s);

    boolean increaseIngredientQuantity(Ingredient i, int amt);

    boolean decreaseIngredientQuantity(Ingredient i, int amt);

    List<Supplier> getSuppliers();

    boolean addIngredient(Ingredient i);

    boolean addIngredient(Ingredient i, int amt);

    HashMap<Ingredient, Integer> getIngredients();

    void setIngredients(HashMap<Ingredient, Integer> ingredients);

    @Override
    void update(Observable sale, Object map);

    void setPin(String pin) throws IllegalArgumentException;

    boolean logIn(String attempt) throws IllegalStateException;

    void logOut();

    List<Sale> getRecords();

    /*
    Note that this is done for testing purposes. This getter should not be used anywhere else for security purposes.
     */
    String getPin();

    boolean isLoggedIn();

    boolean getPinIsNull();

    boolean checkForRecords();

    void addMenuItem(MenuItem newItem);
}
