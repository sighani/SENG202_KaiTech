package kaitech.model;

import kaitech.api.model.LoyaltyCard;
import org.joda.money.Money;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class LoyaltyCardTest {

    @Test
    public void equalsTest() {
        LocalDate d = LocalDate.now();
        LoyaltyCard loyaltyCard1 = new LoyaltyCardImpl(3221, d, "Michael", Money.parse("NZD 0.01"));
        assertEquals(loyaltyCard1.getId(), 3221);
        assertEquals(loyaltyCard1.getLastPurchase(), d);
        assertEquals(loyaltyCard1.getBalance(), Money.parse("NZD 0.01"));
        assertEquals(loyaltyCard1.getCustomerName(), "Michael");
    }

    @Test
    public void testAddPoints() {
        LocalDate date = LocalDate.now();
        LoyaltyCard loyaltyCard = new LoyaltyCardImpl(1, date, "Sam", Money.parse("NZD 10.00"));
        loyaltyCard.addPoints(Money.parse("NZD 5.00"), 10, date);
        assertEquals(loyaltyCard.getBalance(), Money.parse("NZD 10.50"));
    }

    @Test
    public void testSpendPoints() {
        LoyaltyCard loyaltyCard = new LoyaltyCardImpl(2, LocalDate.now(), "David", Money.parse("NZD 15.00"));
        Money purchaseCost = loyaltyCard.spendPoints(Money.parse("NZD 5.00"));

        //testing when the purchase is less than the avaliable balance it returns 0$ and reduces avaliable balance
        assertEquals(loyaltyCard.getBalance(), Money.parse("NZD 10.00"));
        assertEquals(purchaseCost, Money.parse("NZD 0.00"));

        purchaseCost = loyaltyCard.spendPoints(Money.parse("NZD 15.00"));

        //testing when the purchase is more than the avaliable balance that it returns correclty and spends all points
        assertEquals(loyaltyCard.getBalance(), Money.parse("NZD 0.00"));
        assertEquals(purchaseCost, Money.parse("NZD 5.00"));
    }
}
