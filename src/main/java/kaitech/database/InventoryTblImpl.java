package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.IngredientTable;
import kaitech.api.database.InventoryTable;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * InventoryTblImpl extends AbstractTable, implements the InventoryTable interface, and permits limited access to the
 * data stored in the inventory table.
 *
 * @author Julia Harrison
 */
public class InventoryTblImpl extends AbstractTable implements InventoryTable {

    /**
     * The InventoryTable containing inventory data relating to the business, used by the InventoryTable.
     */
    private final IngredientTable ingredientTable;

    /**
     * Cache for the codes of ingredients in the inventory.
     */
    private final Set<String> codes = new HashSet<>();

    /**
     * Cache for ingredient quantities, stored as a Map of Ingredient to integer quantity.
     */
    private final Map<Ingredient, Integer> inventory = new HashMap<>();

    /**
     * Constructor for the InventoryTable.
     * On instantiation, greedy loads the codes of ingredients into cache.
     *
     * @param dbHandler       The DatabaseHandler to load the inventory from and save to.
     * @param ingredientTable The IngredientTable for the business, containing information about ingredients.
     */
    public InventoryTblImpl(DatabaseHandler dbHandler, IngredientTable ingredientTable) { //TODO: Throw exception GUI can catch
        super(dbHandler);
        this.ingredientTable = ingredientTable;
        PreparedStatement getCodesQuery = dbHandler.prepareStatement("SELECT ingredient FROM inventory;");
        ResultSet results;
        try {
            results = getCodesQuery.executeQuery();
            while (results.next()) {
                codes.add(results.getString("ingredient"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load inventory.", e);
        }
    }

    @Override
    public Integer getIngredientQuantity(Ingredient ingredient) { //TODO: Throw exception GUI can catch
        Integer quantity = inventory.get(ingredient);
        String code = ingredient.getCode();

        if (quantity == null && codes.contains(code)) {
            try {
                PreparedStatement getIngQuery = dbHandler.prepareStatement("SELECT quantity FROM inventory WHERE ingredient=?;");
                getIngQuery.setString(1, code);
                ResultSet results = getIngQuery.executeQuery();
                if (results.next()) {
                    quantity = results.getInt("quantity");
                    inventory.put(ingredient, quantity);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve ingredient from database.", e);
            }
        }

        return quantity;
    }

    @Override
    public Set<String> getAllStockCodes() {
        return Collections.unmodifiableSet(codes);
    }

    @Override
    public void putInventory(Ingredient ingredient, int quantity) throws IllegalArgumentException { //TODO: Throw exception GUI can catch
        if (quantity < 0) {
            throw new IllegalArgumentException("Unable to input ingredient with negative quantity.");
        }
        try {
            PreparedStatement putInvStmt = dbHandler.prepareResource("/sql/modify/insert/insertInventory.sql");
            String code = ingredient.getCode();
            putInvStmt.setString(1, code);
            putInvStmt.setInt(2, quantity);
            putInvStmt.executeUpdate();
            codes.add(code);
            inventory.put(ingredientTable.getOrAddIngredient(ingredient), quantity);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save inventory to database.", e);
        }
    }

    @Override
    public void removeInventory(Ingredient ingredient) { //TODO: Throw exception GUI can catch
        try {
            PreparedStatement removeInvStmt = dbHandler.prepareResource("/sql/modify/delete/deleteInventory.sql");
            String code = ingredient.getCode();
            removeInvStmt.setString(1, code);
            removeInvStmt.executeUpdate();
            codes.remove(code);
            inventory.remove(ingredient);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to remove inventory from database.", e);
        }
    }

    @Override
    public Map<Ingredient, Integer> resolveInventory() {
        return codes.stream() //
                .map(ingredientTable::getIngredient) //
                .collect(Collectors.toMap(Function.identity(), this::getIngredientQuantity));
    }

    @Override
    public void setQuantity(Ingredient ingredient, int newAmt) throws IllegalArgumentException { //TODO: Throw exception GUI can catch
        if (newAmt < 0) {
            throw new IllegalArgumentException("Cannot set ingredient with negative quantity.");
        }
        try {
            PreparedStatement updateStmt = dbHandler.prepareStatement("UPDATE inventory SET quantity=? WHERE ingredient=?;");
            updateStmt.setInt(1, newAmt);
            updateStmt.setString(2, ingredient.getCode());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update ingredient quantity in database.", e);
        }
        inventory.put(ingredient, newAmt);
    }

    @Override
    public void updateQuantity(Ingredient ingredient, int change) throws IllegalArgumentException { //TODO: Throw exception GUI can catch
        Integer currentQuant = getIngredientQuantity(ingredient);
        boolean notExists = currentQuant == null;

        if (change < 0 && notExists) {
            throw new IllegalArgumentException("The specified ingredient does not exist.");
        } else if (change < 0 && currentQuant + change < 0) {
            throw new IllegalArgumentException("Cannot decrease quantity by an amount greater than what the Business owns.");
        } else if (notExists) {
            putInventory(ingredient, change);
        } else {
            setQuantity(ingredient, currentQuant + change);
        }
    }

    @Override
    public void updateQuantities(Map<MenuItem, Integer> itemsOrdered) {
        for (Map.Entry<MenuItem, Integer> order : itemsOrdered.entrySet()) {
            Recipe recipe = order.getKey().getRecipe();
            Map<Ingredient, Integer> ingredients = recipe.getIngredients();
            for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
                updateQuantity(entry.getKey(), entry.getValue() * order.getValue() * -1);
            }
        }
    }
}
