import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    private ServerSocket serverSocket;

    public Server(){
        try {
            this.serverSocket = new ServerSocket(5000);
            Socket socket = this.serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) objectInputStream.readObject();
            if(message != null){
                System.out.println(message.getSender() + ": " +message.getMessageBody());
            }

            objectInputStream.close();
            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            
        }
    }

    public static void main(String[] args) {
        new Server();
    }

}