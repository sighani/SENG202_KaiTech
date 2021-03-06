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

    /**
     * The database tables containing data relating to the business, used by the SaleTable.
     */
    private final MenuItemTable menuItemTable;
    private final InventoryTable inventoryTable;

    /**
     * Cache for the receipt numbers of sales.
     */
    private final Set<Integer> receiptNumbers = new HashSet<>();

    /**
     * Cache for Sales, stored as a Map from receipt number to Sale.
     */
    private final Map<Integer, Sale> sales = new HashMap<>();

    /**
     * The name of the table.
     */
    private final String tableName = "sales";

    /**
     * The name of the primary key column of the table..
     */
    private final String tableKey = "receiptNumber";

    /**
     * Constructor for the SaleTable.
     * On instantiation, greedy loads the receipt numbers of sales into cache.
     *
     * @param dbHandler      The DatabaseHandler to load the suppliers from and save to.
     * @param menuItemTable  The MenuItemTable for the business, containing information about menu items.
     * @param inventoryTable The InventoryTable for the business, containing information about inventory.
     */
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

    /**
     * Get the menu items ordered and the quantity of the order for a given receipt number.
     *
     * @param receiptNo The receipt number of the sale to retrieve the items of.
     * @return A Map of MenuItem to Integer, representing the menu item ordered and its quantity.
     */
    private Map<MenuItem, Integer> getItemsOrdered(int receiptNo) { //TODO: Throw exception GUI can catch
        Map<MenuItem, Integer> itemsOrdered = new HashMap<>();
        try {
            PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM sale_items WHERE receiptNumber=?;");
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
                            Money.parse(results.getString("totalPrice")),
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

    /**
     * Database specific implementation of a sale, which has database updating on attribute changes.
     */
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
            try {
                PreparedStatement stmt = dbHandler.prepareStatement("DELETE FROM sale_items WHERE receiptNumber=?;");
                stmt.setInt(1, receiptNumber);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to clear existing items ordered for the sale from the database.", e);
            }

            Money cashOut = Money.parse("NZD 0");
            if (!totalPrice.isEqual(Sale.calculateTotalCost(this.getItemsOrdered()))) {
                // Cash out must have occurred, as discounts are not yet implemented
                cashOut = totalPrice.minus(Sale.calculateTotalCost(this.getItemsOrdered()));
            }
            setTotalPrice(cashOut.plus(Sale.calculateTotalCost(itemsOrdered)));

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
                int receiptNo = getReceiptNumber();
                String code = item.getCode();
                PreparedStatement selectStmt = dbHandler.prepareStatement("SELECT quantity FROM sale_items " +
                        "WHERE receiptNumber=? AND menuItem=?;");
                selectStmt.setInt(1, receiptNo);
                selectStmt.setString(2, code);
                ResultSet results = selectStmt.executeQuery();
                if (results.next()) {
                    // Adding existing item to order
                    changeOrderedQuantity(item, quantity);
                } else {
                    // Adding new item to order
                    PreparedStatement insertStmt = dbHandler.prepareResource("/sql/modify/insert/insertSaleItem.sql");
                    insertStmt.setInt(1, receiptNo);
                    insertStmt.setString(2, code);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                    setTotalPrice(totalPrice.plus(item.getPrice().multipliedBy(quantity)));
                    super.addItemToOrder(menuItemTable.getOrAddItem(item), quantity);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to add item to sale.", e);
            }
        }

        @Override
        public void removeItemFromOrder(MenuItem item) {
            try {
                int receiptNo = getReceiptNumber();
                PreparedStatement selectStmt = dbHandler.prepareStatement("SELECT quantity FROM sale_items " +
                        "WHERE receiptNumber=? AND menuItem=?;");
                selectStmt.setInt(1, receiptNo);
                selectStmt.setString(2, item.getCode());
                ResultSet results = selectStmt.executeQuery();
                if (results.next()) {
                    setTotalPrice(totalPrice.minus(item.getPrice().multipliedBy(results.getInt("quantity"))));
                }
                PreparedStatement deleteStmt = dbHandler.prepareResource("/sql/modify/delete/deleteSaleItem.sql");
                deleteStmt.setInt(1, receiptNo);
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
                    setTotalPrice(totalPrice.plus(item.getPrice().multipliedBy(change)));
                    super.changeOrderedQuantity(menuItemTable.getOrAddItem(item), change);
                } catch (SQLException e) {
                    throw new RuntimeException("Unable to update item in sale.", e);
                }
            }
        }

        @Override
        public void setDate(LocalDate date) {
            updateColumn(tableName, key, "date", Date.valueOf(date));
            super.setDate(date);
        }

        @Override
        public void setTime(LocalTime time) {
            updateColumn(tableName, key, "time", Time.valueOf(time));
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
