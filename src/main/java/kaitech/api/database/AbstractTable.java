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

public abstract class AbstractTable {
    protected final DatabaseHandler dbHandler;

    protected AbstractTable(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

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
