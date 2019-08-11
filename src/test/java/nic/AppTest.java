package nic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void jackieTest() {
        assertTrue(1 + 1 == 2);
    }

    @Test
    public void juliaTest() {
        assertNotEquals(9 + 10, 21);
    }

    @Test
    public void oisinTest() {
        assertEquals(2*2, 4);
    }
}
