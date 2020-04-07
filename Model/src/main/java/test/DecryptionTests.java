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
 * Class for testing public methods of rsa_model package, excluding constructors, setters and getters,
 * It is checking the encrypt method from Algorithm class.
 * @author Michal Szeler
 * @version 2.0
 */
public class DecryptionTests {
     /**
     * Static field for an instance of Algorithm class
     */
    private static Algorithm algorithm;
    /**
     * Static field for an instance of Private Key string representation
     */
    private static String privateKeyString;
    /**
     * Static field that indicates whether the setUp method successfully initialized all variables
     */
    private static boolean setUpIsDone = false;


    /**
     * Method initializes essential private fields in the class
     */
    @BeforeClass
    public static void setUpClass() {
        try{
            KeysGenerator generator = new KeysGenerator();
            privateKeyString =  Base64.getEncoder().encodeToString(generator.getPrivateKey().getEncoded());
            algorithm = new Algorithm();
            setUpIsDone = true;
        } catch(KeysGeneratorInitializationException e){
            System.out.println("Cannot run tests, KeysGenerator could not be initialized");
        }
    }

    /**
     * Test checks if decrypting the message using different Public Key than the correlated one returns different text
     * In this case, message is a correctly encrypted message, but with different pair of keys. Before encryption it was: Text to encrypt
     */
    @Test
    public void decryptEncryptedMessageWithUnrelatedPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String message = "YHrsiQQFokyXmbCyhgI2JJHXTVP/2/o096HLJ0KwRAZPnLPZTsfiDAtPUln1bcog6jIeajT6XbK2p0qnsDWfll" +
                        "OkBPtr4eA0OR9zOqlkwL1nCihaOu55rlDgq3oeNjqdO0x5IunUKSwt32P8ta4LKwNXV6Cr0mssEVOrnfcp6fAoSxizMoCjo" +
                        "gdafUivEacaO+udOcxkm2FR/VKESWYW/HkMPoqawOZMkg6OcRserdVvDNIrdmP2SOK7Mr4mBmizQ30xkH3R+c8U575hlfOa" +
                        "0W14fuo7m4xNwHew9fF+XzSz4kHpR0n5jg0xYR/UZkozt6PENnheagWbVe0L5QbUrA==";
                Assert.assertNotEquals("Decryption returned correct text","Text to encrypt",algorithm.decrypt(message,privateKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if decrypting an empty message returns a message: Base64 decoder encountered an error
     * In this case, message is empty
     */
    @Test
    public void decryptEmptyMessageWithCorrectPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String message="";
                Assert.assertEquals("Incorrect message returned","Base64 decoder encountered an error\n",algorithm.decrypt(message,privateKeyString));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if decrypting the message using correlated Private Key returns correct message
     * In this case, message is a short text
     */
    @Test
    public void decryptEncryptedMessageWithRelatedPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String originalMessage="Text to decrypt";
                String encryptedMessage="r4CwC8/98lXk4WBbcUqhidFP6Bzaez3R5p/AP54Z+UnJEW6WS2vDFqFsv1oXov5CBMgXQVLzUJ4teJsGuxpU7orf" +
                        "TrfN+gm3zGfUmmf1bzUHy5df44qrICOH3LTuSTNvzrDWPg8lHeYKVPzKlfBKpbBWoJINv3T+xVMqcCRC6y2PtRYGwTKGMGN" +
                        "LMTXno49dpuFFXgnQDxHcl0kRAdOxyZQsTNd6FBksRvn2Db1Pt6LZg7CZbeTOTYSUmI8bxuVur/qQ1aF+w7c3c5TdgW5HRY" +
                        "yzSB1iBqIOnj0M7gKhhxdeix4nBWSR3hNZD+9iQkqBUu2pxTSnpgk3Lu2wD+u41w==";
                String correlatedPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDCCLAenmqHlmS8g/tGdNYKmk" +
                        "8a2xL73FmWrIi4wSrAPhbU8iKZtNVXHsvd+uZmLZgY5pMoINpF0Ci4FvDY4ZuA7nTsIVPaItQde4uFaZ8CYvhA68YiXbodI4" +
                        "vy1rHi2tn0Pq3GJLPkvs/KB+tRZx1knEwroQT/f9WTQyJ+lYddI6ySgiBBbNu1O5E7eJbIk14HgGMgcyOiqE8El3XnkSOMrl" +
                        "eknQkVAvkgltU/nTx4ZaeLeIznzx8BOVVuQqwUgpeAYgOEF9rIHGes9fdY7Lp+xBVNBfc6v53oWJpcQASy6EaGHBGTkU3Ygr" +
                        "VZyv/KZUk1jc5fZ1nbNV0ziJ8nsGAvAgMBAAECggEAYt8wd3m7yPl+vXz3ZrCUfPSiiInHg6WF1Tx3ssVjtnAtDDeSIAbAi1" +
                        "M7CRMXoktrbIzZwaHz940Zoqy1754OskVQOR7Gbqo+sgNJViM7DmlGHTbHUK237680WfrCzFk+pLHntMbxEAeXNFiStXk4xh" +
                        "LK0ZSMSnFmcFLOWfhx8Kgg0m8ODMFFeGEiK55kKwJ2pmDDOMestVramQZ9wCQjj7lpEXo7gT+Jt0YXap11YhQRgKeFO4SUQv" +
                        "UCXicduvkE/swfKPaxcnONHJ7h/kBVAKi401Oq5tTQvUqNCOZ1GruLsR5ZEO7eFr+c9lLd8E9TBb1kC/44x/X8oFW/g1Iw0Q" +
                        "KBgQDjPirh0ORHH+lcb11SZvdpj+3g5ZD31RXJWGHTlDchvv+NYUGq0lUEus9CcFLjWWY1H6GclRpfOSJYGHWHzOxBgt3C2x" +
                        "KQQl3XqRbTguluYeE5lM7x4A4p3Y/ADLgTaL+YyLRPA45UTpGyrKa6cW+07aPDJ833fHP67BuAtFh36QKBgQDalqyPjZWiMV" +
                        "quL/rrQ1jteiEoRm/JItPIGRI0UuxTqPyZiEkfqlYcwnIKaApcjxV/P4/JFtCASBGNUiv0FX44L99afk7PkbvWPYiyDRy+8m" +
                        "WKm3D6/HuBKknzSE5ZZc/rqrsnsBf0uFBzvW7PsPAYejrATcol9FUnQ9vYXn6gVwKBgDT3zK8OLeeWPKLbInmOnqTwc5Y0bH" +
                        "IYLRfdjg/vQFqRVqfxv4i9tiuAD8eAvxFXSUegs973skn321WFff/FeFxc0KwWvGD71zvpofRzU03zargtQ3+FpkeD6XnufQ" +
                        "mpJN/MCJtSTN7B4ZwwOjzDLDKUIdPfd1b0n2QhnNeHMgfhAoGAPby8tDbbAlpKMFRg3D3DSh9qagOq4vYxNpzOJXUeRmq+HW" +
                        "b+sDDd7XWF3CaSdp37Blufcr482V6Bhpakz2Wapqfuv5JZFAsnMFVk3FUTv2KbB1Dr0hepkkutY0cSyfEVjoxEG+uCeJ3uKy" +
                        "Jfv81Lor4zBclyxNGDk3l5CF14CjUCgYAtyrk8kM4ndTYP0vwtsP4vwUKvy8BYLHH9VQSS01QbR9lU6Nyado3eDWZr8ygjpU" +
                        "KWMfRwQhWRv9D9+xx6yww3uW21T4PpXOmKt+BPS2GjFH/fHTsY/26G9XGp+nbHURSjDUpFNHwHo5xLl2OVI6KphAj3h12BpY" +
                        "LtmN7VT8QdIA==";
                Assert.assertEquals("Text after decryption is not the same", originalMessage, algorithm.decrypt(encryptedMessage, correlatedPrivateKey));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
        Assert.fail();
        }
    }

    /**
     * Test checks if decrypting the message using manipulated correlated Private Key returns different message
     * In this case, message is a short text
     */
    @Test
    public void decryptEncryptedMessageWithModifiedRelatedPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String originalMessage="Text to decrypt";
                String encryptedMessage="r4CwC8/98lXk4WBbcUqhidFP6Bzaez3R5p/AP54Z+UnJEW6WS2vDFqFsv1oXov5CBMgXQVLzUJ4teJsGuxpU7orf" +
                        "TrfN+gm3zGfUmmf1bzUHy5df44qrICOH3LTuSTNvzrDWPg8lHeYKVPzKlfBKpbBWoJINv3T+xVMqcCRC6y2PtRYGwTKGMGN" +
                        "LMTXno49dpuFFXgnQDxHcl0kRAdOxyZQsTNd6FBksRvn2Db1Pt6LZg7CZbeTOTYSUmI8bxuVur/qQ1aF+w7c3c5TdgW5HRY" +
                        "yzSB1iBqIOnj0M7gKhhxdeix4nBWSR3hNZD+9iQkqBUu2pxTSnpgk3Lu2wD+u41w==";
                String manipulatedPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDCCLAenmqHlmS8g/tGdNYKmk" +
                        "8a2xL73FmWrIi4wsrAPhbU8iKZtNVXHsvd+uZmLZgY5pMoINpF0Ci4FvDY4auA7nTsIVPaItQde4uFaZ8CYvhA68YiXbodI4" +
                        "vy1rHi2tn0Pq3GJLPkvs/KB+tRZx1knEwroQT/f9WTQyJ+lYddI6ySgiBBbNu1O5E7eJbIk14HgGMgcyOiqE8El3XnkSOMrl" +
                        "eknQkVAvkgltU/nTx4ZaeLeIznzx8BOVVuQqwUgpeAYgOEF9rIHGes9fdY7Lp+xBVNBfc6v53oWJpcQASy6EaGHBGTkU3Ygr" +
                        "VZyv/KZUk1jc5fZ1nbNV0ziJ8nsGAvAgMBAAECggEAYt8wd3m7yPl+vXz3ZrCUfPSiiInHg6WF1Tx3ssVjtnAtDDeSIAbAi1" +
                        "M7CRMXoktrbIzZwaHz920Zoqy1754OskVQOR7Gbqo+sgNJViM7DmlGHTbHUK237680WfrCzFk+pLHntMbxEAeXNFiStXk4xh" +
                        "LK0ZSMSnFmcFLOWfhx8Kgg0m8ODMFFeGEiK55kKwJ2pmDDOMestVramQZ9wCQjj7lpEXo7gT+Jt0YXap11YhQRgKeFO4SUQv" +
                        "UCXicduvkE/swfKPaxcnONHJ7h/kBVAKi401Oq5tTQvUqNCOZ1GruLsR5ZEO7eFr+c9lLd8E9TBb1kC/44x/X8oFW/g1Iw0Q" +
                        "KBgQDjPirh0ORHH+lcb11SZvdpj+3g5ZD31RXJWGHTlDchvv+NYUGq0lUEus9CcFLjWWY1H6GclRpfOSJYGHWHzOxBgt3C2x" +
                        "KQQl3XqRbTguluYeE5lM7x4A4p3Y/ADLgTaL+YyLRPA45UTpGyrKa6cW+07aPDJ833fHP67BuAtFh36QKBgQDalqyPjZWiMV" +
                        "quL/rrQ1jteiEoRm/JItPIGRI0UuxTqPyZiEkfqlYcwnIKaApcjxV/P4/JFtCASBGNUiv0FX44L99afk7PkbvWPYiyDRy+8m" +
                        "WKm3D6/HuBKknzSE5ZZc/rqrsnsBf0uFBzvW7PsPAYejrATcol9FUnQ9vYXn6gVwKBgDT3zK8OLeeWPKLbInmOnqTwc5Y0bH" +
                        "IYLRfdjg/vQFqRVqfxv4i9tiuAD4eAvxFXSUegs973skn321WFff/FeFxc0KwWvGD71zvpofRzU03zargtQ3+FpkeD6XnufQ" +
                        "mpJN/MCJtSTN7B4ZwwOjzDLDKUIdPfd1b0n2QhnNeHMgfhAoGAPby8tDbbAlpKMFRg3D3DSh9qagOq4vYxNpzOJXUeRmq+HW" +
                        "b+sDDd7XWF3CaSdp37Blufcr482V6Bhpakz2Wapqfuv5JZFAsnMFVk3FUTv2KbB1Dr0hepkkutY0cSyfEVjoxEG+uCeJ3uKy" +
                        "Jfv81Lor4zBclyxNGDk3l5CF14CjUCgYAtyrk8kM4ndTYP0vwtsP4vwUKvy8BYLHH9VQSS01QbR9lU6Nyado3eDWZr8ygjpU" +
                        "KWMfRwQhWRv9D9+xx6yww3uW21T4PpXOmKt+BPS2GjFH/fHTsY/26G9XGp+nbHURSjDUpFNHwHo5xLl2OVI6KphAj3h12BpY" +
                        "LtmN7VT8QdIA==";
                Assert.assertNotEquals("Text after decryption is the same", originalMessage, algorithm.decrypt(encryptedMessage, manipulatedPrivateKey));
            } catch (AlgorithmException e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }

    /**
     * Test checks if decrypting the message using incorrectly formatted Private Key threw an error
     * In this case, message is a short text
     */
    @Test
    public void decryptEncryptedMessageWithBadlyFormattedPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String encryptedMessage="r4CwC8/98lXk4WBbcUqhidFP6Bzaez3R5p/AP54Z+UnJEW6WS2vDFqFsv1oXov5CBMgXQVLzUJ4teJsGuxpU7orf" +
                        "TrfN+gm3zGfUmmf1bzUHy5df44qrICOH3LTuSTNvzrDWPg8lHeYKVPzKlfBKpbBWoJINv3T+xVMqcCRC6y2PtRYGwTKGMGN" +
                        "LMTXno49dpuFFXgnQDxHcl0kRAdOxyZQsTNd6FBksRvn2Db1Pt6LZg7CZbeTOTYSUmI8bxuVur/qQ1aF+w7c3c5TdgW5HRY" +
                        "yzSB1iBqIOnj0M7gKhhxdeix4nBWSR3hNZD+9iQkqBUu2pxTSnpgk3Lu2wD+u41w==";
                String correlatedPrivateKey = "fadsfasdgetjhrtswaerfadfhgfgjse3257356234sdfa6qrasr";
                algorithm.decrypt(encryptedMessage, correlatedPrivateKey);
                Assert.fail("Algorithm error was not thrown");
            } catch (AlgorithmException e) {
                Assert.assertEquals("PKCS #8 key specification is invalid",e.getMessage());
            }
        }
        else {
            Assert.fail();
        }
    }

        /**
     * Test checks if decryption threw an exception after receiving null message
     */
    @Test
    public void decryptNullMessageWithProperPublicKeyTest(){
        if(setUpIsDone) {
            try {
                algorithm.decrypt(null, privateKeyString);
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
    public void decryptEncryptedMessageWithNullPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String encryptedMessage="r4CwC8/98lXk4WBbcUqhidFP6Bzaez3R5p/AP54Z+UnJEW6WS2vDFqFsv1oXov5CBMgXQVLzUJ4teJsGuxpU7orf" +
                        "TrfN+gm3zGfUmmf1bzUHy5df44qrICOH3LTuSTNvzrDWPg8lHeYKVPzKlfBKpbBWoJINv3T+xVMqcCRC6y2PtRYGwTKGMGN" +
                        "LMTXno49dpuFFXgnQDxHcl0kRAdOxyZQsTNd6FBksRvn2Db1Pt6LZg7CZbeTOTYSUmI8bxuVur/qQ1aF+w7c3c5TdgW5HRY" +
                        "yzSB1iBqIOnj0M7gKhhxdeix4nBWSR3hNZD+9iQkqBUu2pxTSnpgk3Lu2wD+u41w==";
                algorithm.decrypt(encryptedMessage, null);
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
     * Test checks if decryption method threw an exception after receiving empty Private Key
     */
    @Test
    public void decryptEncryptedMessageWithEmptyPublicKeyTest(){
        if(setUpIsDone) {
            try {
                String encryptedMessage="r4CwC8/98lXk4WBbcUqhidFP6Bzaez3R5p/AP54Z+UnJEW6WS2vDFqFsv1oXov5CBMgXQVLzUJ4teJsGuxpU7orf" +
                        "TrfN+gm3zGfUmmf1bzUHy5df44qrICOH3LTuSTNvzrDWPg8lHeYKVPzKlfBKpbBWoJINv3T+xVMqcCRC6y2PtRYGwTKGMGN" +
                        "LMTXno49dpuFFXgnQDxHcl0kRAdOxyZQsTNd6FBksRvn2Db1Pt6LZg7CZbeTOTYSUmI8bxuVur/qQ1aF+w7c3c5TdgW5HRY" +
                        "yzSB1iBqIOnj0M7gKhhxdeix4nBWSR3hNZD+9iQkqBUu2pxTSnpgk3Lu2wD+u41w==";
                algorithm.decrypt(encryptedMessage, "");
                Assert.fail("Algorithm error was not thrown");
            } catch (AlgorithmException e) {
                Assert.assertEquals("PKCS #8 key specification is invalid",e.getMessage());
            }
        }
        else {
            Assert.fail();
        }
    }
}
