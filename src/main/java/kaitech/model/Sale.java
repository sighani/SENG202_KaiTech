package kaitech.model;

import kaitech.util.PaymentType;

import java.sql.Time;
import java.util.Date;
import java.util.Map;

/**
 * A class that represents a sales record. A sales record is created whenever
 * an order is made. It contains important details about the sale that is
 * useful for data collection.
 */
public class Sale {
    /**
     * Receipt number.
     */
    private int receiptNumber;

    /**
     * A map from the MenuItems ordered to the integer quantities.
     */
    private Map<MenuItem, Integer> itemsOrdered;

    /**
     * The date the order was taken.
     */
    private Date date;

    /**
     * The time the order was taken.
     */
    private Time time;

    /**
     * The method of payment used.
     */
    private PaymentType paymentType;

    /**
     * Any additional notes that the employee who took the order has left.
     */
    private String notes;

    /**
     * The total price of the order.
     */
    private int totalPrice;
}
