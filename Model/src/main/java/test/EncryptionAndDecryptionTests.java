package main.java.test;

import main.java.model.Algorithm;
import main.java.model.AlgorithmException;
import main.java.model.KeysGenerator;
import main.java.model.KeysGeneratorInitializationException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Base64;

/**
 * Class for testing public methods from rsa_model package, excluding constructors, setters and getters,
 * It is checking cooperation between encrypt and decrypt method from Algorithm class
 * @author Michal Szeler
 * @version 2.0
 */
public class EncryptionAndDecryptionTests {
    /**
     * Static field for an instance of Algorithm class
     */
    private static Algorithm algorithm;
    /**
     * Static field for an instance of Private Key string representation
     */
    private static String privateKeyString;
    /**
     * Static field for an instance of Private Key string representation
     */
    private static String publicKeyString;
    /**
     * Static field that indicates whether the setUp method successfully initialized all variables
     */
    private static boolean setUpIsDone = false;

    /**
     * Method initializes all private fields in the class
     */
    @BeforeClass
    public static void setUp() {
        try{
            KeysGenerator generator = new KeysGenerator();
            privateKeyString = Base64.getEncoder().encodeToString(generator.getPrivateKey().getEncoded());
            publicKeyString =  Base64.getEncoder().encodeToString(generator.getPublicKey().getEncoded());
            algorithm = new Algorithm();
            setUpIsDone = true;
        } catch(KeysGeneratorInitializationException e){
            System.out.println("Cannot run tests, KeysGenerator could not be initialized");
        }
    }

    /**
     * Test checks if decryption of encrypted message passed successfully
     * In this case, message is a short text
     */
    @Test
    public void encryptAndDecryptCorrectMessageWithCorrectKeysTest(){
        if(setUpIsDone) {
            try {
                String message = "Regular text to encrypt";
                Assert.assertEquals("Text after encrypting and decrypting is not the same", message, algorithm.decrypt(algorithm.encrypt(message, publicKeyString), privateKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
   }

    /**
     * Test checks if decryption of encrypted message passed successfully
     * In this case, message is empty
     */
   @Test
    public void encryptAndDecryptEmptyMessageWithCorrectKeysTest(){
       if(setUpIsDone) {
           try {
               String message="";
               Assert.assertEquals("Text after encrypting and decrypting is not the same", message, algorithm.decrypt(algorithm.encrypt(message, publicKeyString), privateKeyString));
           } catch (AlgorithmException e) {
               Assert.fail();
           }
       }
       else {
           Assert.fail();
       }
   }

    /**
     * Test checks if decryption of encrypted message passed successfully
     * In this case, message is a short text in Polish, which contains special characters
     */
    @Test
    public void encryptAndDecryptCorrectMessageInPolishWithCorrectKeysTest(){
        if(setUpIsDone) {
            try {
                String message="Język polski nie powoduje błędów";
                Assert.assertEquals("Text after encrypting and decrypting is not the same", message, algorithm.decrypt(algorithm.encrypt(message, publicKeyString), privateKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if decryption of encrypted message passed successfully
     * In this case, message is a long text
     */
    @Test
    public void encryptAndDecryptCorrectLongMessageWithCorrectKeysTest(){
        if(setUpIsDone) {
            try {
                String message =
                        "Exquisite cordially mr happiness of neglected distrusts. "+
                        "Boisterous impossible unaffected he me everything. " +
                        "Is fine loud deal an rent open give. Find upon and sent spot song son eyes. "+
                        "Do endeavor he differed carriage is learning my graceful. Feel plan know is he like on pure. "+
                        "See burst found sir met think hopes are marry among. Delightful remarkably new assistance saw literature mrs favourable. \n\n" +
                        "In reasonable compliment favourable is connection dispatched in terminated. Do esteem object we called father excuse remove. "+
                        "So dear real on like more it. Laughing for two families addition expenses surprise the. If sincerity he to curiosity arranging. "+
                        "Learn taken terms be as. Scarcely mrs produced too removing new old. \n";

                Assert.assertEquals("Text after encrypting and decrypting is not the same", message, algorithm.decrypt(algorithm.encrypt(message, publicKeyString), privateKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if decryption or encryption threw an exception after receiving null message
     */
    @Test
    public void encryptAndDecryptNullMessageWithCorrectKeysTest(){
        if(setUpIsDone) {
            try {
                algorithm.decrypt(algorithm.encrypt(null, publicKeyString), privateKeyString);
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
    public void encryptAndDecryptCorrectMessageWithNullPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String message="Message";
                algorithm.decrypt(algorithm.encrypt(message, null), privateKeyString);
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
     * Test checks if decryption method threw an exception after receiving null Private Key
     */
    @Test
    public void encryptAndDecryptCorrectMessageWithNullPrivateKeyTest(){
        if(setUpIsDone) {
            try {
                String message="Message";
                algorithm.decrypt(algorithm.encrypt(message, publicKeyString), null);
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
     * Test checks if encryption and decryption using uncorrelated keys returns different message
     */
    @Test
    public void encryptAndDecryptCorrectMessageWithUncorrelatedKeysTest(){
        if(setUpIsDone) {
            try {
                boolean additionalSetUpIsDone = false;
                String additionalPublicKeyString="";
                try{
                    KeysGenerator additionalGenerator;
                    while(!additionalSetUpIsDone){
                        additionalGenerator = new KeysGenerator();
                        additionalPublicKeyString =  Base64.getEncoder().encodeToString(additionalGenerator.getPublicKey().getEncoded());
                        if(!publicKeyString.matches(additionalPublicKeyString))
                            additionalSetUpIsDone = true;
                    }
                } catch(KeysGeneratorInitializationException e){
                    Assert.fail("Cannot run test, additionalKeysGenerator could not be initialized");
                }
                if(additionalSetUpIsDone){
                    String message="Message";
                    Assert.assertNotEquals("Messages are the same", message,algorithm.decrypt(algorithm.encrypt(message,additionalPublicKeyString),privateKeyString));
                }
                else{
                    Assert.fail();
                }
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

}
