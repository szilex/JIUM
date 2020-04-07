package main.java.model;

/**
 * Class represents an exception which occurred while using RSA Algorithm class
 * @author Michal Szeler
 * @version 2.0
 */
public class AlgorithmException extends Exception {
    /**
     * Package-private constructor with one parameter, constructor of base class is invoked
     * @param message Description and cause of the error
     */
    AlgorithmException(String message) {
        super(message);
    }
}