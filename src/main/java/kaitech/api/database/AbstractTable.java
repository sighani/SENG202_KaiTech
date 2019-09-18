package kaitech.api.database;

import kaitech.database.DatabaseHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The AbstractTable class is, as it is named, an abstract class providing utility methods to the
 * database table implementation classes. It provides updateColumn, which allows the generic updating
 * of a single value in a specified table, and insertRows, which allows the mass insertion of rows
 * into a table.
 *
 * @author Julia Harrison
 */
public abstract class AbstractTable {
    protected final DatabaseHandler dbHandler;

    protected AbstractTable(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    /**
     * Update a specific column (row value) in a database table with a given object.
     *
     * @param tableName The name of the table to update.
     * @param keyValues A map of String to Object, where String is a column name and Object is the
     *                  value of a/the key of the row to change.
     * @param colName   The name of the column to update a value within.
     * @param obj       The new value for the specified column.
     */
    protected void updateColumn(String tableName, Map<String, Object> keyValues, String colName, Object obj) { //TODO: Throw exception GUI can catch
        List<Object> values = new ArrayList<>();
        values.add(obj);
        StringBuilder stmt = new StringBuilder(String.format("UPDATE %s SET %s=? WHERE", tableName, colName));
        boolean first = true;
        for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
            stmt.append(" ");
            if (!first) {
                stmt.append("AND ");
            }
            first = false;
            stmt.append(entry.getKey()).append("=?");
            values.add(entry.getValue());
        }
        stmt.append(";");

        try {
            PreparedStatement prepStmt = dbHandler.prepareStatement(stmt.toString());
            for (int i = 0; i < values.size(); i++) {
                prepStmt.setObject(i + 1, values.get(i));
            }
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update column.", e);
        }
    }

    /**
     * Given the name of a table and a list of lists of objects, for each of the lists in the list,
     * insert the objects into the given table. Used in places such as the ingredients class; when
     * you set the ingredient's suppliers to a list of suppliers, each supplier needs to be added
     * to the ingredient_suppliers table alongside the ingredient.
     *
     * @param tblName    The name of the table to add the objects to.
     * @param valueLists A list of lists of objects, where each list of objects corresponds
     *                   to a row to be added to the database.
     */
    protected void insertRows(String tblName, List<List<Object>> valueLists) {
        if (valueLists.size() < 1) {
            return;
        }
        List<String> colNames = new ArrayList<>();
        try {
            DatabaseMetaData metaData = dbHandler.getConn().getMetaData();
            ResultSet results = metaData.getColumns(null, null, tblName, null);
            while (results.next()) {
                colNames.add(results.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Unable to retrieve information about table %s.", tblName), e);
        }

        String stmtString = "INSERT INTO %s (%s) VALUES %s;";
        String cols = String.join(", ", colNames);
        String qnMarkBrackets = "(" + StringUtils.repeat("?", ", ", valueLists.get(0).size()) + ")";
        String qtnMarks = StringUtils.repeat(qnMarkBrackets, ", ", valueLists.size());

        try {
            PreparedStatement stmt = dbHandler.prepareStatement(String.format(stmtString, tblName, cols, qtnMarks));
            int i = 1;
            for (List<Object> values : valueLists) {
                for (Object obj : values) {
                    stmt.setObject(i++, obj);
                }
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save to database.", e);
        }
    }
}
