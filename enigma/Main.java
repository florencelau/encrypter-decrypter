package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Florence Lau
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);


        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        myMachine = readConfig();
        setUp(myMachine, _input.nextLine().toUpperCase());
        while (_input.hasNextLine()) {
            String nextMsgLine = _input.nextLine().toUpperCase();
            if (nextMsgLine.startsWith("*")) {
                myMachine = new Machine(_alphabet, nRotors,
                        nPawls, availRotors);
                setUp(myMachine, nextMsgLine);
            } else {
                printMessageLine(myMachine.convert(nextMsgLine));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _config.useDelimiter("[\\s\\t\\n]+");
            String tempAlpha = _config.next();
            if (tempAlpha.contains(" ") || tempAlpha.contains("*")
                    || tempAlpha.contains("(") || tempAlpha.contains(")")
                    || tempAlpha.matches(".*[a-z].*")) {
                throw error("alphabet contains invalid character");
            }
            _alphabet = new Alphabet(tempAlpha);
            if (!_config.hasNextInt()) {
                throw error("missing number of rotors");
            } else {
                nRotors = _config.nextInt();
            }
            if (!_config.hasNextInt()) {
                throw error("missing number of pawls");
            } else {
                nPawls = _config.nextInt();
            }
            if (nPawls > nRotors) {
                throw error("number of pawls cannot exceed number of rotors");
            }
            while (_config.hasNext()) {
                availRotors.add(readRotor());
            }
            return new Machine(_alphabet, nRotors, nPawls, availRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rotorName = _config.next().toUpperCase();
            String rotorTypeNotches = _config.next().toUpperCase();
            char rotorType = rotorTypeNotches.charAt(0);
            String rotorNotches = "";
            if (rotorType == 'M') {
                for (int i = 1; i < rotorTypeNotches.length(); i++) {
                    rotorNotches +=
                            Character.toString(rotorTypeNotches.charAt(i));
                }
            }
            String rotorCycles = "";
            String lastCycle = "";
            while (_config.hasNext("\\(\\S+")) {
                lastCycle = _config.next();
                rotorCycles = rotorCycles + lastCycle + " ";
            }
            if (!lastCycle.endsWith(")")) {
                throw error("cycles must end with parentheses");
            }
            Permutation myPermutation = new Permutation(rotorCycles, _alphabet);
            if (rotorType == 'M') {
                return new MovingRotor(rotorName, myPermutation, rotorNotches);
            } else if (rotorType == 'N') {
                return new FixedRotor(rotorName, myPermutation);
            } else if (rotorType == 'R') {
                return new Reflector(rotorName, myPermutation);
            } else {
                throw error("invalid rotor type");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] settingsArray = settings.trim().split("\\s+");
        String[] usedRotors = new String[nRotors];
        int settingsIndex = 0;
        String plugb = "";
        if (nRotors > settingsArray.length - 1) {
            throw error("number of rotors in settings line does "
                    + "not match that of config");
        }
        if (!settingsArray[0].equals("*")) {
            throw error("missing * in the settings line, wrong format");
        }
        for (int i = 1; i <= nRotors; i++) {
            usedRotors[i - 1] = settingsArray[i].toUpperCase();
            settingsIndex = i + 1;
        }
        M.insertRotors(usedRotors);
        if (settingsIndex > settingsArray.length - 1) {
            throw error("missing rotor settings");
        }
        String rotorSettings = settingsArray[settingsIndex];
        M.setRotors(rotorSettings);
        if (settingsIndex < settingsArray.length - 1) {
            for (int i = settingsIndex + 1; i < settingsArray.length; i++) {
                String aCycle = settingsArray[i];
                if (aCycle.startsWith("(") && aCycle.endsWith(")")
                        && aCycle.length() == 4) {
                    if (!plugb.contains(Character.toString(aCycle.charAt(1)))
                            && !plugb.contains(Character.toString
                            (aCycle.charAt(2)))) {
                        plugb += aCycle;
                    } else {
                        throw error("duplicate character in "
                                + "plugboard not allowed");
                    }
                    if (i != settingsArray.length - 1) {
                        plugb += " ";
                    }
                } else {
                    throw error("plugboard permutations must be in pairs");
                }
            }
            Permutation plugbPerm = new Permutation(plugb, _alphabet);
            M.setPlugboard(plugbPerm);
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = msg.replaceAll("\\s", "");
        char[] msgArray = msg.toCharArray();
        String result = "";
        for (int i = 0; i < msgArray.length; i++) {
            result += msgArray[i];
            if ((i + 1) % 5 == 0) {
                result += " ";
            }
        }
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Collection of available rotors this machine can use. */
    private Collection<Rotor> availRotors = new ArrayList<>();

    /** Number of rotors this machine has. */
    private int nRotors;

    /** Number of pawls this machine has. */
    private int nPawls;

    /** The current machine in use. */
    private Machine myMachine;
}
