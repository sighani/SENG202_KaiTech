package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.IngredientTable;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.model.IngredientImpl;
import kaitech.model.RecipeImpl;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

/**
 * RecipeTblImpl extends AbstractTable, implements the RecipeTable interface,
 * and permits limited access to the data stored in the Recipes table.
 *
 * @author Julia Harrison
 */
public class RecipeTblImpl extends AbstractTable implements RecipeTable {

    /**
     * The IngredientTable containing ingredient data relating to the business, used by the RecipeTable.
     */
    private final IngredientTable ingredientTable;

    /**
     * Cache for the ID numbers of recipes.
     */
    private final Set<Integer> idNumbers = new HashSet<>();

    /**
     * Cache for Recipes, stored as a Map from ID number to Recipe.
     */
    private final Map<Integer, Recipe> recipes = new HashMap<>();

    /**
     * The name of the table.
     */
    private final String tableName = "recipes";

    /**
     * The name of the primary key column of the table..
     */
    private final String tableKey = "recipeID";

    /**
     * Constructor for the RecipeTable.
     * On instantiation, greedy loads the ID numbers of recipes into cache.
     *
     * @param dbHandler       The DatabaseHandler to load the recipes from and save to.
     * @param ingredientTable The IngredientTable for the business, containing information about ingredients.
     */
    public RecipeTblImpl(DatabaseHandler dbHandler, IngredientTable ingredientTable) {
        super(dbHandler);
        this.ingredientTable = ingredientTable;
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT recipeID FROM recipes");
        ResultSet results;
        try {
            results = stmt.executeQuery();
            while (results.next()) {
                idNumbers.add(results.getInt("recipeID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve recipes.", e);
        }
    }

    /**
     * Gets the ingredients and their quantities required for the recipe.
     *
     * @return A Map of Ingredient to Integer required quantity.
     */
    private Map<Ingredient, Integer> getIngredients(int recipeID) { //TODO: Throw exception GUI can catch
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        try {
            PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM recipe_ingredients WHERE recipe=?;");
            stmt.setInt(1, recipeID);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                Ingredient ingredient = ingredientTable.getIngredient(results.getString("ingredient"));
                if (ingredient == null) {
                    ingredient = ingredientTable.putIngredient(new IngredientImpl(results.getString("ingredient")));
                }
                ingredients.put(ingredient, results.getInt("quantity"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve items ordered for specified sale.", e);
        }

        return ingredients;
    }

    @Override
    public Recipe getRecipe(int id) {
        Recipe recipe = recipes.get(id);

        if (recipe == null && idNumbers.contains(id)) {
            try {
                PreparedStatement getRecipeQuery = dbHandler.prepareStatement("SELECT * FROM recipes WHERE recipeID=?;");
                getRecipeQuery.setInt(1, id);
                ResultSet results = getRecipeQuery.executeQuery();
                if (results.next()) {
                    Map<Ingredient, Integer> ingredients = getIngredients(id);
                    recipe = new DbRecipe(id,
                            results.getString("name"),
                            ingredients,
                            results.getInt("preparationTime"),
                            results.getInt("cookingTime"),
                            results.getInt("numServings"));
                    recipes.put(id, recipe);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve sale from database.", e);
            }
        }

        return recipe;
    }

    @Override
    public Set<Integer> getAllIDNo() {
        return Collections.unmodifiableSet(idNumbers);
    }

    @Override
    public Recipe putRecipe(Recipe from) {
        try {
            PreparedStatement putStmt = dbHandler.prepareResource("/sql/modify/insert/insertRecipe.sql");
            putStmt.setObject(1, null);
            putStmt.setString(2, from.getName());
            putStmt.setInt(3, from.getPreparationTime());
            putStmt.setInt(4, from.getCookingTime());
            putStmt.setInt(5, from.getNumServings());
            putStmt.executeUpdate();

            int recipeID;
            ResultSet results = putStmt.getGeneratedKeys();
            if (results.next()) {
                recipeID = results.getInt(1);
            } else {
                throw new RuntimeException("Could not retrieve auto incremented ID for recipe.");
            }

            Recipe dbRecipe = new DbRecipe(recipeID, from);
            from.getIngredients().forEach(dbRecipe::addIngredient);
            idNumbers.add(recipeID);
            recipes.put(recipeID, dbRecipe);
            return dbRecipe;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save recipe to database.", e);
        }
    }

    private void deleteRelatedRows(int id) {
        try {
            PreparedStatement stmt = dbHandler.prepareStatement("DELETE FROM recipe_ingredients WHERE recipe=?;");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete rows from recipe_ingredients table.");
        }
    }

    @Override
    public void removeRecipe(int id) {
        try {
            PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/delete/deleteRecipe.sql");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            deleteRelatedRows(id);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to remove recipe from the database.", e);
        }
        recipes.remove(id);
        idNumbers.remove(id);
    }

    @Override
    public Map<Integer, Recipe> resolveAllRecipes() {
        return idNumbers.stream() //
                .map(this::getRecipe) //
                .collect(Collectors.toMap(Recipe::getID, Function.identity()));
    }

    /**
     * Database specific implementation of a recipe, which has database updating on attribute changes.
     */
    private class DbRecipe extends RecipeImpl {
        private final Map<String, Object> key;

        public DbRecipe(int recipeID, String name, Map<Ingredient, Integer> ingredients, int preparationTime,
                        int cookingTime, int numServings) {
            super(recipeID, name, preparationTime, cookingTime, numServings, ingredients);
            key = singletonMap(tableKey, getID());
        }

        public DbRecipe(int recipeID, Recipe from) {
            super(recipeID, from.getName(), from.getPreparationTime(), from.getCookingTime(), from.getNumServings(),
                    from.getIngredients());
            key = singletonMap(tableKey, getID());
        }

        @Override
        public void setName(String name) {
            updateColumn(tableName, key, "name", name);
            super.setName(name);
        }

        @Override
        public void setPreparationTime(int preparationTime) {
            updateColumn(tableName, key, "preparationTime", preparationTime);
            super.setPreparationTime(preparationTime);
        }

        @Override
        public void setCookingTime(int cookingTime) {
            updateColumn(tableName, key, "cookingTime", cookingTime);
            super.setCookingTime(cookingTime);
        }

        @Override
        public void setNumServings(int numServings) {
            updateColumn(tableName, key, "numServings", numServings);
            super.setNumServings(numServings);
        }

        @Override
        public void setIngredients(Map<Ingredient, Integer> ingredients) {
            try {
                PreparedStatement stmt = dbHandler.prepareStatement("DELETE FROM recipe_ingredients WHERE recipe=?;");
                stmt.setInt(1, recipeID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to clear existing ingredients for the recipe from the database.", e);
            }

            List<List<Object>> values = new ArrayList<>();
            for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
                values.add(Arrays.asList(recipeID, entry.getKey().getCode(), entry.getValue()));
            }
            insertRows("recipe_ingredients", values);
            super.setIngredients(ingredients.entrySet().stream() //
                    .map(e -> Pair.of(ingredientTable.getOrAddIngredient(e.getKey()), e.getValue())) //
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue)) //
            );
        }

        @Override
        public boolean addIngredient(Ingredient ingredient, int amt) {
            try {
                PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/insert/insertRecipeIngredient.sql");
                stmt.setInt(1, getID());
                stmt.setString(2, ingredient.getCode());
                stmt.setInt(3, amt);
                stmt.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                return false;
            } catch (SQLException e) {
                throw new RuntimeException("Unable to add ingredient to recipe.", e);
            }
            Ingredient dbIngredient = ingredientTable.getOrAddIngredient(ingredient);
            return super.addIngredient(dbIngredient, amt);
        }

        @Override
        public void updateIngredientAmount(Ingredient ingredient, int amt) {
            Ingredient dbIngredient = ingredientTable.getOrAddIngredient(ingredient);
            if (!getIngredients().containsKey(dbIngredient)) {
                throw new IllegalArgumentException("Ingredient to modify is not in the recipe.");
            }
            if (amt <= 0) {
                throw new IllegalArgumentException("New quantity must be a positive number.");
            }
            try {
                PreparedStatement stmt = dbHandler.prepareStatement("UPDATE recipe_ingredients SET quantity=? " +
                        "WHERE recipe=? AND ingredient=?;");
                stmt.setInt(1, amt);
                stmt.setInt(2, this.recipeID);
                stmt.setString(3, ingredient.getCode());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to update ingredient amount.", e);
            }
            super.updateIngredientAmount(dbIngredient, amt);
        }

        @Override
        public void removeIngredient(Ingredient ingredient) {
            try {
                PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/delete/deleteRecipeIngredient.sql");
                stmt.setString(1, ingredient.getCode());
                stmt.setInt(2, this.recipeID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to remove ingredient from database.", e);
            }
            super.removeIngredient(ingredient);
        }
    }
}
