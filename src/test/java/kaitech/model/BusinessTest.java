package kaitech.model;

import kaitech.api.model.*;
import kaitech.util.PaymentType;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessTest {
    private Business testBusiness;

    @BeforeEach
    public void init() {
        BusinessImpl.reset();
        testBusiness = BusinessImpl.getInstance();
    }

    @Test
    public void setPinTest() {
        assertNull(testBusiness.getPin());
        testBusiness.setPin("0000");
        assertEquals("0000", testBusiness.getPin());
    }

    @Test
    public void setPinTooManyDigitsTest() {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin("00000"),
                "The pin should contain 4 digits only");
    }

    @Test
    public void setPinTooFewDigitsTest() {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin("0"),
                "The pin should contain 4 digits only");
    }

    @Test
    public void setPinWithLettersTest() {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin("t35t"),
                "The pin should contain digits only");
    }

    @Test
    public void setPinWithNonAlphanumericValuesTest() {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin("@@@@"),
                "The pin should contain digits only.");
    }

    @Test
    public void logInTest() {
        testBusiness.setPin("4692");
        assertTrue(testBusiness.logIn("4692"));
    }

    @Test
    public void logInFailTest() {
        testBusiness.setPin("4692");
        assertFalse(testBusiness.logIn("4693"));
        assertFalse(testBusiness.logIn("4629"));
    }

    @Test
    public void cannotLogInWhenAlreadyLoggedInTest() {
        testBusiness.setPin("4692");
        assertTrue(testBusiness.logIn("4692"));
        assertThrows(IllegalStateException.class, () -> testBusiness.logIn("4692"),
                "The user is already logged in.");
    }

    @Test
    public void cannotLogInWhenPinNotSetTest() {
        assertThrows(IllegalStateException.class, () -> testBusiness.logIn("4692"),
                "A pin has not been set yet.");
    }

    @Test
    public void logOutTest() {
        testBusiness.setPin("4692");
        testBusiness.logIn("4692");
        assertTrue(testBusiness.isLoggedIn());
        testBusiness.logOut();
        assertFalse(testBusiness.isLoggedIn());
    }
}