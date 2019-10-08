package kaitech.database;

import kaitech.api.database.IngredientTable;
import kaitech.api.database.LoyaltyCardTable;
import kaitech.api.database.SupplierTable;
import kaitech.api.model.LoyaltyCard;
import kaitech.model.LoyaltyCardImpl;
import org.joda.money.Money;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TestLoyaltyCardDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private LoyaltyCardTable loyaltyCardTable;

    @Before
    public void init() throws Throwable {
        dbHandler = new DatabaseHandler(tempFolder.newFile());
        PreparedStatement stmt = dbHandler.prepareResource("/sql/setup/setupLoyaltyCardsTbl.sql");
        stmt.executeUpdate();
        loyaltyCardTable = new LoyaltyCardTblImpl(dbHandler);
    }

    @After
    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    @Test
    public void testPutLoyaltyCard() throws Throwable{
        LocalDate d = LocalDate.now();
        LoyaltyCard loyaltyCard = new LoyaltyCardImpl(1, d, "Sam", Money.parse("NZD 10.00"));
        loyaltyCardTable.putLoyaltyCard(loyaltyCard);
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM loyalty_cards WHERE id=1;");
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            assertEquals("Checking Id is correct", 1, results.getInt("id"));
            assertEquals("Checking Name", "Sam", results.getString("customerName"));
            assertEquals("Checking Balalance", Money.parse("NZD 10.00"), Money.parse(results.getString("balance")));
        }
    }

    @Test
    public void testGetLoyaltyCard(){
        LocalDate d = LocalDate.now();
        LoyaltyCard loyaltyCard = new LoyaltyCardImpl(21, d, "David", Money.parse("NZD 10.00"));
        loyaltyCardTable.putLoyaltyCard(loyaltyCard);
        LoyaltyCard loyaltyCardReturned = loyaltyCardTable.getLoyaltyCard(21);
        assertEquals(loyaltyCard.getCustomerName(), loyaltyCardReturned.getCustomerName());
    }

    @Test
    public void testGetAllLoyaltyCardIds(){
        LocalDate d = LocalDate.now();
        LoyaltyCard loyaltyCard = new LoyaltyCardImpl(21, d, "David", Money.parse("NZD 10.00"));
        LoyaltyCard loyaltyCard2 = new LoyaltyCardImpl(321, d, "Tom", Money.parse("NZD 10.05"));
        LoyaltyCard loyaltyCard3 = new LoyaltyCardImpl(213, d, "Harry", Money.parse("NZD 10.00"));
        loyaltyCardTable.putLoyaltyCard(loyaltyCard);
        loyaltyCardTable.putLoyaltyCard(loyaltyCard2);
        loyaltyCardTable.putLoyaltyCard(loyaltyCard3);
        Set<Integer> expectedIds = new HashSet<>(Arrays.asList(21, 321, 213));
        assertEquals("Checking List of ID's is equal when returned", expectedIds, loyaltyCardTable.getAllLoyaltyCardIDs());
    }

    @Test
    public void testremoveLoyaltyCard() throws Throwable{
        LocalDate d = LocalDate.now();
        LoyaltyCard loyaltyCard = new LoyaltyCardImpl(21, d, "David", Money.parse("NZD 10.00"));
        loyaltyCardTable.putLoyaltyCard(loyaltyCard);
        //checking that the card is loaded correctly, it will be but eh might as well
        assertNotNull(loyaltyCardTable.getLoyaltyCard(21));
        //removing loyalty card
        loyaltyCardTable.removeLoyaltyCard(21);
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM loyalty_cards WHERE id=21;");
        ResultSet results = stmt.executeQuery();
        assertFalse(results.next());
    }

    @Test
    public void testResolveAllLoyaltyCards(){
        //setting up new objects
        LocalDate d = LocalDate.now();
        LoyaltyCard loyaltyCard1 = new LoyaltyCardImpl(1, d, "David", Money.parse("NZD 10.00"));
        LoyaltyCard loyaltyCard2 = new LoyaltyCardImpl(2, d, "David", Money.parse("NZD 10.00"));
        LoyaltyCard loyaltyCard3 = new LoyaltyCardImpl(3, d, "David", Money.parse("NZD 10.00"));
        LoyaltyCard loyaltyCard4 = new LoyaltyCardImpl(4, d, "David", Money.parse("NZD 10.00"));

        //saving to db
        loyaltyCardTable.putLoyaltyCard(loyaltyCard1);
        loyaltyCardTable.putLoyaltyCard(loyaltyCard2);
        loyaltyCardTable.putLoyaltyCard(loyaltyCard3);
        loyaltyCardTable.putLoyaltyCard(loyaltyCard4);

        Map<Integer, LoyaltyCard> returnedLoyaltyCards = loyaltyCardTable.resolveAllLoyaltyCards();

        assertEquals("Checking the right amount are returned", 4, returnedLoyaltyCards.size());

        Set<Integer> expectedIds = new HashSet<>(Arrays.asList(1, 2, 3, 4));

        assertEquals("Checking the keyset contains the correct keys", expectedIds, returnedLoyaltyCards.keySet());
    }
}
