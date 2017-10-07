package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Florence Lau
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != _numRotors) {
            throw error("wrong number of rotors");
        }
        ArrayList<String> rotorsAdded = new ArrayList<>();
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor aRotor : _allRotors) {
                String rotorName = aRotor.name();
                if (rotorName.equals(rotors[i])) {
                    if (i == 0) {
                        if (!aRotor.reflecting()) {
                            throw error("first rotor is not the reflector");
                        }
                    }
                    if (rotorsAdded.contains(rotorName)) {
                        throw error("no duplicate rotors allowed");
                    }
                    if (aRotor.rotates() && (i + 1) < _pawls) {
                        throw error("moving rotor is placed to the left of"
                                + " a fixed rotor");
                    }
                    myRotors.add(aRotor);
                    rotorsAdded.add(rotors[i]);
                }
            }
            if (!rotorsAdded.contains(rotors[i])) {
                throw error("invalid rotor");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("wrong number of settings");
        } else {
            for (int i = 1; (i - 1) < setting.length(); i++) {
                Rotor curr = myRotors.get(i);
                curr.set(setting.charAt(i - 1));
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
        noPlugboard = false;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int firstMovingRotor = numRotors() - numPawls();
        for (int advanceIndex = firstMovingRotor;
             advanceIndex < numRotors(); advanceIndex += 1) {
            Rotor curr1 = myRotors.get(advanceIndex);
            if (advanceIndex == numRotors() - 1) {
                curr1.advance();
            } else if (advanceIndex == firstMovingRotor) {
                Rotor next = myRotors.get(advanceIndex + 1);
                if (next.atNotch()) {
                    curr1.advance();
                }
            } else {
                Rotor next = myRotors.get(advanceIndex + 1);
                if (curr1.atNotch() || next.atNotch()) {
                    curr1.advance();
                }
            }
        }
        if (!noPlugboard) {
            c = _plugboard.permute(c);
        }
        for (int goForwardIndex = numRotors() - 1; goForwardIndex > 0;
             goForwardIndex -= 1) {
            Rotor curr2 = myRotors.get(goForwardIndex);
            c = curr2.convertForward(c);
        }
        for (int goBackwardsIndex = 0; goBackwardsIndex < myRotors.size();
             goBackwardsIndex += 1) {
            Rotor curr3 = myRotors.get(goBackwardsIndex);
            c = curr3.convertBackward(c);
        }
        if (!noPlugboard) {
            c = _plugboard.invert(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == ' ') {
                result += " ";
            } else {
                int convertedInt = convert(_alphabet.toInt(msg.charAt(i)));
                char letterToAdd = _alphabet.toChar(convertedInt);
                result += Character.toString(letterToAdd);
            }
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotor slots I have. */
    private int _numRotors;

    /** Number of pawls I have. */
    private int _pawls;

    /** Collection of all available rotors I have. */
    private Collection<Rotor> _allRotors;

    /** Array list of my rotors. */
    private ArrayList<Rotor> myRotors = new ArrayList<>();

    /** My plugboard. */
    private Permutation _plugboard;

    /** Variable for whether I have a plugboard. */
    private boolean noPlugboard = true;
}
