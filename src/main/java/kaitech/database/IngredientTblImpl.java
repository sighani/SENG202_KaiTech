package kaitech.database;

import kaitech.api.database.IngredientTable;
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
import java.util.stream.Collectors;

/**
 * IngredientTblImpl implements the IngredientTable interface, and permits limited access to the data stored in the
 * Ingredients table.
 *
 * @author Julia Harrison
 */
public class IngredientTblImpl implements IngredientTable { //TODO: Throw exception GUI can catch
    private final DatabaseHandler dbHandler;
    private final Set<String> codes = new HashSet<>();
    private final Map<String, Ingredient> ingredients = new HashMap<>();

    public IngredientTblImpl(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        PreparedStatement getCodesQuery = dbHandler.prepareStatement("SELECT code FROM ingredients");
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

    @Override
    public Ingredient getIngredient(String code) { //TODO: Throw exception GUI can catch
        Ingredient ing = ingredients.get(code);
        if (ing == null) {
            try {
                PreparedStatement getIngQuery = dbHandler.prepareStatement("SELECT * FROM ingredients WHERE code=?");
                getIngQuery.setString(1, code);
                ResultSet results = getIngQuery.executeQuery();
                if (results.next()) {
                    ThreeValueLogic[] logicValues = ThreeValueLogic.values();
                    ing = new DbIngredient(results.getString("code"),
                            results.getString("name"),
                            UnitType.values()[results.getInt("unit")],
                            Money.parse(results.getString("price")),
                            logicValues[results.getInt("isVeg")],
                            logicValues[results.getInt("isVegan")],
                            logicValues[results.getInt("isGF")]);
                    ingredients.put(code, ing);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve ingredient from database.", e);
            }
        }
        return ing;
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
                .collect(Collectors.toMap(Ingredient::getCode, e -> e));
    }

    private void updateColumn(String code, String colName, Object obj) { //TODO: Throw exception GUI can catch
        PreparedStatement stmt = dbHandler.prepareStatement(String.format("UPDATE ingredients SET %s=? WHERE code=?;",
                colName));
        try {
            stmt.setObject(1, obj);
            stmt.setString(2, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Unable to update %s with value %s.", colName, obj), e);
        }
    }

    private class DbIngredient extends IngredientImpl {
        public DbIngredient(String code) {
            super(code);
        }

        public DbIngredient(String code, String name, UnitType unit, Money price, ThreeValueLogic isVeg,
                            ThreeValueLogic isVegan, ThreeValueLogic isGF) {
            super(code, name, unit, price, isVeg, isVegan, isGF);
        }

        public DbIngredient(Ingredient other) {
            super(other.getCode(), other.getName(), other.getUnit(), other.getPrice(), other.getIsVeg(),
                    other.getIsVegan(), other.getIsGF());
            this.suppliers.addAll(other.getSuppliers());
        }

        @Override
        public void setName(String name) {
            updateColumn(getCode(), "name", name);
            super.setName(name);
        }

        @Override
        public void setUnit(UnitType unit) {
            updateColumn(getCode(), "unit", unit.ordinal());
            super.setUnit(unit);
        }

        @Override
        public void setPrice(Money price) {
            updateColumn(getCode(), "price", price.toString());
            super.setPrice(price);
        }

        @Override
        public void setIsVeg(ThreeValueLogic isVeg) {
            updateColumn(getCode(), "isVeg", isVeg.ordinal());
            super.setIsVeg(isVeg);
        }

        @Override
        public void setIsVegan(ThreeValueLogic isVegan) {
            updateColumn(getCode(), "isVegan", isVegan.ordinal());
            super.setIsVegan(isVegan);
        }

        @Override
        public void setIsGF(ThreeValueLogic isGF) {
            updateColumn(getCode(), "isGF", isGF.ordinal());
            super.setIsGF(isGF);
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
            super.addSupplier(supplier);
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
