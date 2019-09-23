package kaitech.database;

import kaitech.api.database.*;
import kaitech.api.model.*;
import kaitech.model.*;
import kaitech.util.PaymentType;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

public class TestSaleDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private SaleTable saleTable;
    private MenuItemTable menuItemTable;
    private InventoryTable inventoryTable;

    public void init() throws Throwable {
        dbHandler = new DatabaseHandler(tempFolder.newFile());
        PreparedStatement stmt;
        List<String> resources = Arrays.asList("/sql/setup/setupIngredientsTbl.sql",
                "/sql/setup/setupSuppliersTbl.sql",
                "/sql/setup/setupRecipesTbl.sql",
                "/sql/setup/setupRecipeIngredientsTbl.sql",
                "/sql/setup/setupMenuItemsTbl.sql",
                "/sql/setup/setupIngredNamesTbl.sql",
                "/sql/setup/setupSalesTbl.sql",
                "/sql/setup/setupSaleItemsTbl.sql",
                "/sql/setup/setupInventoryTbl.sql");
        for (String resource : resources) {
            stmt = dbHandler.prepareResource(resource);
            stmt.executeUpdate();
        }
        IngredientTable ingredientTable = new IngredientTblImpl(dbHandler, new SupplierTblImpl(dbHandler));
        RecipeTable recipeTable = new RecipeTblImpl(dbHandler, ingredientTable);
        inventoryTable = new InventoryTblImpl(dbHandler, ingredientTable);
        menuItemTable = new MenuItemTblImpl(dbHandler, recipeTable, ingredientTable);
        saleTable = new SaleTblImpl(dbHandler, menuItemTable, inventoryTable);
    }

    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    private Sale putSale(LocalDate date, LocalTime time, Money totalPrice, PaymentType paymentType,
                         Map<MenuItem, Integer> itemsOrdered) {
        Sale sale = new SaleImpl(date, time, totalPrice, paymentType, itemsOrdered);
        return saleTable.putSale(sale);
    }

    @Test
    public void testPutSale() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        inventoryTable.putInventory(ingredient, 500);
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00"));

        LocalDate date = LocalDate.of(2019, 9, 6);
        LocalTime time = LocalTime.of(14, 0, 0);
        Money price = Money.parse("NZD 6.00");
        Map<MenuItem, Integer> itemsOrdered = Collections.singletonMap(menuItem, 2);

        Sale sale = putSale(date, time, price, PaymentType.CASH, itemsOrdered);
        sale.setNotes("Note");
        int receiptNo = sale.getReceiptNumber();

        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM sales WHERE receiptNumber=?;");
        stmt.setInt(1, receiptNo);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            assertEquals(sale.getDate(), results.getDate("date").toLocalDate());
            assertEquals(sale.getTime(), results.getTime("time").toLocalTime());
            assertEquals(sale.getTotalPrice(), Money.parse(results.getString("totalPrice")));
            assertEquals(sale.getPaymentType().ordinal(), results.getInt("paymentType"));
            assertEquals(sale.getNotes(), results.getString("notes"));
        } else {
            throw new RuntimeException("Could not retrieve sale from database.");
        }

        stmt = dbHandler.prepareStatement("SELECT * FROM sale_items WHERE receiptNumber=?;");
        stmt.setInt(1, receiptNo);
        results = stmt.executeQuery();
        if (results.next()) {
            assertEquals(menuItem.getCode(), results.getString("menuItem"));
            assertEquals(2, results.getInt("quantity"));
        } else {
            throw new RuntimeException("Unable to retrieve sale items from database.");
        }
        teardown();
    }

    @Test
    public void testSaleUpdatesInventory() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        inventoryTable.putInventory(ingredient, 500);
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00"));

        LocalDate date = LocalDate.of(2019, 9, 6);
        LocalTime time = LocalTime.of(14, 0, 0);
        Money price = Money.parse("NZD 6.00");
        Map<MenuItem, Integer> itemsOrdered = Collections.singletonMap(menuItem, 2);

        Sale sale = putSale(date, time, price, PaymentType.EFTPOS, itemsOrdered);
        Integer ingredientQuantity = inventoryTable.getIngredientQuantity(ingredient);
        assertEquals(300, (int) ingredientQuantity);
        teardown();
    }

    @Test
    public void testGetSale() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        inventoryTable.putInventory(ingredient, 500);
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00"));
        menuItem = menuItemTable.putMenuItem(menuItem);

        LocalDate date = LocalDate.of(2019, 9, 6);
        LocalTime time = LocalTime.of(14, 0, 0);
        Money price = Money.parse("NZD 6.00");
        Map<MenuItem, Integer> itemsOrdered = Collections.singletonMap(menuItem, 2);

        Sale sale = putSale(date, time, price, PaymentType.CASH, itemsOrdered);
        int receiptNo = sale.getReceiptNumber();

        Sale retrievedSale = saleTable.getSale(receiptNo);
        assertEquals(date, retrievedSale.getDate());
        assertEquals(time, retrievedSale.getTime());
        assertEquals(Money.parse("NZD 6.00"), retrievedSale.getTotalPrice());
        assertEquals(PaymentType.CASH, retrievedSale.getPaymentType());
        assertNull(retrievedSale.getNotes());
        assertTrue(retrievedSale.getItemsOrdered().containsKey(menuItem));

        Sale sale2 = putSale(date, time, price, PaymentType.EFTPOS, itemsOrdered);

        SupplierTable otherSupplierTable = new SupplierTblImpl(dbHandler);
        IngredientTable otherIngredientTable = new IngredientTblImpl(dbHandler, otherSupplierTable);
        RecipeTable otherRecipeTable = new RecipeTblImpl(dbHandler, otherIngredientTable);
        InventoryTable otherInventoryTable = new InventoryTblImpl(dbHandler, otherIngredientTable);
        MenuItemTable otherMenuItemTable = new MenuItemTblImpl(dbHandler, otherRecipeTable, otherIngredientTable);
        SaleTable otherSaleTable = new SaleTblImpl(dbHandler, otherMenuItemTable, otherInventoryTable);
        Sale dbSale = otherSaleTable.getSale(receiptNo);
        assertNotNull(dbSale);

        assertEquals(retrievedSale.getDate(), dbSale.getDate());
        assertEquals(retrievedSale.getTime(), dbSale.getTime());
        assertEquals(retrievedSale.getTotalPrice(), dbSale.getTotalPrice());
        assertEquals(retrievedSale.getPaymentType(), dbSale.getPaymentType());
        assertNull(dbSale.getNotes());
        assertEquals(1, dbSale.getItemsOrdered().size());

        teardown();
    }

    @Test
    public void testAddRemoveChangeItem() throws Throwable {
        init();

        Ingredient ingredient = new IngredientImpl("PORK");
        inventoryTable.putInventory(ingredient, 500);
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00"));
        menuItem = menuItemTable.putMenuItem(menuItem);

        LocalDate date = LocalDate.of(2019, 9, 6);
        LocalTime time = LocalTime.of(14, 0, 0);
        Money price = Money.parse("NZD 6.00");
        Map<MenuItem, Integer> itemsOrdered = Collections.singletonMap(menuItem, 2);

        Sale sale = putSale(date, time, price, PaymentType.CASH, itemsOrdered);
        LocalDate newDate = LocalDate.now();
        LocalTime newTime = LocalTime.now();
        sale.setDate(newDate);
        sale.setTime(newTime);
        sale.setPaymentType(PaymentType.EFTPOS);
        sale.setTotalPrice(Money.parse("NZD 8.00"));

        assertEquals(1, sale.getItemsOrdered().size());
        MenuItem menuItem2 = new MenuItemImpl("NOT BAO", recipe, Money.parse("NZD 2.50"));
        sale.addItemToOrder(menuItem2, 2);
        assertEquals(2, sale.getItemsOrdered().size());
        assertTrue(sale.getItemsOrdered().containsKey(menuItemTable.getMenuItem("NOT BAO")));

        sale.changeOrderedQuantity(menuItemTable.getMenuItem("NOT BAO"), 1);
        assertEquals(3, (int) sale.getItemsOrdered().get(menuItemTable.getMenuItem("NOT BAO")));

        sale.removeItemFromOrder(menuItemTable.getMenuItem("NOT BAO"));
        assertEquals(1, sale.getItemsOrdered().size());
        assertFalse(sale.getItemsOrdered().containsKey(menuItemTable.getMenuItem("NOT BAO")));
        assertTrue(sale.getItemsOrdered().containsKey(menuItem));

        teardown();
    }

    @Test
    public void testGetAllReceiptNumbers() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        inventoryTable.putInventory(ingredient, 500);
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00"));

        LocalDate date = LocalDate.of(2019, 9, 6);
        LocalTime time = LocalTime.of(14, 0, 0);
        Money price = Money.parse("NZD 6.00");
        Map<MenuItem, Integer> itemsOrdered = Collections.singletonMap(menuItem, 2);

        Sale sale1 = putSale(date, time, price, PaymentType.CASH, itemsOrdered);
        Sale sale2 = putSale(date, time, price, PaymentType.CASH, itemsOrdered);
        int receiptNo1 = sale1.getReceiptNumber();
        int receiptNo2 = sale2.getReceiptNumber();
        Set<Integer> receiptNos = saleTable.getAllReceiptNo();
        assertEquals(2, receiptNos.size());
        assertTrue(receiptNos.contains(receiptNo1));
        assertTrue(receiptNos.contains(receiptNo2));
        teardown();
    }

    @Test
    public void testRemoveSale() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        inventoryTable.putInventory(ingredient, 500);
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00"));

        LocalDate date = LocalDate.of(2019, 9, 6);
        LocalTime time = LocalTime.of(14, 0, 0);
        Money price = Money.parse("NZD 6.00");
        Map<MenuItem, Integer> itemsOrdered = Collections.singletonMap(menuItem, 2);

        Sale sale = putSale(date, time, price, PaymentType.CASH, itemsOrdered);
        int receiptNo = sale.getReceiptNumber();
        assertNotNull(saleTable.getSale(receiptNo));
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM sales WHERE receiptNumber=?;");
        stmt.setInt(1, receiptNo);
        ResultSet results = stmt.executeQuery();
        assertTrue(results.next());

        saleTable.removeSale(receiptNo);
        assertNull(saleTable.getSale(receiptNo));
        results = stmt.executeQuery();
        assertFalse(results.next());
        teardown();
    }

    @Test
    public void testResolveAllSales() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        inventoryTable.putInventory(ingredient, 500);
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00"));

        LocalDate date = LocalDate.of(2019, 9, 6);
        LocalTime time = LocalTime.of(14, 0, 0);
        Money price = Money.parse("NZD 6.00");
        Map<MenuItem, Integer> itemsOrdered = Collections.singletonMap(menuItem, 2);

        Sale sale1 = putSale(date, time, price, PaymentType.CASH, itemsOrdered);
        Sale sale2 = putSale(date, time, price, PaymentType.CASH, itemsOrdered);
        int receiptNo1 = sale1.getReceiptNumber();
        int receiptNo2 = sale2.getReceiptNumber();

        Map<Integer, Sale> sales = saleTable.resolveAllSales();
        assertEquals(sale1, sales.get(receiptNo1));
        assertEquals(sale2, sales.get(receiptNo2));
        teardown();
    }
}
