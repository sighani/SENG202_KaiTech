package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.LoyaltyCardTable;
import kaitech.api.model.LoyaltyCard;
import kaitech.model.LoyaltyCardImpl;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

/**
 * LoyaltyCardTblImpl extends AbstractTable, implements the LoyaltyCardTable interface, and permits limited access to
 * the data stored in the loyalty_card table.
 *
 * @author Julia Harrison
 */
public class LoyaltyCardTblImpl extends AbstractTable implements LoyaltyCardTable {

    /**
     * Cache for loyalty card IDs.
     */
    private final Set<Integer> ids = new HashSet<>();

    /**
     * Cache for the loyalty cards, stored as a Map from ID to LoyaltyCard.
     */
    private final Map<Integer, LoyaltyCard> loyaltyCards = new HashMap<>();

    /**
     * The name of the table.
     */
    private final String tableName = "loyalty_cards";

    /**
     * The name of the primary key column of the table.
     */
    private final String tableKey = "id";

    /**
     * Constructor for the LoyaltyCardTable.
     * On instantiation, greedy loads the IDs of loyalty cards into cache.
     *
     * @param dbHandler The DatabaseHandler to load the loyalty cards from and save to.
     */
    public LoyaltyCardTblImpl(DatabaseHandler dbHandler) {
        super(dbHandler);

        PreparedStatement getIDsQuery = dbHandler.prepareStatement("SELECT id FROM loyalty_cards;");
        ResultSet results;
        try {
            results = getIDsQuery.executeQuery();
            while (results.next()) {
                ids.add(results.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load loyalty cards.", e);
        }
    }

    @Override
    public LoyaltyCard getLoyaltyCard(int id) {
        LoyaltyCard loyaltyCard = loyaltyCards.get(id);

        if (loyaltyCard == null && ids.contains(id)) {
            try {
                PreparedStatement getCardQuery = dbHandler.prepareStatement("SELECT * FROM loyalty_cards WHERE id=?;");
                getCardQuery.setInt(1, id);
                ResultSet results = getCardQuery.executeQuery();
                if (results.next()) {
                    loyaltyCard = new DbLoyaltyCard(id,
                            results.getDate("lastPurchase").toLocalDate(),
                            results.getString("customerName"),
                            Money.parse(results.getString("balance")));
                    loyaltyCards.put(id, loyaltyCard);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve loyalty card from database.", e);
            }
        }

        return loyaltyCard;
    }

    @Override
    public Set<Integer> getAllLoyaltyCardIDs() {
        return Collections.unmodifiableSet(ids);
    }

    @Override
    public LoyaltyCard putLoyaltyCard(LoyaltyCard loyaltyCard) {
        try {
            PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/insert/insertLoyaltyCard.sql");
            int id = loyaltyCard.getId();
            stmt.setInt(1, id);
            stmt.setString(2, loyaltyCard.getBalance().toString());
            stmt.setString(3, loyaltyCard.getCustomerName());
            stmt.setDate(4, Date.valueOf(loyaltyCard.getLastPurchase()));
            stmt.executeUpdate();
            LoyaltyCard dbLoyaltyCard = new DbLoyaltyCard(loyaltyCard);
            ids.add(id);
            loyaltyCards.put(id, dbLoyaltyCard);
            return dbLoyaltyCard;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save loyalty card to database.", e);
        }
    }

    @Override
    public void removeLoyaltyCard(int id) {
        try {
            PreparedStatement removeCardStmt = dbHandler.prepareResource("/sql/modify/delete/deleteLoyaltyCard.sql");
            removeCardStmt.setInt(1, id);
            removeCardStmt.executeUpdate();
            ids.remove(id);
            loyaltyCards.remove(id);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to remove loyalty card from database.", e);
        }
    }

    @Override
    public Map<Integer, LoyaltyCard> resolveAllLoyaltyCards() {
        return ids.stream() //
                .map(this::getLoyaltyCard) //
                .collect(Collectors.toMap(LoyaltyCard::getId, Function.identity()));
    }

    /**
     * Database specific implementation of a LoyaltyCard, which has database updating on attribute changes.
     */
    private class DbLoyaltyCard extends LoyaltyCardImpl {
        private final Map<String, Object> key;

        public DbLoyaltyCard(int id, LocalDate lastPurchase, String customerName, Money currentBalance) {
            super(id, lastPurchase, customerName, currentBalance);
            key = singletonMap(tableKey, getId());
        }

        public DbLoyaltyCard(LoyaltyCard from) {
            super(from.getId(), from.getLastPurchase(), from.getCustomerName(), from.getBalance());
            key = singletonMap(tableKey, getId());
        }

        @Override
        public void addPoints(Money purchaseCost, int percentage_returned, LocalDate date) {
            updateColumn(tableName, key, "lastPurchase", Date.valueOf(date));
            float result = percentage_returned / 100.0f;
            Money bonus = purchaseCost.multipliedBy(result, RoundingMode.HALF_DOWN);
            Money currentBalance = getBalance();
            updateColumn(tableName, key, "balance", currentBalance.plus(bonus).toString());
            super.addPoints(purchaseCost, percentage_returned, date);
        }

        @Override
        public Money spendPoints(Money purchaseCost) {
            Money newBalance = getBalance().minus(purchaseCost);
            if (newBalance.isNegative()) {
                newBalance = Money.parse("NZD 0");
            }
            updateColumn(tableName, key, "balance", newBalance.toString());
            return super.spendPoints(purchaseCost);
        }

        @Override
        public void setLastPurchase(LocalDate date) {
            updateColumn(tableName, key, "lastPurchase", Date.valueOf(date));
            super.setLastPurchase(date);
        }

        @Override
        public void setBalance(Money balance) {
            updateColumn(tableName, key, "balance", balance.toString());
            super.setBalance(balance);
        }

        @Override
        public void setCustomerName(String name) {
            updateColumn(tableName, key, "customerName", name);
            super.setCustomerName(name);
        }
    }
}
