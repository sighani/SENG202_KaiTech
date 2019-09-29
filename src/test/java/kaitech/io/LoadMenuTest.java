package kaitech.io;

import kaitech.api.model.Menu;
import kaitech.api.model.MenuItem;
import kaitech.util.MenuItemType;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoadMenuTest {

    private Menu menu;
    private Map<String, MenuItem> menuItems;

    @Before
    public void loadMenuFile() {
        String fileName = "resources/data/SampleMenu.xml";
        try {
            LoadData.loadMenu(fileName);
        } catch (SAXException e) {
            System.out.println("Wrong file type");
        }
        menuItems = LoadData.menuItems();
        menu = LoadData.menu();
        assertEquals("Checking all items are present", 6, menuItems.size());
    }

    @Test
    public void testMenuItems() {
        MenuItem beefBurger = menuItems.get("BB1");
        MenuItem lemonade = menuItems.get("Lem");
        assertTrue("Checking that the burger contains Onion", beefBurger.getIngredients().contains("Onion"));
        assertTrue("Checking that the burger contains Mayo", beefBurger.getIngredients().contains("Mayo"));
        assertTrue("Checking the lemonade has a can", lemonade.getIngredients().contains("LemCan"));
        //checking names
        assertEquals("Checking the burger is named correctly", "Beefburger", beefBurger.getName());
        assertEquals("Checking lemondae is correctly named", "LemCan", lemonade.getName());

        //checking types
        assertEquals("checking the burger is correct type", MenuItemType.GRILL, beefBurger.getType());
        assertEquals("Cheking the lemonade is the correct type", MenuItemType.BEVERAGE, lemonade.getType());

        //checking costs
        assertEquals("Checking the cost is read in propoerly to the burger",
                Money.parse("NZD 30.00"), beefBurger.getPrice());
        assertEquals("Checking that the price defaults to 0", Money.parse("NZD 0.00"), lemonade.getPrice());
    }

    @Test
    public void testMenuAttributes() {
        assertTrue("Checking the menu contains Chicken fried rice", menu.getMenuItems().containsKey("CF"));
        assertTrue("Checking the menu contains baby face cocktail", menu.getMenuItems().containsKey("BF"));
        assertEquals("Checking the description is correct",
                "Sample menu: Things we sell in cold weather", menu.getDescription());
        assertEquals("Checking the title is correct", "Winter warmers", menu.getTitle());
        assertEquals("Checking the menu contains the correct amount of items",
                6, menu.getMenuItems().size());
    }

}
