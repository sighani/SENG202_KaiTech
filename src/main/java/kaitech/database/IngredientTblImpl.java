package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.IngredientTable;
import kaitech.api.database.SupplierTable;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Supplier;
import kaitech.model.IngredientImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

/**
 * IngredientTblImpl extends AbstractTable, implements the IngredientTable interface, and permits limited access to the
 * data stored in the ingredients table.
 *
 * @author Julia Harrison
 */
public class IngredientTblImpl extends AbstractTable implements IngredientTable {
    private final SupplierTable supplierTable;
    private final Set<String> codes = new HashSet<>();
    private final Map<String, Ingredient> ingredients = new HashMap<>();
    private final String tableName = "ingredients";
    private final String tableKey = "code";

    public IngredientTblImpl(DatabaseHandler dbHandler, SupplierTable supplierTable) { //TODO: Throw exception GUI can catch
        super(dbHandler);
        this.supplierTable = supplierTable;
        PreparedStatement getCodesQuery = dbHandler.prepareStatement("SELECT code FROM ingredients;");
        ResultSet results;
        try {
            results = getCodesQuery.executeQuery();
            while (results.next()) {
                codes.add(results.getString("code"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load ingredients.", e);
        }
    }

    /**
     * Gets the suppliers of the ingredient. Should only be called by getIngredient in this class.
     *
     * @param ingredientCode The code of the ingredient to get the suppliers of.
     * @return A List of Suppliers that supply the ingredient.
     * @throws SQLException Thrown when the database fails to prepare the get statement or retrieve the suppliers.
     */
    private List<Supplier> getSuppliers(String ingredientCode) throws SQLException {
        PreparedStatement getSuppCodesQuery = dbHandler.prepareStatement("SELECT supplier FROM ingredient_suppliers WHERE ingredient=?;");
        getSuppCodesQuery.setString(1, ingredientCode);
        ResultSet supplierCodes = getSuppCodesQuery.executeQuery();
        List<Supplier> suppliers = new ArrayList<>();
        while (supplierCodes.next()) {
            suppliers.add(supplierTable.getSupplier(supplierCodes.getString("supplier")));
        }
        return suppliers;
    }

    @Override
    public Ingredient getIngredient(String code) { //TODO: Throw exception GUI can catch
        Ingredient ingredient = ingredients.get(code);

        if (ingredient == null && codes.contains(code)) {
            try {
                PreparedStatement getIngQuery = dbHandler.prepareStatement("SELECT * FROM ingredients WHERE code=?;");
                getIngQuery.setString(1, code);
                ResultSet results = getIngQuery.executeQuery();
                if (results.next()) {
                    ThreeValueLogic[] logicValues = ThreeValueLogic.values();
                    ingredient = new DbIngredient(code,
                            results.getString("name"),
                            UnitType.values()[results.getInt("unit")],
                            Money.parse(results.getString("price")),
                            logicValues[results.getInt("isVeg")],
                            logicValues[results.getInt("isVegan")],
                            logicValues[results.getInt("isGF")]);

                    try {
                        List<Supplier> suppliers = getSuppliers(code);
                        ((DbIngredient) ingredient).setSuppliers(suppliers);
                    } catch (SQLException e) {
                        System.out.println("Unable to retrieve suppliers of the ingredient. " +
                                "Leaving suppliers field null.");
                    }

                    ingredients.put(code, ingredient);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve ingredient from database.", e);
            }
        }

        return ingredient;
    }

    @Override
    public Set<String> getAllIngredientCodes() {
        return Collections.unmodifiableSet(codes);
    }

    @Override
    public Ingredient putIngredient(Ingredient from) { //TODO: Throw exception GUI can catch
        try {
            PreparedStatement putIngStmt = dbHandler.prepareResource("/sql/modify/insert/insertIngredient.sql");
            String code = from.getCode();
            putIngStmt.setString(1, code);
            putIngStmt.setString(2, from.getName());
            putIngStmt.setInt(3, from.getUnit().ordinal());
            putIngStmt.setString(4, from.getPrice().toString());
            putIngStmt.setInt(5, from.getIsVeg().ordinal());
            putIngStmt.setInt(6, from.getIsVegan().ordinal());
            putIngStmt.setInt(7, from.getIsGF().ordinal());
            putIngStmt.executeUpdate();
            Ingredient dbIngredient = new DbIngredient(from);
            from.getSuppliers().forEach(dbIngredient::addSupplier);
            codes.add(code);
            ingredients.put(code, dbIngredient);
            return dbIngredient;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save ingredient to database.", e);
        }
    }

    @Override
    public void removeIngredient(String code) { //TODO: Throw exception GUI can catch
        try {
            PreparedStatement removeIngStmt = dbHandler.prepareResource("/sql/modify/delete/deleteIngredient.sql");
            removeIngStmt.setString(1, code);
            removeIngStmt.executeUpdate();
            codes.remove(code);
            ingredients.remove(code);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete ingredient from database.", e);
        }
    }

    @Override
    public Map<String, Ingredient> resolveAllIngredients() {
        return codes.stream() //
                .map(this::getIngredient) //
                .collect(Collectors.toMap(Ingredient::getCode, Function.identity()));
    }

    private class DbIngredient extends IngredientImpl {
        private final Map<String, Object> key;

        public DbIngredient(String code) {
            super(code);
            key = singletonMap(tableKey, getCode());
        }

        public DbIngredient(String code, String name, UnitType unit, Money price, ThreeValueLogic isVeg,
                            ThreeValueLogic isVegan, ThreeValueLogic isGF) {
            super(code, name, unit, price, isVeg, isVegan, isGF);
            key = singletonMap(tableKey, getCode());
        }

        public DbIngredient(Ingredient other) {
            super(other.getCode(), other.getName(), other.getUnit(), other.getPrice(), other.getIsVeg(),
                    other.getIsVegan(), other.getIsGF());
            other.getSuppliers().forEach(this::addSupplier);
            key = singletonMap(tableKey, getCode());
        }

        @Override
        public void setName(String name) {
            updateColumn(tableName, key, "name", name);
            super.setName(name);
        }

        @Override
        public void setUnit(UnitType unit) {
            updateColumn(tableName, key, "unit", unit.ordinal());
            super.setUnit(unit);
        }

        @Override
        public void setPrice(Money price) {
            updateColumn(tableName, key, "price", price.toString());
            super.setPrice(price);
        }

        @Override
        public void setIsVeg(ThreeValueLogic isVeg) {
            updateColumn(tableName, key, "isVeg", isVeg.ordinal());
            super.setIsVeg(isVeg);
        }

        @Override
        public void setIsVegan(ThreeValueLogic isVegan) {
            updateColumn(tableName, key, "isVegan", isVegan.ordinal());
            super.setIsVegan(isVegan);
        }

        @Override
        public void setIsGF(ThreeValueLogic isGF) {
            updateColumn(tableName, key, "isGF", isGF.ordinal());
            super.setIsGF(isGF);
        }

        private void setSuppliers(List<Supplier> suppliers) {
            super.suppliers.clear();
            super.suppliers.addAll(suppliers);
        }

        @Override
        public void addSupplier(Supplier supplier) { //TODO: Throw exception GUI can catch
            try {
                PreparedStatement insertStmt = dbHandler.prepareResource("/sql/modify/insert/insertIngredientSupplier.sql");
                insertStmt.setString(1, getCode());
                insertStmt.setString(2, supplier.getID());
                insertStmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to add supplier.", e);
            }
            super.addSupplier(supplierTable.getOrAddSupplier(supplier));
        }

        @Override
        public void removeSupplier(Supplier supplier) { //TODO: Throw exception GUI can catch
            try {
                PreparedStatement removeStmt = dbHandler.prepareResource("/sql/modify/delete/deleteIngredientSupplier.sql");
                removeStmt.setString(1, getCode());
                removeStmt.setString(2, supplier.getID());
                removeStmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to remove supplier.", e);
            }
            super.removeSupplier(supplier);
        }
    }
}
