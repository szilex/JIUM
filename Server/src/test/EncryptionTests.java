package test;

import model.Algorithm;
import model.AlgorithmException;
import model.KeysGenerator;
import model.KeysGeneratorInitializationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Base64;

/**
 * Class for testing public methods of rsa_model package, excluding constructors, setters and getters,
 * It is checking the encrypt method from Algorithm class.
 * @author Michal Szeler
 * @version 2.0
 */
public class EncryptionTests {
    /**
     * Static field for an instance of Algorithm class
     */
    private static Algorithm algorithm;
    /**
     * Static field for an instance of Private Key string representation
     */
    private static String publicKeyString;
    /**
     * Static field for an instance of Private Key string representation
     */
    private static String additionalPublicKeyString;
    /**
     * Static field that indicates whether the setUp method successfully initialized all variables
     */
    private static boolean setUpIsDone = false;
    /**
     * Static field that indicates whether additional fields were successfully initialized
     */
    private static boolean additionalSetUpIsDone = false;

    /**
     * Method initializes essential private fields in the class
     */
    @BeforeClass
    public static void setUpClass() {
        try{
            KeysGenerator generator = new KeysGenerator();
            publicKeyString =  Base64.getEncoder().encodeToString(generator.getPublicKey().getEncoded());
            algorithm = new Algorithm();
            setUpIsDone = true;
        } catch(KeysGeneratorInitializationException e){
            System.out.println("Cannot run tests, KeysGenerator could not be initialized");
        }
    }

    /**
     * Method that initializes additional private fields in the class required to perform some tests.
     * New Public Key can not be the same as the one that has already been initialized
     */
    @Before
    public void setUp(){
        try{
            while(!additionalSetUpIsDone){
                KeysGenerator additionalGenerator = new KeysGenerator();
                additionalPublicKeyString =  Base64.getEncoder().encodeToString(additionalGenerator.getPublicKey().getEncoded());
                if(!publicKeyString.matches(additionalPublicKeyString))
                    additionalSetUpIsDone = true;
            }
        } catch(KeysGeneratorInitializationException e){
            System.out.println("Cannot run tests, additionalKeysGenerator could not be initialized");
        }
    }

    /**
     * Test checks if encrypting the same message using different Public Keys returns different results
     * In this case, message is a short text
     */
    @Test
    public void encryptCorrectMessageWithDifferentPublicKeysTest(){
        if(setUpIsDone&&additionalSetUpIsDone) {
            try {
                String message = "Regular text to encrypt";
                Assert.assertNotEquals("Text after encryption is the same", algorithm.encrypt(message, publicKeyString), algorithm.encrypt(message,additionalPublicKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if encrypting the same empty message using different Public Keys returns different results
     * In this case, message is empty
     */
    @Test
    public void encryptEmptyMessageWithCorrectPublicKeyTest(){
        if(setUpIsDone&&additionalSetUpIsDone) {
            try {
                String message="";
                Assert.assertNotEquals("Text after encryption is not the same", algorithm.encrypt(message,publicKeyString), algorithm.encrypt(message, additionalPublicKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if encrypting the same message using the same Public Key returns different results, as it should, because each time padding might be applied differently
     * In this case, message is a short text
     */
    @Test
    public void encryptCorrectMessageWithSamePublicKeyTest(){
        if(setUpIsDone) {
            try {
                String message="To be encrypted or not to be encrypted, that is the question";
                Assert.assertNotEquals("Text after encryption is not the same", algorithm.encrypt(message,publicKeyString), algorithm.encrypt(message,publicKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if encrypting the same message using the same Public Key returns different results, as it should, because each time padding is applied differently
     * In this case, message is empty
     */
    @Test
    public void encryptEmptyMessageWithSamePublicKeyTest(){
        if(setUpIsDone) {
            try {
                String message = "";
                Assert.assertNotEquals("Text after encryption is not the same", algorithm.encrypt(message,publicKeyString), algorithm.encrypt(message,publicKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if encryption threw an exception after receiving null data argument
     */
    @Test
    public void encryptNullMessageWithCorrectPublicKeyTest(){
        if(setUpIsDone) {
            try {
                algorithm.encrypt(null, publicKeyString);
                Assert.fail("AlgorithmException was not thrown");
            } catch (AlgorithmException e) {
                Assert.assertEquals("Argument value was null", e.getMessage());
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if encryption method threw an exception after receiving null Public Key
     */
    @Test
    public void encryptCorrectMessageWithNullPublicKeyTest(){
        if(setUpIsDone) {
            try {
                algorithm.encrypt("Message", null);
                Assert.fail("AlgorithmException was not thrown");
            } catch (AlgorithmException e) {
                Assert.assertEquals("Argument value was null", e.getMessage());
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if encryption method threw an exception after receiving empty Public Key
     */
    @Test
    public void encryptCorrectMessageWithEmptyPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String message="Message";
                algorithm.encrypt(message, "");
                Assert.fail("AlgorithmException was not thrown");
            } catch (AlgorithmException e) {
                Assert.assertEquals("Exception message is incorrect","X.509 key specification is invalid", e.getMessage());
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if encryption method threw an exception after receiving incorrectly formatted Public Key
     */
    @Test
    public void encryptCorrectMessageWithBadlyFormattedPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String message="Message";
                algorithm.encrypt(message, "grdfs435adfsrg23423537sdfg");
                Assert.fail("AlgorithmException was not thrown");
            } catch (AlgorithmException e) {
                Assert.assertEquals("Exception message is incorrect","X.509 key specification is invalid", e.getMessage());
            }
        }
        else {
            Assert.fail();
        }
    }
}
