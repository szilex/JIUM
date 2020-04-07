package main.java.model;

import java.lang.annotation.*;

/**
 * An interface to specify the details of methods from Algorithm class.
 * It uses enum objects to represent type of operation performed by method as well as Encoding used in them
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Details {
    /**
     * Enum variable that represents potential values of encodings used in Algorithm class
     */
    enum Encoding {
        Base64, X509, PKCS8, None
    }

    /**
     * Enum variable that represents potential values of operations performed in Algorithm class
     */
    enum Operation {
        ConvertStringToPublicKey, ConvertStringToPrivateKey, Decrypt, Encrypt, None
    }

    /**
     * Variable that holds the value of Encoding used in method
     */
    Encoding encoding() default Encoding.None;

    /**
     * Variable that holds the value of Operation used in method
     */
    Operation operation() default Operation.None;

    /**
     * Variable that contains the description of method
     */
    String description();

    /**
     * Variable that contains the author of method
     */
    String author();
}