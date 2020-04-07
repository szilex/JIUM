
package main.java.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.7-b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Operations", targetNamespace = "http://model/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Operations {


    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "encrypt", targetNamespace = "http://model/", className = "main.java.client.Encrypt")
    @ResponseWrapper(localName = "encryptResponse", targetNamespace = "http://model/", className = "main.java.client.EncryptResponse")
    @Action(input = "http://model/Operations/encryptRequest", output = "http://model/Operations/encryptResponse")
    public String encrypt(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "decrypt", targetNamespace = "http://model/", className = "main.java.client.Decrypt")
    @ResponseWrapper(localName = "decryptResponse", targetNamespace = "http://model/", className = "main.java.client.DecryptResponse")
    @Action(input = "http://model/Operations/decryptRequest", output = "http://model/Operations/decryptResponse")
    public String decrypt(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

}
