package kaitech.model;

import kaitech.api.model.LoyaltyCard;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Implements the {@link LoyaltyCard} interface; used to store details about a loyalty card.
 */
public class LoyaltyCardImpl implements LoyaltyCard {

    /**
     * For Percentage stuff
     */
    private static BigDecimal pc = new BigDecimal(100);


    /**
     * Unique ID for each customers card
     */
    private int id;

    /**
     * Spendable points on the card, to be discounted from the cost of the order if chosen
     * to by the holder
     */
    private Money balance;

    /**
     * Customer name, optional
     */
    private String customerName;

    /**
     * Last time card used, cannot be used more than a year after last purchase
     */
    private LocalDate lastPurchase;

    /**
     * Overloaded constructor, customer name is optional
     */
    public LoyaltyCardImpl(int id, LocalDate currentDate) {
        this.lastPurchase = currentDate;
        this.id = id;
        this.customerName = "Unknown";
        this.balance = Money.parse("NZD 0.00");
    }

    /**
     * Constructor with customer name
     */
    public LoyaltyCardImpl(int id, LocalDate currentDate, String customerName) {
        this.lastPurchase = currentDate;
        this.id = id;
        this.customerName = customerName;
        this.balance = Money.parse("NZD 0.00");
    }

    /**
     * Constructor for when loyalty cards are parsed with existing balances
     */
    public LoyaltyCardImpl(int id, LocalDate lastPurchase, String customerName, Money currentBalance) {
        this.lastPurchase = lastPurchase;
        this.id = id;
        this.customerName = customerName;
        this.balance = currentBalance;
    }

    @Override
    public void addPoints(Money purchaseCost, int percentage_returned, LocalDate date) {
        //sets last purchase to current date
        this.lastPurchase = date;
        //update the customers balance by given percentage of the purchase price
        float result = percentage_returned / 100.0f;
        this.balance = getBalance().plus(purchaseCost.multipliedBy(result, RoundingMode.HALF_DOWN));
    }

    @Override
    public Money spendPoints(Money purchaseCost) {
        if (purchaseCost.isLessThan(this.balance)) {
            //the whole card balance is used up, some left over
            this.balance = this.balance.minus(purchaseCost);
            return Money.parse("NZD 0.00");
        } else {
            //they are both either equal or there is more owed than on the card
            Money tempPurchaseCost = purchaseCost.minus(this.balance);
            this.balance = Money.parse("NZD 0.00");
            return tempPurchaseCost;
        }
    }

    @Override
    public LocalDate getLastPurchase() {
        return lastPurchase;
    }

    @Override
    public Money getBalance() {
        return balance;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getCustomerName() {
        return customerName;
    }

    @Override
    public void setLastPurchase(LocalDate lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    @Override
    public void setBalance(Money balance) {
        this.balance = balance;
    }

    @Override
    public void setCustomerName(String name) {
        this.customerName = customerName;
    }
}
