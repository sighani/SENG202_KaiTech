package kaitech.io;

import kaitech.api.model.LoyaltyCard;
import kaitech.api.model.Supplier;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LoadLoyaltyCardsTest {
    //storing loaded files
    private Map<Integer, LoyaltyCard> cardsLoaded;

    //cleaning before each test
    @Before
    public void testLoadFile() {
        //path to Loyalty Card test files
        String fileName = "resources/data/LoyaltyCards.xml";
        //new LoyaltyCard loader instance
        try {
            LoadData.loadLoyaltyCards(fileName);
        } catch (SAXException e) {
            System.out.println("Wrong file type");
        } catch (IOException e) {
            System.out.println("Error parsing file.");
            e.printStackTrace();
        }
        cardsLoaded = LoadData.getLoyaltyCardsLoaded();

        //make sure we loaded all cards
        assertEquals("All Cards in the file should be added", 6, cardsLoaded.size());
    }

    @Test
    public void testIdLoaded(){
        LoyaltyCard loyaltyCard = cardsLoaded.get(12345);
        assertEquals("Checking ID's are loaded correctly", 12345, loyaltyCard.getId());
    }

    @Test
    public void testNameLoaded(){
        LoyaltyCard loyaltyCard = cardsLoaded.get(12222245);
        assertEquals("Checking names are loaded corretly", "Godizilla", loyaltyCard.getCustomerName());
    }

    @Test
    public void testBalanceLoaded(){
        LoyaltyCard loyaltyCard = cardsLoaded.get(99023);
        assertEquals("Checking that balances are loaded correctly", Money.parse("NZD 10.01"), loyaltyCard.getBalance());
    }

    @Test
    public void testDateLoaded(){
        LoyaltyCard loyaltyCard = cardsLoaded.get(90392);
        try{
        assertEquals("Checking dates are loaded correctly", new SimpleDateFormat("dd/MM/yyyy").parse("13/06/2018").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                loyaltyCard.getLastPurchase());

        }catch (ParseException e){
            System.err.println("Parse error from parsing to simple date format, will not occur");
        }
    }
}
