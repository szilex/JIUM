package model;

/**
 * Class represents an exception which occurred during initialization of RSA Keys Generator
 * @author Michal Szeler
 * @version 2.0
 */
public class KeysGeneratorInitializationException extends Exception {

    /**
     * Package-private constructor with one parameter, constructor of base class is invoked
     * @param message Description and cause of the error
     */
    KeysGeneratorInitializationException(String message) {
        super(message);
    }
}
