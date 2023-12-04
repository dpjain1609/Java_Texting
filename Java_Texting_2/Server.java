import java.net.ServerSocket;
import java.net.Socket;

/*
 * Server class for Group chat
 * Starts a server socket on a specified port
 * Accepts any client that is trying to connect and creates a handler and starts it on a separate thread
 */
public class Server{
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
    
    private ServerSocket serverSocket;
    
    public Server(){
        try {
            this.serverSocket = new ServerSocket(5000);
        } catch (Exception e) {
            
        }
    }

    /*
     * Function to start the server 
     * Accept clients who are trying to connect 
     * Launch a new handler on a new thread for each client connected
     */
    public void startServer(){
        while(!this.serverSocket.isClosed()){

            try {
                System.out.println("Waiting for new client");
                Socket socket = this.serverSocket.accept();
                System.out.println("New client connected");
                Handler handler = new Handler(socket);

                Thread thread = new Thread(handler);
                thread.start();

            } catch (Exception e) {
                closeServer();
                e.printStackTrace();
            }
        }
    }

    /*
     * Function to close the server
     */
    public void closeServer(){
        try {
            if(this.serverSocket != null){
                this.serverSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}