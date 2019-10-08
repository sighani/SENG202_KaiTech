package kaitech.io;

import kaitech.api.model.*;
import kaitech.model.BusinessImpl;
import kaitech.util.MenuItemType;
import kaitech.util.PhoneType;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoadDataTest {

    private Business business;

    @Before
    public void loadAllDataTypes() throws Throwable {
        business = BusinessImpl.createTestBusiness(File.createTempFile("test_db", ".db"));

        String ingFileName = "resources/data/Ingredients.xml";
        String suppFileName = "resources/data/Suppliers.xml";
        String menuFileName = "resources/data/SampleMenu.xml";
        String loyaltyCardFileName = "resources/data/LoyaltyCards.xml";
        try {
            LoadData.loadIngredients(ingFileName);
            LoadData.saveIngredients(business);
            LoadData.loadSuppliers(suppFileName);
            LoadData.loadMenu(menuFileName);
            LoadData.loadLoyaltyCards(loyaltyCardFileName);
        } catch (SAXException e) {
            System.out.println("Error in DTD, this wont be triggered");
        }
    }

    @Test
    public void testSavingLoyaltyCards(){
        LoadData.saveLoyaltyCards(business);
        LoyaltyCard loyaltyCard1 = business.getLoyaltyCardTable().getLoyaltyCard(12345);
        LoyaltyCard loyaltyCard2 = business.getLoyaltyCardTable().getLoyaltyCard(7821);

        assertEquals("Checking Names are correct", "Michael Freeman", loyaltyCard1.getCustomerName());
        assertEquals("Checking names are correct", "King Kong", loyaltyCard2.getCustomerName());

        assertEquals("Checking Balance is correct", Money.parse("NZD 0.01"), loyaltyCard1.getBalance());
        assertEquals("Checking Balance is correct", Money.parse("NZD 10.00"), loyaltyCard2.getBalance());

        try {
            assertEquals("Checking Date is correct",
                    new SimpleDateFormat("dd/MM/yyyy").parse("11/06/2018").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    loyaltyCard1.getLastPurchase());
            assertEquals("Checking Date is correct",
                    new SimpleDateFormat("dd/MM/yyyy").parse("21/06/2018").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    loyaltyCard2.getLastPurchase());
        } catch (ParseException p){
            System.err.println("Parsing Date error, will not occur");
        }

        assertEquals("Checking ID is correct", 12345, loyaltyCard1.getId());
        assertEquals("Checking ID is correct", 7821, loyaltyCard2.getId());
    }

    @Test
    public void testSavingIngredients() {
        Ingredient ing1 = business.getIngredientTable().getIngredient("BPat");
        Ingredient ing2 = business.getIngredientTable().getIngredient("Beetroot");

        assertEquals("Checking Ingredient name is loaded correctly", ing1.getName(), "Beef patty");
        assertEquals("Checking units are loaded correctly", ing1.getUnit(), UnitType.COUNT);
        assertEquals("Checking Vegan Option is loaded correctly", ing1.getIsVegan(), ThreeValueLogic.NO);

        assertEquals("Checking Ingredient name is loaded correctly pt2",
                ing2.getName(), "Beetroot slice");
        assertEquals("Checking Price is loaded correctly",
                ing2.getPrice(), Money.parse("NZD 165.00"));
        assertEquals("Checking Vegetarian option is set to default",
                ing2.getIsVeg(), ThreeValueLogic.NO);
    }


    @Test
    public void testSavingSuppliers() {
        LoadData.saveSuppliers(business);
        Supplier supplier1 = business.getSupplierTable().getSupplier("s1");
        assertEquals("Checking that suppliers (Names) are correctly loaded into the database",
                supplier1.getName(), "Countup");
        assertEquals("Checking supplier address is correctly loaded into the database",
                supplier1.getAddress(), "12 High Street, Christchurch");
        Supplier supplier2 = business.getSupplierTable().getSupplier("s2");
        assertEquals("Checking supplier URL is set to default if no value is given in XML file",
                supplier2.getUrl(), Supplier.UNKNOWN_URL);
        assertEquals("Checking supplier phone type is entered correctly",
                supplier2.getPhoneType(), PhoneType.MOBILE);

    }

    @Test
    public void testSavingMenu() {
        LoadData.saveMenu(business);
        Menu menu = business.getMenuTable().getMenu(1);
        MenuItem menuItem1 = business.getMenuItemTable().getMenuItem("BB1");
        MenuItem menuItem2 = business.getMenuItemTable().getMenuItem("CF");

        assertEquals("Testing the menu has the correct title",
                menu.getTitle(), "Winter warmers");
        assertEquals("Testing the menu has the correct description",
                menu.getDescription(), "Sample menu: Things we sell in cold weather");
        assertEquals("Testing all menuItems are contained in the menu",
                menu.getMenuItems().size(), 6);

        assertEquals("Checking menu Item 1 has the correct name",
                menuItem1.getName(), "Beefburger");

        assertEquals("Checking menu Item cost is correct", menuItem2.getPrice(), Money.parse("NZD 25.00"));
        assertEquals("Checking menuItem type is correct", menuItem2.getType(), MenuItemType.ASIAN);

    }

    @Test
    public void checkFileOkTest() {
        String fname1 = "resources/data/Ingredients.xml";
        assertTrue(LoadData.checkFileOK(fname1));
    }
}
