package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.InventoryTable;
import kaitech.api.database.MenuItemTable;
import kaitech.api.database.SaleTable;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.model.SaleImpl;
import kaitech.util.PaymentType;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.money.Money;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableSet;

/**
 * SaleTblImpl extends AbstractTable, implements the SaleTable interface,
 * and permits limited access to the data stored in the Sales table.
 *
 * @author Julia Harrison
 */
public class SaleTblImpl extends AbstractTable implements SaleTable {
    private final MenuItemTable menuItemTable;
    private final InventoryTable inventoryTable;
    private final Set<Integer> receiptNumbers = new HashSet<>();
    private final Map<Integer, Sale> sales = new HashMap<>();
    private final String tableName = "sales";
    private final String tableKey = "receiptNumber";

    public SaleTblImpl(DatabaseHandler dbHandler, MenuItemTable menuItemTable, InventoryTable inventoryTable) { //TODO: Throw exception GUI can catch
        super(dbHandler);
        this.menuItemTable = menuItemTable;
        this.inventoryTable = inventoryTable;
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT receiptNumber FROM sales");
        ResultSet results;
        try {
            results = stmt.executeQuery();
            while (results.next()) {
                receiptNumbers.add(results.getInt("receiptNumber"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve sales.", e);
        }
    }

    private Map<MenuItem, Integer> getItemsOrdered(int receiptNo) { //TODO: Throw exception GUI can catch
        Map<MenuItem, Integer> itemsOrdered = new HashMap<>();
        try {
            PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM sales_items WHERE receiptNumber=?;");
            stmt.setInt(1, receiptNo);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                MenuItem menuItem = menuItemTable.getMenuItem(results.getString("menuItem"));
                int quantity = results.getInt("quantity");
                itemsOrdered.put(menuItem, quantity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve items ordered for specified sale.", e);
        }

        return itemsOrdered;
    }

    @Override
    public Sale getSale(int receiptNo) { //TODO: Throw exception GUI can catch
        Sale sale = sales.get(receiptNo);

        if (sale == null && receiptNumbers.contains(receiptNo)) {
            try {
                PreparedStatement getSaleQuery = dbHandler.prepareStatement("SELECT * FROM sales WHERE receiptNumber=?;");
                getSaleQuery.setInt(1, receiptNo);
                ResultSet results = getSaleQuery.executeQuery();
                if (results.next()) {
                    Map<MenuItem, Integer> itemsOrdered = getItemsOrdered(receiptNo);
                    PaymentType[] paymentTypes = PaymentType.values();
                    sale = new DbSale(receiptNo,
                            results.getDate("date").toLocalDate(),
                            results.getTime("time").toLocalTime(),
                            Money.parse(results.getString("price")),
                            paymentTypes[results.getInt("paymentType")],
                            results.getString("notes"),
                            itemsOrdered
                    );
                    sales.put(receiptNo, sale);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve sale from database.", e);
            }
        }

        return sale;
    }

    @Override
    public Set<Integer> getAllReceiptNo() {
        return unmodifiableSet(receiptNumbers);
    }

    @Override
    public Sale putSale(Sale from) { //TODO: Throw exception GUI can catch
        try {
            PreparedStatement putSaleStmt = dbHandler.prepareResource("/sql/modify/insert/insertSale.sql");
            putSaleStmt.setObject(1, null);
            putSaleStmt.setDate(2, Date.valueOf(from.getDate()));
            putSaleStmt.setTime(3, Time.valueOf(from.getTime()));
            putSaleStmt.setInt(4, from.getPaymentType().ordinal());
            putSaleStmt.setString(5, from.getNotes());
            putSaleStmt.setString(6, from.getTotalPrice().toString());
            putSaleStmt.executeUpdate();

            int receiptNumber;
            ResultSet results = putSaleStmt.getGeneratedKeys();
            if (results.next()) {
                receiptNumber = results.getInt(1);
            } else {
                throw new RuntimeException("Could not retrieve auto incremented ID for sale.");
            }

            Sale dbSale = new DbSale(receiptNumber, from);
            dbSale.setItemsOrdered(from.getItemsOrdered());
            receiptNumbers.add(receiptNumber);
            sales.put(receiptNumber, dbSale);
            inventoryTable.updateQuantities(dbSale.getItemsOrdered());
            return dbSale;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save sale to database.", e);
        }
    }

    @Override
    public void removeSale(int receiptNo) { //TODO: Throw exception GUI can catch
        try {
            PreparedStatement deleteStmt = dbHandler.prepareResource("/sql/modify/delete/deleteSale.sql");
            deleteStmt.setInt(1, receiptNo);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete sale from the database.", e);
        }
        receiptNumbers.remove(receiptNo);
        sales.remove(receiptNo);
    }

    @Override
    public Map<Integer, Sale> resolveAllSales() {
        return receiptNumbers.stream() //
                .map(this::getSale) //
                .collect(Collectors.toMap(Sale::getReceiptNumber, Function.identity()));
    }

    private class DbSale extends SaleImpl {
        private final Map<String, Object> key;

        public DbSale(int receiptNo, LocalDate date, LocalTime time, Money totalPrice, PaymentType paymentType,
                      String notes, Map<MenuItem, Integer> itemsOrdered) {
            super(receiptNo, date, time, totalPrice, paymentType, notes, itemsOrdered);
            key = singletonMap(tableKey, getReceiptNumber());
        }

        public DbSale(int receiptNo, Sale other) {
            super(receiptNo, other.getDate(), other.getTime(), other.getTotalPrice(), other.getPaymentType(),
                    other.getNotes(), other.getItemsOrdered());
            key = singletonMap(tableKey, getReceiptNumber());
        }

        @Override
        public void setItemsOrdered(Map<MenuItem, Integer> itemsOrdered) {
            int receiptNo = getReceiptNumber();
            List<List<Object>> values = new ArrayList<>();
            for (Map.Entry<MenuItem, Integer> entry : itemsOrdered.entrySet()) {
                values.add(Arrays.asList(receiptNo, entry.getKey().getCode(), entry.getValue()));
            }
            insertRows("sale_items", values);
            super.setItemsOrdered(itemsOrdered.entrySet().stream() //
                    .map(e -> Pair.of(menuItemTable.getOrAddItem(e.getKey()), e.getValue())) //
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue)) //
            );
        }

        @Override
        public void addItemToOrder(MenuItem item, int quantity) {
            try {
                PreparedStatement insertStmt = dbHandler.prepareResource("/sql/modify/insert/insertSaleItem.sql");
                insertStmt.setInt(1, getReceiptNumber());
                insertStmt.setString(2, item.getCode());
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to add item to sale.", e);
            }
            super.addItemToOrder(menuItemTable.getOrAddItem(item), quantity);
        }

        @Override
        public void removeItemFromOrder(MenuItem item) {
            try {
                PreparedStatement deleteStmt = dbHandler.prepareResource("/sql/modify/delete/deleteSaleItem.sql");
                deleteStmt.setInt(1, getReceiptNumber());
                deleteStmt.setString(2, item.getCode());
                deleteStmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to remove item from sale.", e);
            }
            super.removeItemFromOrder(item);
        }

        @Override
        public void changeOrderedQuantity(MenuItem item, int change) {
            Integer currentQuant = this.itemsOrdered.get(item);
            if (boundsCheckChange(currentQuant, change)) {
                try {
                    PreparedStatement updateStmt = dbHandler.prepareStatement("UPDATE sale_items SET quantity=? " +
                            "WHERE receiptNumber=? AND menuItem=?;");
                    updateStmt.setInt(1, currentQuant + change);
                    updateStmt.setInt(2, this.receiptNumber);
                    updateStmt.setString(3, item.getCode());
                    updateStmt.executeUpdate();
                    super.changeOrderedQuantity(menuItemTable.getOrAddItem(item), change);
                } catch (SQLException e) {
                    throw new RuntimeException("Unable to update item in sale.", e);
                }
            }
        }

        @Override
        public void setDate(LocalDate date) {
            updateColumn(tableName, key, "date", date);
            super.setDate(date);
        }

        @Override
        public void setTime(LocalTime time) {
            updateColumn(tableName, key, "time", time);
            super.setTime(time);
        }

        @Override
        public void setPaymentType(PaymentType paymentType) {
            updateColumn(tableName, key, "paymentType", paymentType.ordinal());
            super.setPaymentType(paymentType);
        }

        @Override
        public void setNotes(String notes) {
            updateColumn(tableName, key, "notes", notes);
            super.setNotes(notes);
        }

        @Override
        public void setTotalPrice(Money totalPrice) {
            updateColumn(tableName, key, "totalPrice", totalPrice.toString());
            super.setTotalPrice(totalPrice);
        }
    }
}
