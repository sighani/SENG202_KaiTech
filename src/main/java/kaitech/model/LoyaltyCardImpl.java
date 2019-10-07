package kaitech.model;

import kaitech.api.model.LoyaltyCard;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.util.Date;

public class LoyaltyCardImpl implements LoyaltyCard {
    /**
     * Loyalty card implementation
     */

    /*
    Unique ID for each customers card
     */
    private int id;

    /*
    Spendable points on the card, to be discounted from the cost of the order if chosen
    to by the holder
     */
    private Money balance;

    /*
    Customer name, optional
     */
    private String customerName;

    /*
    Last time card used, cannot be used more than a year after last purchase
     */

    private Date lastPurchase;


    /*
    Overloaded constructor, customer name is optional
     */

    public LoyaltyCardImpl(int id, Date currentDate){
        this.lastPurchase = currentDate;
        this.id = id;
        this.customerName = "Unknown";
        this.balance = Money.parse("NZD 0.00");
    }

    /*
    Constructor with customer name
     */
    public LoyaltyCardImpl(int id, Date currentDate, String customerName){
        this.lastPurchase = currentDate;
        this.id = id;
        this.customerName = customerName;
        this.balance = Money.parse("NZD 0.00");
    }


    /*
    Constructor for when loyaltycards are parsed with existing balances
     */

    public LoyaltyCardImpl(int id, Date lastPurchase, String customerName, Money currentBalance){
        this.lastPurchase = lastPurchase;
        this.id = id;
        this.customerName = customerName;
        this.balance = currentBalance;
    }

    public void addPoints(Money purchaseCost){
        //sets last purchase to current date
        this.lastPurchase = new Date();
        //update the customers balance by 10 percent of the purchase price
        this.balance = this.balance.plus(purchaseCost.dividedBy(10, RoundingMode.FLOOR));
    }

    public Money spendPoints(Money purchaseCost){
        if(purchaseCost.isLessThan(this.balance)){
            //the whole card balance is used up, some left over
            this.balance = this.balance.minus(purchaseCost);
            return Money.parse("NZD 0.00");
        }else{
            //they are both either equal or there is more owed than on the card
            Money tempPurchaseCost = purchaseCost.minus(this.balance);
            this.balance = Money.parse("NZD 0.00");
            return tempPurchaseCost;
        }
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Date getLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(Date lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }
}
