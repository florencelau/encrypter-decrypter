package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Florence Lau
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
    }

    @Override
    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return true;
    }

    @Override
    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        char c = alphabet().toChar(setting());
        if (_notches.contains(Character.toString(c))) {
            return true;
        }
        return false;
    }

    @Override
    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
        int advanced = setting() + 1;
        if (advanced >= size()) {
            set(permutation().wrap(setting() + 1));
        } else {
            set(advanced);
        }
    }

    /** My notches. */
    private String _notches;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** My setting. */
    private int _setting;

}
