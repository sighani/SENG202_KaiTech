package kaitech.database;

import kaitech.api.database.PinTable;
import kaitech.api.model.Business;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestPinDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private PinTable pinTable;

    public void init() throws Throwable {
        dbHandler = new DatabaseHandler(tempFolder.newFile());
        PreparedStatement stmt = dbHandler.prepareResource("/sql/setup/setupPinTbl.sql");
        stmt.executeUpdate();
        pinTable = new PinTblImpl(dbHandler);
    }

    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    @Test
    public void testGetPin() throws Throwable {
        init();

        String salt = pinTable.generateSalt(64);
        String hash = pinTable.hashPin("0000", salt);
        pinTable.putPin(Business.DEFAULT_USER, hash, salt);

        String salt2 = pinTable.generateSalt(64);
        String hash2 = pinTable.hashPin("1111", salt2);
        pinTable.putPin("Alt", hash2, salt2);

        assertEquals(2, pinTable.getAllNames().size());
        assertEquals(hash, pinTable.getHashedPin(Business.DEFAULT_USER));
        assertEquals(hash2, pinTable.getHashedPin("Alt"));
        assertNotEquals(hash, hash2);

        PinTable otherPinTable = new PinTblImpl(dbHandler);
        assertEquals(hash, otherPinTable.getHashedPin(Business.DEFAULT_USER));
        assertEquals(salt, otherPinTable.getSalt(Business.DEFAULT_USER));
        assertEquals(hash2, otherPinTable.getHashedPin("Alt"));
        assertEquals(salt2, otherPinTable.getSalt("Alt"));

        teardown();
    }

    @Test
    public void testRemovePin() throws Throwable {
        init();

        String salt = pinTable.generateSalt(64);
        String hash = pinTable.hashPin("0000", salt);
        pinTable.putPin(Business.DEFAULT_USER, hash, salt);

        String salt2 = pinTable.generateSalt(64);
        String hash2 = pinTable.hashPin("1111", salt2);
        pinTable.putPin("Alt", hash2, salt2);

        assertEquals(2, pinTable.getAllNames().size());
        assertTrue(pinTable.getAllNames().contains(Business.DEFAULT_USER));
        assertTrue(pinTable.getAllNames().contains("Alt"));
        pinTable.removePin("Alt");
        assertEquals(1, pinTable.getAllNames().size());
        assertTrue(pinTable.getAllNames().contains(Business.DEFAULT_USER));
        assertFalse(pinTable.getAllNames().contains("Alt"));

        teardown();
    }
}
