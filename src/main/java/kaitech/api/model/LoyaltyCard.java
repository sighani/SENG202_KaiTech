package kaitech.api.model;

import org.joda.money.Money;

import java.time.LocalDate;

/**
 * Interface to represent loyalty cards
 *
 * @author Michael Freeman
 */
public interface LoyaltyCard {

    /**
     * Adds the appropriate amount of money to the customers card according to
     * the cost of the purchase, set at 10% currently
     *
     * @param purchaseCost The Money price of the purchase
     */
    void addPoints(Money purchaseCost);

    /**
     * Takes the current price of the order and returns the new price
     * after the customers purchase points have been used up
     *
     * @param purchaseCost The current Money price of the order before the card holder uses their points
     * @return The new Money purchase cost of the order, after loyalty points have been applied
     */
    Money spendPoints(Money purchaseCost);

    /**
     * Get the previous date of purchase
     *
     * @return The LocalDate of the last purchase made by the card holder
     */
    LocalDate getLastPurchase();

    /**
     * Get the current balance on the card
     *
     * @return The Money balance on the loyalty card
     */
    Money getBalance();

    /**
     * Gets the ID of the current card
     *
     * @return The int ID of the card
     */
    int getId();

    /**
     * Get the customer name associated with the card
     *
     * @return The String name of the customer associated with the card
     */
    String getCustomerName();

    /**
     * Set the previous date of purchase
     *
     * @param lastPurchase The LocalDate of the last purchase made by the card holder
     */
    void setLastPurchase(LocalDate lastPurchase);

    /**
     * Set the balance on the card
     *
     * @param balance The Money balance of the card
     */
    void setBalance(Money balance);

    /**
     * Set the name of the customer associated with the card
     *
     * @param name The String name of the customer associated with the card
     */
    void setCustomerName(String name);
}

