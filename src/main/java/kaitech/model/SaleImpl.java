package kaitech.model;

import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implements the {@link Sale} interface; used to store details about a sale which has occurred.
 */
public class SaleImpl implements Sale {

    /**
     * Receipt number.
     * This field is immutable, create a new object to change.
     */
    protected final int receiptNumber;

    /**
     * The date the order was taken.
     */
    protected LocalDate date;

    /**
     * The time the order was taken.
     */
    protected LocalTime time;

    /**
     * The total price of the order.
     */
    protected Money totalPrice;

    /**
     * The method of payment used.
     */
    protected PaymentType paymentType;

    /**
     * Any additional notes that the employee who took the order has left.
     */
    protected String notes;

    /**
     * A map from the MenuItems ordered to the integer quantities.
     */
    protected final Map<MenuItem, Integer> itemsOrdered = new HashMap<>();

    /**
     * A run-of-the-mill constructor, except the Business is added as an observer and is notified with the Map of
     * MenuItems ordered.
     *
     * @param date         The Date of purchase
     * @param time         The Time of purchase
     * @param totalPrice   An int representing the total cost of the sale
     * @param paymentType  The PaymentType value showing the method of payment
     * @param notes        A String where the cashier can leave any additional comments
     * @param itemsOrdered The Map of MenuItems ordered
     */
    public SaleImpl(LocalDate date, LocalTime time, Money totalPrice, PaymentType paymentType, String notes,
                    Map<MenuItem, Integer> itemsOrdered) {
        this.receiptNumber = -1;
        this.date = date;
        this.time = time;
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
        this.notes = notes;
        this.itemsOrdered.putAll(itemsOrdered);
    }

    protected SaleImpl(int receiptNo, LocalDate date, LocalTime time, Money totalPrice, PaymentType paymentType,
                       String notes, Map<MenuItem, Integer> itemsOrdered) {
        this.receiptNumber = receiptNo;
        this.date = date;
        this.time = time;
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
        this.notes = notes;
        this.itemsOrdered.putAll(itemsOrdered);
    }

    public SaleImpl(LocalDate date, LocalTime time, Money totalPrice, PaymentType paymentType,
                    Map<MenuItem, Integer> itemsOrdered) {
        this.receiptNumber = -1;
        this.date = date;
        this.time = time;
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
        this.itemsOrdered.putAll(itemsOrdered);
    }

    @Override
    public int getReceiptNumber() {
        return receiptNumber;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public LocalTime getTime() {
        return time;
    }

    @Override
    public Money getTotalPrice() {
        return totalPrice;
    }

    @Override
    public PaymentType getPaymentType() {
        return paymentType;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public Map<MenuItem, Integer> getItemsOrdered() {
        //Unmodifiable so database can easily track changes.
        return Collections.unmodifiableMap(itemsOrdered);
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
    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice;
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
    public void setItemsOrdered(Map<MenuItem, Integer> itemsOrdered) {
        this.itemsOrdered.clear();
        this.itemsOrdered.putAll(itemsOrdered);
    }

    @Override
    public void addItemToOrder(MenuItem item, int quantity) {
        this.itemsOrdered.put(item, quantity);
    }

    @Override
    public void removeItemFromOrder(MenuItem item) {
        this.itemsOrdered.remove(item);
    }

    @Override
    public void changeOrderedQuantity(MenuItem item, int change) throws IllegalArgumentException {
        Integer currentQuant = this.itemsOrdered.get(item);
        if (boundsCheckChange(currentQuant, change)) {
            removeItemFromOrder(item);
            addItemToOrder(item, currentQuant + change);
        }
    }

    protected boolean boundsCheckChange(Integer currentQuant, int change) throws IllegalArgumentException {
        boolean notExists = currentQuant == null;
        if (change < 0 && notExists) {
            throw new IllegalArgumentException("The specified ingredient does not exist.");
        } else if (!notExists && currentQuant + change < 0) {
            throw new IllegalArgumentException("Cannot decrease quantity by an amount greater than what the Business owns.");
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof SaleImpl)) return false;
        SaleImpl other = (SaleImpl) obj;
        return Objects.equals(other.getReceiptNumber(), getReceiptNumber()) //
                && Objects.equals(other.getDate(), getDate()) //
                && Objects.equals(other.getTime(), getTime()) //
                && Objects.equals(other.getTotalPrice(), getTotalPrice()) //
                && Objects.equals(other.getPaymentType(), getPaymentType()) //
                && Objects.equals(other.getNotes(), getNotes()) //
                && Objects.equals(other.getItemsOrdered(), getItemsOrdered());
    }

    @Override
    public int hashCode() {
        int i = 0;
        i = 31 * i + getReceiptNumber();
        i = 31 * i + (getDate() == null ? 0 : getDate().hashCode());
        i = 31 * i + (getTime() == null ? 0 : getTime().hashCode());
        i = 31 * i + (getTotalPrice() == null ? 0 : getTotalPrice().hashCode());
        i = 31 * i + (getPaymentType() == null ? 0 : getPaymentType().hashCode());
        i = 31 * i + (getNotes() == null ? 0 : getNotes().hashCode());
        i = 31 * i + (getItemsOrdered() == null ? 0 : getItemsOrdered().hashCode());
        return i;
    }
}
