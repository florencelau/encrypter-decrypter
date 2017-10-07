package enigma;

import org.junit.Test;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the FixedRotor class.
 *  @author Florence Lau
 */
public class FixedRotorTest {

    /** Instance variables. */
    FixedRotor testingFixedRotor;

    /** Instantiate fixed rotor. */
    public void makeFixedRotor() {
        Permutation myPerm = new Permutation("(AELTPHQXRU)"
                + " (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        testingFixedRotor = new FixedRotor("testingFixedRotor", myPerm);
    }

    @Test
    public void testMethods() {
        makeFixedRotor();
        assertEquals(testingFixedRotor.reflecting(), false);
        assertEquals(testingFixedRotor.atNotch(), false);
        testingFixedRotor.advance();
        assertEquals(testingFixedRotor.setting(), 0);
    }
}
