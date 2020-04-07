package model.rsa;

import java.security.*;

/**
 * Class uses KeyPairGenerator class to generate pair of keys (private and public) required in RSA encryption
 * @author Michal Szeler
 * @version 2.0
 */
public class KeysGenerator{
    /**
     * Field contains a pair of RSA Keys that were generated in constructor
     */
    private KeyPair keyPair;

    /**
     * Constructor for RSAKeysGenerator, which uses an instance of KeyPairGenerator class to create RSA Key Pair
     * @throws KeysGeneratorInitializationException An error occurred during initialization of KeyPairGenerator
     */
    public KeysGenerator() throws KeysGeneratorInitializationException {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            generator.initialize(2048, random);
            keyPair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e){
            throw new KeysGeneratorInitializationException("Couldn't get an instance of RSA KeyPairGenerator");
        } catch (NoSuchProviderException e){
            throw new KeysGeneratorInitializationException("Couldn't retrieve algorithm from provider");
        }
    }

    /**
     * Method allows to get an instance of PublicKey class previously generated in constructor, if constructor doesn't throw an exception, then method can't return null
     * @return An instance of PublicKey class, which represents RSA public key, in X.509 format, encoded with Base64 encoding
     */
    public final PublicKey getPublicKey(){
        return keyPair.getPublic();
    }


    /**
     * Method allows to get an instance of PrivateKey class previously generated in constructor, if constructor doesn't throw an exception, then method can't return null
     * @return An instance of PrivateKey class, which represents RSA private key, in PKCS #8 format, encoded with Base64 encoding
     */
    public final PrivateKey getPrivateKey(){
        return keyPair.getPrivate();
    }
}
