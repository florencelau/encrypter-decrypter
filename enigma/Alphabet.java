package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Florence Lau
 */
class Alphabet {

    /** A new alphabet mapping from characters to and from
     indices into the alphabet. */
    private HashMap<Character, Integer> alphabetMap = new HashMap<>();

    /** Reverse of this alphabet map. */
    private HashMap<Integer, Character> integersMap = new HashMap<>();


    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        for (int i = 0; i < chars.length(); i++) {
            if (alphabetMap.containsKey(chars.charAt(i))) {
                throw error("duplicate characters not allowed "
                        + "in alphabet");
            } else {
                alphabetMap.put(chars.charAt(i), i);
            }
        }
        for (char c : alphabetMap.keySet()) {
            integersMap.put(alphabetMap.get(c), c);
        }
    }

    /** Returns the size of the alphabet. */
    int size() {
        return alphabetMap.size();
    }

    /** Returns true if C is in this alphabet. */
    boolean contains(char c) {
        return alphabetMap.containsKey(c);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index >= 0 && index < size()) {
            return integersMap.get(index);
        } else {
            throw error("character index out of range");
        }
    }

    /** Returns the index of character C, which must be in the alphabet. */
    int toInt(char c) {
        if (contains(c)) {
            return alphabetMap.get(c);
        } else {
            throw error("character not in alphabet");
        }
    }

}
