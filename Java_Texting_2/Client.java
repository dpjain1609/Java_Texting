import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String username;

    public Client(String username){
        this.username = username;

        try {
            this.socket = new Socket("localhost", 5000);
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            Message usernameObject = new Message(false, this.username, ";");
            this.objectOutputStream.writeObject(usernameObject); 

        } catch (Exception e) {
            //removeClient();
            e.printStackTrace();
            System.out.println("Exception in Client()");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username for the group chat: ");
        String username = scanner.nextLine();
        Client client = new Client(username);
        client.listenForMessage();
        client.sendMessage();
        scanner.close();
    }

    public void sendMessage(){
        Scanner scanner = new Scanner(System.in);
        
        try {
            while(socket.isConnected()){

                String messageToSend = scanner.nextLine();
                
                if(messageToSend.equalsIgnoreCase("quit")){
                    Message messageToClient = new Message(true, this.username, messageToSend);
                    objectOutputStream.writeObject(messageToClient);
                    break;
                }
                
                Message messageToClient = new Message(false, this.username, messageToSend);
                objectOutputStream.writeObject(messageToClient);
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            removeClient();
            e.printStackTrace();
            System.out.println("Exception in sendMessage()");
        }

        scanner.close();
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message messageFromGroupChat;

                while(socket.isConnected()){
                    try {
                        messageFromGroupChat = (Message)objectInputStream.readObject();
                        int shift = messageFromGroupChat.getShiftValue();
                        String textMessage = decrypt(messageFromGroupChat.getMessageBody(), shift);
                        System.out.println(messageFromGroupChat.getSender() + ": " + textMessage);
                    } catch (Exception e) {
                        removeClient();
                        break;
                   }
                }
                
            }
        }).start();
    }

    public void removeClient(){
        try {
            if(objectInputStream != null){
                objectInputStream.close();
            }

            if(objectOutputStream != null){
                objectOutputStream.close();
            }

            if(this.socket != null){
                this.socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Decrypt a message using Caesar cipher
    public static String decrypt(String encryptedMessage, int shift) {
        StringBuilder decryptedMessage = new StringBuilder();

        for (char c : encryptedMessage.toCharArray()) {
            // Decryption logic for each character
            if (Character.isLetter(c)) {
                char shiftedChar = (char) (c - shift);
                if ((Character.isLowerCase(c) && shiftedChar < 'a') ||
                    (Character.isUpperCase(c) && shiftedChar < 'A')) {
                    shiftedChar += 26; // Wrap around the alphabet
                }
                decryptedMessage.append(shiftedChar);
            } else {
                decryptedMessage.append(c); // Keep non-letter characters unchanged
            }

        }

        return decryptedMessage.toString();
    }

}
