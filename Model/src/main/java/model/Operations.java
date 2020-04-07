package main.java.model;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.Serializable;

/**
 * Class contains methods that are distributed as through WebService
 * @author Michal Szeler
 * @version 6.0
 */
@WebService()
public class Operations implements Serializable {

  /**
   * Field contains an instance of Algorithm class
   */
  Algorithm algorithm = new Algorithm();

  /**
   * Method representing WebMethod for encrypting data
   * @param data Text representing message to encrypt
   * @param publicKey Text representing RSA public key
   * @return Encrypted message
   */
  @WebMethod
  public String encrypt(String data, String publicKey) {
    String result;
    try{
      result=algorithm.encrypt(data, publicKey);
    }
    catch(AlgorithmException e){
      result=e.getMessage();
    }
    return result;
  }

  /**
   * Method representing WebMethod for decrypting data
   * @param data Text representing encrypted message to decrypt
   * @param privateKey Text representing RSA private key
   * @return Decrypted message
   */
  @WebMethod
  public String decrypt(String data, String privateKey) {
    String result;
    try{
      result=algorithm.decrypt(data, privateKey);
    }
    catch(AlgorithmException e){
      result=e.getMessage();
    }
    return result;
  }

  /**
   * Method represents entry point for the project
   * @param argv
   */
   public static void main (String[]argv){
      Object implementor = new Operations();
      String address = "http://localhost:9000/operations";
      Endpoint.publish(address, implementor);
    }
  }
