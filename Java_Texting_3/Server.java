import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{

    public static List<ClientHandler> clientHandlerList = new ArrayList<>();
    private static int numberOfClients = 0;

    public Server(int port) throws IOException{

        if(port < 0 || port > 65535){
            throw new IllegalArgumentException("Invalid port number");
        }

        //server socket on local host at given port number
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket;

        while(!serverSocket.isClosed()){
            System.out.println("Waiting for client connection");
            clientSocket = serverSocket.accept();

            System.out.println("New client connected");

            //Client handler for current client
            ClientHandler client = new ClientHandler(clientSocket);
            clientHandlerList.add(client);

            Thread thread = new Thread(client);
            thread.start();

            numberOfClients++;
        }

    }

    public static void main(String args[]){
        try {
            new Server(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}