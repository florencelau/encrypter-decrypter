package enigma;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Florence Lau
 */
public class MachineTest {

    /** Instantiates my machine. */
    public Machine makeMyMachine() {
        Collection<Rotor> allRotors = new ArrayList<>();
        Permutation p1 = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZ) (S)", UPPER);
        Rotor r1 = new MovingRotor("r1", p1, "Q");
        Permutation p2 = new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) "
                + "(BJ) (GR) (NT) (A) (Q)", UPPER);
        Rotor r2 = new MovingRotor("r2", p2, "E");
        Permutation p3 = new Permutation("(ABDHPEJT) "
                + "(CFLVMZOYQIRWUKXSG) (N)", UPPER);
        Rotor r3 = new MovingRotor("r3", p3, "V");
        Permutation p4 = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) "
                + "(DV) (KU)", UPPER);
        Rotor r4 = new MovingRotor("r4", p4, "J");
        Permutation p5 = new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) "
                + "(EGTJPX)", UPPER);
        Rotor r5 = new MovingRotor("r5", p5, "Z");
        Permutation p6 = new Permutation("(AJQDVLEOZWIYTS) "
                + "(CGMNHFUX) (BPRK)", UPPER);
        Rotor r6 = new MovingRotor("r6", p6, "ZM");
        Permutation p7 = new Permutation("(AE) (BN) (CK) (DQ) "
                + "(FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
        Rotor reflectorB = new Reflector("reflectorB", p7);
        Permutation p8 = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) "
                + "(HIX)", UPPER);
        Rotor beta = new FixedRotor("beta", p8);
        allRotors.add(r1);
        allRotors.add(r2);
        allRotors.add(r3);
        allRotors.add(r4);
        allRotors.add(r5);
        allRotors.add(r6);
        allRotors.add(reflectorB);
        allRotors.add(beta);
        return new Machine(UPPER, 5, 3, allRotors);
    }

    @Test
    public void testConvertLetter() {
        Machine myMachine = makeMyMachine();
        assertEquals(myMachine.numRotors(), 5);
        assertEquals(myMachine.numPawls(), 3);
        String[] myRotors = {"reflectorB", "beta", "r3", "r4", "r1"};
        myMachine.insertRotors(myRotors);
        myMachine.setRotors("AXLE");
        Permutation pBoard = new Permutation("(YF) (ZH)", UPPER);
        myMachine.setPlugboard(pBoard);
        assertEquals(myMachine.convert(UPPER.toInt('Y')), UPPER.toInt('Z'));
    }

    @Test
    public void testConvertLetter2() {
        Machine myMachine4 = makeMyMachine();
        String[] myRotors = {"reflectorB", "beta", "r3", "r2", "r1"};
        myMachine4.insertRotors(myRotors);
        myMachine4.setRotors("AVAA");
        assertEquals(myMachine4.convert(UPPER.toInt('A')), UPPER.toInt('T'));
    }

    @Test
    public void testConvertMsgWithPlugboard() {
        Machine myMachine2 = makeMyMachine();
        assertEquals(myMachine2.numRotors(), 5);
        assertEquals(myMachine2.numPawls(), 3);
        String[] myRotors2 = {"reflectorB", "beta", "r1", "r2", "r3"};
        myMachine2.insertRotors(myRotors2);
        myMachine2.setRotors("AAAA");
        Permutation pBoard2 = new Permutation("(AQ) (EP)", UPPER);
        myMachine2.setPlugboard(pBoard2);
        assertEquals(myMachine2.convert("Hello world"), "IHBDQ QMTQZ");
    }

    @Test
    public void testConvertMsgWithoutPlugboard() {
        Machine myMachine3 = makeMyMachine();
        assertEquals(myMachine3.numRotors(), 5);
        assertEquals(myMachine3.numPawls(), 3);
        String[] myRotors2 = {"reflectorB", "beta", "r1", "r2", "r3"};
        myMachine3.insertRotors(myRotors2);
        myMachine3.setRotors("AAAA");
        assertEquals(myMachine3.convert("Hello world"), "ILBDA AMTAZ");
    }
}
