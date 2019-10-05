package kaitech.model;

import kaitech.api.model.LoyaltyCard;
import org.joda.money.Money;

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

    public LoyaltyCardImpl(Date currentDate, int id){
        this.lastPurchase = currentDate;
        this.id = id;
    }

    public LoyaltyCardImpl(Date currentDate, int id, String customerName){

    }


}
