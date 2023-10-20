import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(65535);
        Server server = new Server(serverSocket);
        server.startServer();
    }

    public void startServer(){
        try {
        
            while(!this.serverSocket.isClosed()){
                Socket socket = this.serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (Exception e) {
            
        }
    }

    public void closeServerSocket(){
        try {
            if(this.serverSocket != null){
                this.serverSocket.close();
            }
        } catch (Exception e) {
            
        }
    }

}
