package kaitech.database;

import kaitech.api.database.IngredientTable;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.model.IngredientImpl;
import kaitech.model.RecipeImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TestRecipeDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private RecipeTable recipeTable;
    private IngredientTable ingredientTable;

    public void init() throws Throwable {
        dbHandler = new DatabaseHandler(tempFolder.newFile());
        PreparedStatement stmt = dbHandler.prepareResource("/sql/setup/setupRecipesTbl.sql");
        stmt.executeUpdate();
        stmt = dbHandler.prepareResource("/sql/setup/setupIngredientsTbl.sql");
        stmt.executeUpdate();
        stmt = dbHandler.prepareResource("/sql/setup/setupSuppliersTbl.sql");
        stmt.executeUpdate();
        stmt = dbHandler.prepareResource("/sql/setup/setupRecipeIngredientsTbl.sql");
        stmt.executeUpdate();
        ingredientTable = new IngredientTblImpl(dbHandler, new SupplierTblImpl(dbHandler));
        recipeTable = new RecipeTblImpl(dbHandler, ingredientTable);
    }

    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    private Recipe putRecipe(String name, Map<Ingredient, Integer> ingredients) {
        Recipe recipe = new RecipeImpl(name, ingredients);
        recipe = recipeTable.putRecipe(recipe);
        return recipe;
    }

    @Test
    public void testPutRecipe() throws Throwable {
        init();
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(new IngredientImpl("CAB"), 5);
        Recipe recipe = putRecipe("Salad", ingredients);
        recipe.setPreparationTime(1);
        recipe.setCookingTime(1);
        recipe.setNumServings(1);

        PreparedStatement getRecipeQuery = dbHandler.prepareStatement("SELECT * FROM recipes WHERE recipeID=?;");
        getRecipeQuery.setInt(1, recipe.getID());
        ResultSet results = getRecipeQuery.executeQuery();
        if (results.next()) {
            assertEquals("Salad", results.getString("name"));
            assertEquals(1, results.getInt("preparationTime"));
            assertEquals(1, results.getInt("cookingTime"));
            assertEquals(1, results.getInt("numServings"));
        } else {
            throw new RuntimeException("Could not retrieve recipe from database.");
        }

        // Test that the corresponding entry in the recipe ingredients table was created
        PreparedStatement getIngredStmt = dbHandler.prepareStatement("SELECT quantity FROM recipe_ingredients " +
                "WHERE recipe=? AND ingredient=?;");
        getIngredStmt.setInt(1, recipe.getID());
        getIngredStmt.setString(2, "CAB");
        ResultSet ingredResults = getIngredStmt.executeQuery();
        assertTrue(ingredResults.next());
        assertEquals(5, ingredResults.getInt("quantity"));

        teardown();
    }

    @Test
    public void testGetRecipe() throws Throwable {
        init();
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        Ingredient ingredient = new IngredientImpl("CAB");
        ingredients.put(ingredient, 5);
        ingredient = ingredientTable.getOrAddIngredient(ingredient);
        Recipe recipe = putRecipe("Salad", ingredients);
        int id = recipe.getID();
        ingredients.clear();
        ingredients.put(ingredient, 5);

        Recipe retrievedRecipe = recipeTable.getRecipe(id);
        assertEquals(ingredients, retrievedRecipe.getIngredients());
        assertEquals(recipe.getName(), retrievedRecipe.getName());
        assertEquals(recipe.getPreparationTime(), retrievedRecipe.getCookingTime());
        assertEquals(recipe.getCookingTime(), retrievedRecipe.getCookingTime());
        assertEquals(recipe.getNumServings(), retrievedRecipe.getNumServings());

        teardown();
    }

    @Test
    public void testGetAllIDNos() throws Throwable {
        init();
        Map<Ingredient, Integer> ingredients = Collections.singletonMap(new IngredientImpl("CAB"), 5);
        Recipe recipe1 = putRecipe("Salad A", ingredients);
        Recipe recipe2 = putRecipe("Salad B", ingredients);
        Set<Integer> recipeIDs = recipeTable.getAllIDNo();
        assertEquals(2, recipeIDs.size());
        assertTrue(recipeIDs.contains(recipe1.getID()));
        assertTrue(recipeIDs.contains(recipe2.getID()));
        teardown();
    }

    @Test
    public void testRemoveRecipe() throws Throwable {
        init();
        Map<Ingredient, Integer> ingredients = Collections.singletonMap(new IngredientImpl("CAB"), 5);
        Recipe recipe = putRecipe("Salad", ingredients);
        int id = recipe.getID();

        // Check the recipe exists in the cache/database
        assertNotNull(recipeTable.getRecipe(id));
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM recipes WHERE recipeID=?;");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        assertTrue(results.next());

        // Test the recipe gets removed
        recipeTable.removeRecipe(id);
        assertNull(recipeTable.getRecipe(id));
        results = stmt.executeQuery();
        assertFalse(results.next());

        teardown();
    }

    @Test
    public void testResolveAllRecipes() throws Throwable {
        init();
        Map<Ingredient, Integer> ingredients = Collections.singletonMap(new IngredientImpl("CAB"), 5);
        Recipe recipe1 = putRecipe("Salad A", ingredients);
        Recipe recipe2 = putRecipe("Salad B", ingredients);
        Map<Integer, Recipe> recipes = recipeTable.resolveAllRecipes();
        assertEquals(recipe1, recipes.get(recipe1.getID()));
        assertEquals(recipe2, recipes.get(recipe2.getID()));
        teardown();
    }
}
