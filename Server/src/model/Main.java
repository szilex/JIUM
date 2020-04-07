package model;

import server.Server;
import java.io.IOException;

/**
 * Class contains an entry point for the application
 * @author Michal Szeler
 * @version 3.0
 */
public class Main {
    /**
     * Method that is an entry point for the application. In creates an instance of Server class that handles the connection with client
     * @param args String array that contains parameters passed from command prompt
     */
    public static void main(String[] args){
        String propertiesName = "server.properties";
        try{
            Server server = new Server(propertiesName);
        } catch(IOException e){
            System.err.println("Couldn't start the server");
            System.exit(5);
        }

    }
}
