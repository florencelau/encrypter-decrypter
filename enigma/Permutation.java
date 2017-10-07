package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Florence Lau
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters not
     *  included in any cycle map to themselves. Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        if (cycles.length() == 0) {
            for (int i2 = 0; i2 < _alphabet.size(); i2 += 1) {
                _permutation.put(i2, i2);
                reversedPerm = _permutation;
                mapsToItself = true;
            }
        } else {
            cycles = cycles.replaceAll("\\s+", "");
            cycles = cycles.replaceAll("\\(", "");
            String[] splitCycles = cycles.split("\\)");
            for (String cycle : splitCycles) {
                addCycle(cycle);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        int i = 0;
        int lastIndex = cycle.length() - 1;
        while (i < lastIndex) {
            char curr = cycle.charAt(i);
            if (_alphabet.contains(curr)) {
                _permutation.put(charToInt(curr),
                        charToInt(cycle.charAt(i + 1)));
                i += 1;
            } else {
                throw error("character is not in alphabet");
            }
        }
        _permutation.put(charToInt(cycle.charAt(lastIndex)),
                charToInt(cycle.charAt(0)));
        for (int i2 = 0; i2 < size(); i2 += 1) {
            if (!_permutation.containsKey(i2)) {
                _permutation.put(i2, i2);
                mapsToItself = true;
            }
        }
        for (int i3 : _permutation.keySet()) {
            reversedPerm.put(_permutation.get(i3), i3);
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _permutation.get(wrap(p));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return reversedPerm.get(wrap(c));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return intToChar(_permutation.get(charToInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        return intToChar(reversedPerm.get(charToInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return mapsToItself;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Return the character C converted into its corresponding integer. */
    int charToInt(char c) {
        return _alphabet.toInt(c);
    }

    /** Return the integer I converted into its corresponding character. */
    char intToChar(int i) {
        return _alphabet.toChar(i);
    }

    /** Variable for whether this permutation is a derangement. */
    private boolean mapsToItself = false;

    /** HashMap of each character mapping in this permutation. */
    private HashMap<Integer, Integer> _permutation = new HashMap<>();

    /** Reversed mapping of this permutation. */
    private HashMap<Integer, Integer> reversedPerm = new HashMap<>();
}

