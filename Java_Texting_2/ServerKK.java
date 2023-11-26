import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // ... (previous code)

    public class Handler implements Runnable {
        private Socket socket;
        private ObjectOutputStream objectOutputStream;
        private ObjectInputStream objectInputStream;

        public Handler(Socket socket) {
            this.socket = socket;
            try {
                this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Message message = (Message) objectInputStream.readObject();
                    
                    if (message.getMessageBody().equalsIgnoreCase("quit")) {
                        // Handle the quit command
                        System.out.println(message.getSender() + " has left the chat.");
                        break; // Exit the loop when a client wants to quit
                    }
                    
                    // Broadcast the message to all clients
                    broadcastMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Close resources and remove the client
                closeResources();
                removeClient(this);
            }
        }

        // ... (rest of the Handler class)
    }

    // ... (rest of the Server class)
}
