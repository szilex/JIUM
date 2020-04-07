package server;

import model.Algorithm;
import model.AlgorithmException;
import model.KeysGenerator;
import model.KeysGeneratorInitializationException;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

/**
 * Class is responsible for connecting with client, correctly maintaining the connection and responding to client's requests
 * It extends Thread class so that each client is supported on different thread
 * It implements Closeable interface to handle SocketServer closing correctly
 * @author Michal Szeler
 * @version 3.0
 */
public class SingleServerConnection extends Thread implements Closeable {
    /**
     * An instance of Algorithm class that allows to encrypt and decrypt messages
     */
    private static Algorithm algorithm;
    /**
     * An instance of KeysGenerator class that allows to get a pair of Keys
     */
    private static KeysGenerator keysGenerator;
    /**
     * Field that contains an instance of Socket class to communicate with client
     */
    private Socket socket;
    /**
     * Field that contains an instance of BufferedReader class to read from server
     */
    private BufferedReader bufferedReader;
    /**
     * Field that contains an instance of PrintWriter class to write to server
     */
    private PrintWriter printWriter;

    /**
     * Constructor for SingleServerConnection class, which initializes Socket and Algorithm fields
     * @param socket Socket through with server will communicate with client
     */
    SingleServerConnection(Socket socket){
        this.socket=socket;
        if(algorithm==null)
            algorithm = new Algorithm();
    }

    /**
     * Method from Thread class that controls data received from client and invokes appropriate methods
     */
    @Override
    public final void run() {
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);

            while(true) {
                String message = bufferedReader.readLine();
                switch (message){
                    case "Encrypt": {
                        encryptAndSendMessage();
                        break;
                    }
                    case "Decrypt": {
                        decryptAndSendMessage();
                        break;
                    }
                    case "Generate_keys":{
                        sendKeyPair();
                        break;
                    }
                    case "Help":{
                        sendAvailableCommands();
                        break;
                    }
                    default:{
                        printWriter.println("200: Unknown command");
                    }
                }
            }

        } catch (IOException e){
            System.err.println("SingleServerConnection exception: "+e.getMessage());
        }
    }

    /**
     * Method acquire message and Public Key from client, encrypts the message and sends the result back to client
     */
    private final void encryptAndSendMessage(){
        try{
            printWriter.println("100: encrypting data");

            int messageLength=Integer.parseInt(bufferedReader.readLine());
            printWriter.println("101: message length received");

            char[] buffer=new char[messageLength];
            bufferedReader.read(buffer,0,messageLength);
            String message = new String(buffer);
            printWriter.println("102: message to encrypt received");

            int publicKeyLength = Integer.parseInt(bufferedReader.readLine());
            printWriter.println("103: public key length received");

            buffer=new char[publicKeyLength];
            bufferedReader.read(buffer,0,publicKeyLength);
            printWriter.println("104: public key received");
            String publicKey = new String(buffer);

            try {
                printWriter.println(algorithm.encrypt(message, publicKey));
            } catch (AlgorithmException e){
                printWriter.println("500: "+e.getMessage());
            }

        } catch (IOException e){
            printWriter.println("400: IOException");
        } catch (NumberFormatException e){
            printWriter.println("600: incorrect numeric value");
        }

    }

    /**
     * Method acquire message and Private Key from client, decrypts the message and sends the result back to client
     */
    private final void decryptAndSendMessage() {
        try{
            printWriter.println("110: decrypting data");

            int messageLength=Integer.parseInt(bufferedReader.readLine());
            printWriter.println("111: message length received");

            char[] buffer=new char[messageLength];
            bufferedReader.read(buffer,0,messageLength);
            String message = new String(buffer);
            printWriter.println("112: message to decrypt received");

            int privateKeyLength = Integer.parseInt(bufferedReader.readLine());
            printWriter.println("113: private key length received");

            buffer=new char[privateKeyLength];
            bufferedReader.read(buffer,0,privateKeyLength);
            printWriter.println("114: private key received");
            String publicKey = new String(buffer);

            try {
                String decryptedMessage=algorithm.decrypt(message, publicKey);
                printWriter.println(decryptedMessage.length());
                printWriter.println(decryptedMessage);
            } catch (AlgorithmException e){
                printWriter.println("500: " + e.getMessage());
            }

        } catch (IOException e){
            printWriter.println("400: IOException");
        }
    }

    /**
     * Method sends to client a pair of Keys
     */
    private final void sendKeyPair(){
        if(keysGenerator==null){
            try{
                keysGenerator = new KeysGenerator();
                printWriter.println("120: key generator is initialized");
            } catch(KeysGeneratorInitializationException e){
                printWriter.println("121: key generator is not initialized");
            }
        }
        else
            printWriter.println("120: key generator is initialized");

        if(keysGenerator!=null){
            String privateKey = Base64.getEncoder().encodeToString(keysGenerator.getPrivateKey().getEncoded());
            String publicKey = Base64.getEncoder().encodeToString(keysGenerator.getPublicKey().getEncoded());
            printWriter.println(privateKey);
            printWriter.println(publicKey);
        }
    }

    /**
     * Method sends to client message containing all available commands
     */
    private final void sendAvailableCommands(){
        printWriter.println("150: sending list of commands");
        printWriter.println("Encrypt|Decrypt|Generate_keys|Help");
    }

    /**
     * Method ensures that Socket is correctly closed
     * @throws IOException Exception thrown if closing the Socket wasn't performed correctly
     */
    @Override
    public final void close() throws IOException {
        if(socket!=null){
            socket.close();
        }
    }
}
