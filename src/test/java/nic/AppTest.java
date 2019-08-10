package nic;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void jackieTest() {
        assertTrue(1+1==2);
    }

    @Test
    public void juliaTest() {
        assertNotEquals(9 + 10, 21);
    }
}
