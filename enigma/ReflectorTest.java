package enigma;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Reflector class.
 *  @author Florence Lau
 */
public class ReflectorTest {

    /** Instantiate reflector. */
    public Reflector makeReflector() {
        Permutation myPerm = new Permutation("(AELTPHQXRU) (BKNW) "
                + "(CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        return new Reflector("testingReflector", myPerm);
    }

    /** Testing whether errors are thrown when they are supposed to be. */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testMethods() {
        assertEquals(makeReflector().reflecting(), true);
        exception.expect(EnigmaException.class);
        makeReflector().set(8);
    }
}
