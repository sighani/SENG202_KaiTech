package kaitech.api.model;

import org.joda.money.Money;

import java.util.Date;

/**
 * Interface to represent loyalty cards
 * @author Michael Freeman
 */


public interface LoyaltyCard {

    /**
     * Adds the appropriate amount of money to the customers card according to
     * the cost of the purchase, set at 10% currently
     * @param purchaseCost
     */
    void addPoints(Money purchaseCost);

    /**
     * Takes the current price of the order and returns the new price
     * after the customers purchase points have been used up
     * @param purchaseCost
     * @return newPurchaseCost
     */
    Money spendPoints(Money purchaseCost);

    /**
     * Get the previous date of purchase
     * @return lastPuchaseDate
     */
    Date getLastPurchase();

    /**
     * Set the previous date of purchase
     * @param lastPurchase
     */
    void setLastPurchase(Date lastPurchase);

    /**
     * Get the current balance on the card
     * @return balance
     */
    Money getBalance();

    /**
     * Set the balance on the card
     * @param balance
     */
    void setBalance(Money balance);

    /**
     * Gets the ID of the current card
     * @return id
     */
    int getId();

    /**
     * Get the customer name associated with the card
     * @return customername
     */
    String getCustomerName();
}

