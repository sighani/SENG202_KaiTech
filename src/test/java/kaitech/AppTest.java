package kaitech;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
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

    @Test
    public void michaelTest() {assertEquals(4 * 5, 20);}

    @Test
    public void samanTest() {
        assertEquals(4 + 9, 13);
    }

    @Test
    public void AmyTest() {
        assertEquals(5+5, 10);
    }
}
