import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    private Socket socket;

    public Client(){
        try {
            this.socket = new Socket("localhost", 5000);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());

            Scanner scan = new Scanner(System.in);
            System.out.println("Enter message");
            String body = scan.nextLine();
            
            Message message = new Message(false, "localUser", body);
            objectOutputStream.writeObject(message);
            objectOutputStream.close();
            this.socket.close();

        } catch (Exception e) {
            
        }
    }

    public static void main(String[] args) {
        new Client();
    }

}
