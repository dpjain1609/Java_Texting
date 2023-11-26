import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    // ... (previous code)

    public void sendMessage() {
        Scanner scanner = new Scanner(System.in);

        try {
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();

                if (messageToSend.equalsIgnoreCase("quit")) {
                    // Send a quit message to the server
                    Message quitMessage = new Message(true, this.username, "quit");
                    objectOutputStream.writeObject(quitMessage);
                    objectOutputStream.flush();
                    break; // Exit the loop when the user wants to quit
                } else {
                    // Send regular chat messages
                    Message messageToServer = new Message(true, this.username, messageToSend);
                    Message messageToClient = new Message(false, this.username, messageToSend);
                    objectOutputStream.writeObject(messageToClient);
                    objectOutputStream.flush();
                }
            }
        } catch (Exception e) {
            removeClient();
            System.out.println("Exception in sendMessage()");
        }

        scanner.close();
    }

    // ... (rest of the code)
}
