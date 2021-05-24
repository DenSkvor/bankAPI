package web;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static source.Constant.*;
import static util.UtilMethods.*;

public class Server {

    private static Server instance;
    public synchronized static Server getInstance(){
        if (instance == null) instance = new Server();
        return instance;
    }
    private Server(){}

    public void startServer(){
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
            createContext(server, ROOT_PATH, readAllProperties(API_PROPERTIES_PATH));
            server.setExecutor(null);
            server.start();
            System.out.println("Server start on port: " + SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
