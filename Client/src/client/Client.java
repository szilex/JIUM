package client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Class is responsible for connecting with server and correctly maintain the connection
 * It implements Closeable interface to handle Socket closing correctly
 * @author Michal Szeler
 * @version 3.0
 */
public class Client implements Closeable {
    /**
     * Field that contains port through which client and server communicate
     */
    private int port;
    /**
     * Field that contains server address
     */
    private String serverAddress;
    /**
     * Field that contains an instance of Socket class to communicate with server
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
     * Constructor for Client class, which initializes all important fields
     * @param propertiesFileName Name of file that contains port and server address
     * @throws IOException Exception thrown if connection to server cannot be established
     */
    public Client(String propertiesFileName) throws IOException{
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(propertiesFileName);
        properties.load(in);
        port = Integer.parseInt(properties.getProperty("port"));
        serverAddress = properties.getProperty("server_address");
        socket = new Socket(serverAddress, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);
 }

    /**
     * Method that sends to server message and Public Key to encrypt it and returns received encrypted message
     * @param message String that contains message to encrypt
     * @param publicKey String that contains Public Key
     * @return Encrypted text or message that contains explanation for exception thrown on server
     */
    public String encryptMessage(String message, String publicKey){
        try{
            printWriter.println("Encrypt");
            String controlMessage=bufferedReader.readLine();
            if(controlMessage.contains("100")){
                printWriter.println(message.length());
                controlMessage=bufferedReader.readLine();
                if(controlMessage.contains("101")){
                    char[] buffer = message.toCharArray();
                    printWriter.print(buffer);
                    printWriter.flush();
                    controlMessage=bufferedReader.readLine();
                    if(controlMessage.contains("102")){
                        printWriter.println(publicKey.length());
                        controlMessage=bufferedReader.readLine();
                        if(controlMessage.contains("103")){
                            buffer = publicKey.toCharArray();
                            printWriter.print(buffer);
                            printWriter.flush();
                            controlMessage=bufferedReader.readLine();
                            if(controlMessage.contains("104")){
                                return bufferedReader.readLine();
                            }
                        }
                    }
                }
            } if(controlMessage.contains("200")){
                return "Connection between client and server wasn't executed properly";
            } else{
                return controlMessage;
            }

        } catch (IOException e){
            return e.getMessage();
        }
    }

    /**
     * Method that sends to server message and Private Key to decrypt it and returns received decrypted message
     * @param message String that contains message to decrypt
     * @param privateKey String that contains Private Key
     * @return Decrypted text or message that contains explanation for exception thrown on server
     */
    public String decryptMessage(String message, String privateKey){
        try{
            printWriter.println("Decrypt");
            String controlMessage=bufferedReader.readLine();
            if(controlMessage.contains("110")){
                printWriter.println(message.length());
                controlMessage=bufferedReader.readLine();
                if(controlMessage.contains("111")){
                    char[] buffer = message.toCharArray();
                    printWriter.print(buffer);
                    printWriter.flush();
                    controlMessage=bufferedReader.readLine();
                    if(controlMessage.contains("112")){
                        printWriter.println(privateKey.length());
                        controlMessage=bufferedReader.readLine();
                        if(controlMessage.contains("113")){
                            buffer = privateKey.toCharArray();
                            printWriter.print(buffer);
                            printWriter.flush();
                            controlMessage=bufferedReader.readLine();
                            if(controlMessage.contains("114")){
                                int messageLength = Integer.parseInt(bufferedReader.readLine());
                                buffer = new char[messageLength];
                                bufferedReader.read(buffer,0,messageLength);
                                return new String(buffer);
                            }
                        }
                    }
                }
            }
            if(controlMessage.contains("200")){
                return "Connection between client and server wasn't executed properly";
            } else{
                return controlMessage;
            }
        } catch (IOException e){
            return e.getMessage();
        }
    }

    /**
     * Methods that request from server to generate and send a pair of Keys
     * @return Pair of Keys - Private Key at position 0 and Public Key at position 1
     * @throws IOException Exception thrown if process wasn't performed correctly
     */
    public String[] getKeyPair() throws IOException {
        printWriter.println("Generate_keys");
        String controlMessage=bufferedReader.readLine();
        if(controlMessage.contains("120")){
            String[] keyPair=new String[2];
            keyPair[0]=bufferedReader.readLine();
            keyPair[1]=bufferedReader.readLine();
            return keyPair;
        } else if(controlMessage.contains("121")){
            throw new IOException("Keys generator is not initialized");
        } else if(controlMessage.contains("200")){
            throw new IOException("Unknown command passed to server");
        } else
            throw new IOException("Unexpected response from server");


    }

    /**
     * Methods returns list of available commands on server
     * @return List of available methods
     * @throws IOException Exception thrown if process wasn't performed correctly
     */
    public List<String> getAvailableCommands() throws IOException{
        printWriter.println("Help");
        String controlMessage=bufferedReader.readLine();
        if(controlMessage.contains("150")){
            controlMessage = bufferedReader.readLine();
            return Arrays.asList(controlMessage.split("|"));
        }else{
            throw new IOException("Unexpected response from server");
        }
    }

    /**
     * Method ensures that Socket is correctly closed
     * @throws IOException Exception thrown if closing the Socket wasn't performed correctly
     */
    @Override
    public void close() throws IOException {
        if(socket!=null){
            socket.close();
        }
    }
}
