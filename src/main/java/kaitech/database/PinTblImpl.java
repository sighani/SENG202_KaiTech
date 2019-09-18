package kaitech.database;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import kaitech.api.database.AbstractTable;
import kaitech.api.database.PinTable;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * PinTblImpl extends AbstractTable, implements the PinTable interface,
 * and permits limited access to the data stored in the Pins table.
 *
 * @author Julia Harrison
 */
public class PinTblImpl extends AbstractTable implements PinTable {
    private static final char[] SALT_CHARS = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
            "12345678990-=~!@#$%^&*()_+").toCharArray();

    private final Set<String> names = new HashSet<>();
    private final Map<String, String> hashes = new HashMap<>();
    private final Map<String, String> salts = new HashMap<>();

    public PinTblImpl(DatabaseHandler dbHandler) {
        super(dbHandler);
        PreparedStatement getNamesQuery = dbHandler.prepareStatement("SELECT name FROM pins;");
        ResultSet results;
        try {
            results = getNamesQuery.executeQuery();
            while (results.next()) {
                names.add(results.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load pins.", e);
        }
    }

    @Override
    public Set<String> getAllNames() {
        return Collections.unmodifiableSet(names);
    }

    @Override
    public String getHashedPin(String name) {
        String hashedPin = hashes.get(name);

        if (hashedPin == null && names.contains(name)) {
            try {
                PreparedStatement stmt = dbHandler.prepareStatement("SELECT hash FROM pins WHERE name=?;");
                stmt.setString(1, name);
                ResultSet results = stmt.executeQuery();
                if (results.next()) {
                    hashedPin = results.getString("hash");
                    hashes.put(name, hashedPin);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve hashed pin from database.", e);
            }
        }

        return hashedPin;
    }

    @Override
    public void putPin(String name, String hashedPin, String salt) {
        try {
            PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/insert/insertPin.sql");
            stmt.setString(1, name);
            stmt.setString(2, hashedPin);
            stmt.setString(3, salt);
            stmt.executeUpdate();
            names.add(name);
            hashes.put(name, hashedPin);
            salts.put(name, salt);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save pin to database.", e);
        }
    }

    @Override
    public void removePin(String name) {
        try {
            PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/delete/deletePin.sql");
            stmt.setString(1, name);
            stmt.executeUpdate();
            names.remove(name);
            hashes.remove(name);
            salts.remove(name);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to remove pin from database", e);
        }
    }

    @Override
    public void updatePin(String name, String newHashedPin) {
        try {
            PreparedStatement stmt = dbHandler.prepareStatement("UPDATE pins SET hash=? WHERE name=?;");
            stmt.setString(1, newHashedPin);
            stmt.setString(2, name);
            stmt.executeUpdate();
            hashes.put(name, newHashedPin);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update pin in database.", e);
        }
    }

    @Override
    public String getSalt(String name) {
        String salt = salts.get(name);

        if (salt == null && names.contains(name)) {
            try {
                PreparedStatement stmt = dbHandler.prepareStatement("SELECT salt FROM pins WHERE name=?;");
                stmt.setString(1, name);
                ResultSet results = stmt.executeQuery();
                if (results.next()) {
                    salt = results.getString("salt");
                    salts.put(name, salt);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve salt from database.", e);
            }
        }

        return salt;
    }

    @Override
    public void updateSalt(String name, String newSalt) {
        try {
            PreparedStatement stmt = dbHandler.prepareStatement("UPDATE pins SET salt=? WHERE name=?;");
            stmt.setString(1, newSalt);
            stmt.setString(2, name);
            stmt.executeUpdate();
            salts.put(name, newSalt);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update salt in database.", e);
        }
    }

    @Override
    public String generateSalt(int length) {
        SecureRandom randy = new SecureRandom();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(SALT_CHARS[randy.nextInt(SALT_CHARS.length)]);
        }
        return builder.toString();
    }

    @Override
    public String hashPin(CharSequence pin, String salt) {
        Hasher hasher = Hashing.sha256().newHasher();
        hasher.putString(pin, StandardCharsets.UTF_8);
        hasher.putString(salt, StandardCharsets.UTF_8);
        return hasher.hash().toString();
    }
}
