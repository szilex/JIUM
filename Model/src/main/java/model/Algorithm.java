package main.java.model;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * Class allows to encrypt and decrypt data by using instances of Cipher class.
 * To convert Public and Private key from string to binary representation (and vice versa), text is encoded and decoded using Base64 encoding.
 * Because of the padding, 2048 bit keys size and RSA algorithm nature, maximum length of unprocessed text can be 190 characters.
 * Any message longer than that will be automatically split into 190 character blocks if "Encrypt" radio button is selected when pressing "Process" button.
 * When "Decrypt" radio button is selected, each encrypted message will be longer than before mentioned limit, so it is split dynamically.
 * @author Michal Szeler
 * @version 5.0
 */
public class Algorithm {

    /**
     * Constructor with no parameters, no fields are initialized
     */
    public Algorithm() {}

    /**
     * Method converts RSA Public Key from String to PublicKey format using X.509 encoding and instance of KeyFactory class
     * @param base64PublicKey String encoded in Base64 format which represents the value of RSA Public Key
     * @return An instance of PublicKey class which was created from base64PublicKey parameter
     * @throws AlgorithmException General exception thrown by method, possible original exceptions and messages:
     *      * NoSuchAlgorithmException - KeyFactory was unable to get an instance of RSA algorithm
     *      * InvalidKeySpecException - base64PublicKey specification is incorrect
     */
    @Details(encoding = Details.Encoding.X509,operation = Details.Operation.ConvertStringToPublicKey,description = "Method for creating an instance of Public Key", author = "Michal Szeler")
    private PublicKey getPublicKey(String base64PublicKey) throws AlgorithmException {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e){
            throw new AlgorithmException("X.509 key specification is invalid");
        } catch(NoSuchAlgorithmException e){
            throw new AlgorithmException("Algorithm couldn't find an instance of RSA KeyFactory");
        } catch (IllegalArgumentException e){
            throw new AlgorithmException("Public Key is incorrectly formatted");
        }
    }

    /**
     * Method converts RSA Private Key from String to PublicKey format using PKCS #8 encoding and instance of KeyFactory class
     * @param base64PrivateKey String encoded in Base64 format which represents the value of RSA Private Key
     * @return An instance of PrivateKey class which was created from base64PrivateKey parameter
     * @throws AlgorithmException General exception thrown by method, possible original exceptions and messages:
     *      * InvalidKeySpecException - base64PrivateKey specification is incorrect
     *      * NoSuchAlgorithmException - KeyFactory was unable to get an instance of RSA algorithm
     */
    @Details(encoding = Details.Encoding.PKCS8,operation = Details.Operation.ConvertStringToPrivateKey, description = "Method for creating an instance of Private Key", author = "Michal Szeler")
    private PrivateKey getPrivateKey(String base64PrivateKey) throws AlgorithmException {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e){
            throw new AlgorithmException("PKCS #8 key specification is invalid");
        } catch(NoSuchAlgorithmException e){
            throw new AlgorithmException("Algorithm couldn't find an instance of RSA KeyFactory");
        } catch (IllegalArgumentException e){
            throw new AlgorithmException("Private Key is incorrectly formatted");
        }
    }

    /**
     * Method encrypts data by using an instance of Cipher class, OAEP with SHA-256 and MGF1 padding and Base64 encoding
     * LinkedList is used to store parts of data, if it is too long
     * @param data Text that contains message to encrypt, if it is longer than 190 characters (because of used padding), it is split into blocks
     * @param publicKey Text that contains RSA Public Key (encoded in Base64 format by default)
     * @return Text that contains an encrypted message
     * @throws AlgorithmException General exception thrown by method, possible original exceptions and messages:
     *      * BadPaddingException - Data is padded inappropriately, eg. with different padding
     *      * NoSuchPaddingException - Algorithm couldn't find appropriate padding
     *      * IllegalBlockSizeException - Data parameter is too long
     *      * InvalidKeyException - publicKey parameter is incorrect
     *      * NoSuchAlgorithmException - Algorithm couldn't get an instance of RSA algorithm
     */

    @Details(encoding = Details.Encoding.Base64,operation = Details.Operation.Encrypt, description = "Method for encrypting data using passed Base64 encoded Public Key", author = "Michal Szeler")
    public final String encrypt(String data, String publicKey) throws AlgorithmException {
        try {
            if(data==null||publicKey==null)
                throw new AlgorithmException("Argument value was null");
            List<byte[]> textList = new LinkedList<>();
            if(data.getBytes().length>190){
                int size = data.length();
                for(int i=0;i*190<size;i++){
                    if((i+1)*190<size)
                        textList.add(data.substring(i * 190, (i + 1) * 190).getBytes());
                    else
                        textList.add(data.substring(i*190).getBytes());
                }
            }
            else
                textList.add(data.getBytes());

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKey(publicKey));
            List<byte[]> resultList = new LinkedList<>();

            for(byte[] elem : textList)
                resultList.add(cipher.doFinal(elem));

            StringBuilder resultText = new StringBuilder();
            for(byte[] elem : resultList)
                resultText.append(Base64.getEncoder().encodeToString(elem));

            return resultText.toString();

        } catch (BadPaddingException e) {
            throw new AlgorithmException("Padding used in text is different then one used in the algorithm");
        } catch (NoSuchPaddingException e) {
            throw new AlgorithmException("Algorithm couldn't find appropriate padding");
        } catch (IllegalBlockSizeException e) {
            throw new AlgorithmException("Inserted text is too long");
        } catch (InvalidKeyException e) {
            throw new AlgorithmException("Incorrect public key");
        } catch (NoSuchAlgorithmException e) {
            throw new AlgorithmException("Program couldn't find appropriate algorithm");
        }
    }

    /**
     * Method decrypts data by using an instance of Cipher class, OAEP with SHA-256 and MGF1 padding and Base64 decoding
     * LinkedList is used to store parts of data, if it is too long
     * @param data Text that contains message to decrypt
     * @param privateKey Text that contains RSA Public Key (encoded in Base64 format by default)
     * @return Text that contains a decrypted message
     * @throws AlgorithmException General exception thrown by method, possible original exceptions and messages:
     *      * BadPaddingException - Data is padded inappropriately, eg. with different padding
     *      * NoSuchPaddingException - Algorithm couldn't find appropriate padding
     *      * IllegalBlockSizeException - Data parameter is too long
     *      * InvalidKeyException - publicKey parameter is incorrect
     *      * NoSuchAlgorithmException - Algorithm couldn't get an instance of RSA algorithm
     */
    @Details(encoding = Details.Encoding.Base64,operation = Details.Operation.Decrypt, description = "Method for decrypting data using passed Base64 encoded Private Key", author = "Michal Szeler")
    public final String decrypt(String data, String privateKey) throws AlgorithmException {
        try {

            if(data==null||privateKey==null)
                throw new AlgorithmException("Argument value was null");

            String[] encryptedList = data.split("==");
            List<byte[]> decodedList = new LinkedList<>();
            byte[] errorByte = new byte[]{(byte)0b00000000};
            for(String elem : encryptedList){
                elem+="==";
                try{
                    decodedList.add(Base64.getDecoder().decode(elem));
                } catch (IllegalArgumentException e){
                    decodedList.add(errorByte);
                }
            }

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey(privateKey));
            StringBuilder resultText = new StringBuilder();

            for(byte[] elem : decodedList){
                try{
                    if(elem!=errorByte)
                        resultText.append(new String(cipher.doFinal(elem)));
                    else
                        resultText.append("Base64 decoder encountered an error\n");
                } catch (BadPaddingException e) {
                    resultText.append("\nPadding used in text is different then one used in the algorithm\n");
                } catch (IllegalBlockSizeException e) {
                    resultText.append("\nInserted text is too long\n");
                }
            }
            return resultText.toString();

        } catch (NoSuchPaddingException e) {
            throw new AlgorithmException("Algorithm couldn't find appropriate padding");
        } catch (InvalidKeyException e) {
            throw new AlgorithmException("Incorrect public key");
        } catch (NoSuchAlgorithmException e) {
            throw new AlgorithmException("Program couldn't find appropriate algorithm");
        }
    }
}