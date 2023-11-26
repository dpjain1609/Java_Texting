import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private String username;
    private final int port;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Client(String username, int port)  throws IOException{
        if(port < 0 || port > 65535){
            throw new IllegalArgumentException("Invalid port number");
        }

        this.username = username;
        this.port = port;
        this.socket = new Socket("localhost", port);

        this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());

        //separate thread to send messages
        Thread sendThread = new Thread(new Runnable() {
            
            @Override
            public void run() {

                while (!socket.isClosed()) {
                    Scanner scan = new Scanner(System.in);
                    System.out.println("Enter receivers separated by spaces");
                    String receivers = scan.nextLine();
                    String receiverArray[] = receivers.split(" ");
                    List<String> receiverList = new ArrayList<>();
    
                    for(String str : receiverArray){
                        receiverList.add(str);
                    }
                    
                    System.out.println("Enter message");
                    String message = scan.nextLine();
                    //encrypt message
                    Message messageToServer = new Message(false, username, receiverList, message);
                    
                    try {
                        outputStream.writeObject(messageToServer); 
                    } catch (Exception e) {
    
                    }
                }

            }

        });

        //separate thread to receive messages
        Thread receiveThread = new Thread(new Runnable() {
           @Override
           public void run() {
                while(!socket.isClosed()){

                    try{
                        Message message = (Message)inputStream.readObject();
                        System.out.println(message.getSender() + ": " + message.getMessageBody());
                    } catch(Exception e){

                    }
                }
           } 
        });

        sendThread.start();
        receiveThread.start();
    }

    public static void main(String[] args) {
        try {
            new Client("d", 5000);  
        } catch (Exception e) {
            // TODO: handle exception
        }   
    }
    
}
