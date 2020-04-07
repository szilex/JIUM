package server;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

/**
 * Class is responsible for creating instances of SingleServerConnection classes
 * It implements Closeable interface to handle SocketServer closing correctly
 * @author Michal Szeler
 * @version 3.0
 */
public class Server implements Closeable {
    /**
     * Field that contains port through which client and server communicate
     */
    private int port;
    /**
     * Field that contains an instance of ServerSocket class to communicate with client
     */
    private ServerSocket serverSocket;

    /**
     * Constructor for Server class, which establishes all required parameters
     * @param propertiesFileName Name of file that contains port
     * @throws IOException Exception thrown if initialization wasn't performed correctly
     */
    public Server(String propertiesFileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(propertiesFileName);
        properties.load(in);
        port=Integer.parseInt(properties.getProperty("port"));
        serverSocket = new ServerSocket(port);
        while(true)
            new SingleServerConnection(serverSocket.accept()).start();
    }

    /**
     * Method ensures that ServerSocket is correctly closed
     * @throws IOException Exception thrown if closing the ServerSocket wasn't performed correctly
     */
    @Override
    public final void close() throws IOException {
        if(serverSocket!=null)
            serverSocket.close();
    }
}
