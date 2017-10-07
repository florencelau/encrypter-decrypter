package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Rotor class.
 *  @author Florence Lau
 */
public class RotorTest {

    /** Instance variables. */
    private Permutation myPerm;
    private Rotor myRotor;

    /** Instantiates my rotor. */
    public void makeRotor() {
        myPerm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZ) (S)", UPPER);
        myRotor = new Rotor("testingRotor", myPerm);
    }

    @Test
    public void testInstanceVariables() {
        makeRotor();
        assertEquals(myRotor.name(), "testingRotor");
        assertEquals(myRotor.alphabet(), UPPER);
        assertEquals(myRotor.permutation(), myPerm);
        assertEquals(myRotor.size(), 26);
        assertEquals(myRotor.setting(), 0);
    }
    @Test
    public void testMethods() {
        makeRotor();
        myRotor.set(5);
        assertEquals(myRotor.setting(), 5);
        myRotor.set('B');
        assertEquals(myRotor.setting(), 1);
        myRotor.advance();
        assertEquals(myRotor.setting(), 1);
        assertEquals(myRotor.convertForward(6), 15);
        assertEquals(myRotor.convertBackward(7), 20);
    }
}
