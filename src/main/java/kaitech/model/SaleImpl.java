package kaitech.model;

import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Observable;

/**
 * A class that represents a sales record. A sales record is created whenever
 * an order is made. It contains important details about the sale that is
 * useful for data collection.
 */
public class SaleImpl extends Observable implements Sale {
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
    private LocalDate date;

    /**
     * The time the order was taken.
     */
    private LocalTime time;

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
    private Money totalPrice;

    /**
     * A run-of-the-mill constructor, except the Business is added as an observer and is notified with the Map of
     * MenuItems ordered.
     *
     * @param itemsOrdered The Map of MenuItems ordered
     * @param date         The Date of purchase
     * @param time         The Time of purchase
     * @param paymentType  The PaymentType value showing the method of payment
     * @param notes        A String where the cashier can leave any additional comments
     * @param totalPrice   An int representing the total cost of the sale
     */
    public SaleImpl(Map<MenuItem, Integer> itemsOrdered, LocalDate date, LocalTime time, PaymentType paymentType,
                    String notes, Money totalPrice, Business business) {
        this.receiptNumber = -1; // -1 implies the receipt number has not yet been assigned by the database
        this.itemsOrdered = itemsOrdered;
        this.date = date;
        this.time = time;
        this.paymentType = paymentType;
        this.notes = notes;
        this.totalPrice = totalPrice;
        addObserver(business);
        setChanged();
        notifyObservers(itemsOrdered);
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int getReceiptNumber() {
        return receiptNumber;
    }

    @Override
    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    @Override
    public Map<MenuItem, Integer> getItemsOrdered() {
        return itemsOrdered;
    }
}
