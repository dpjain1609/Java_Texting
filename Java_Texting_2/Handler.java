import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
/*
 * Handler class for Client
 * For each client connected to the server, create a handler
 * Add the handler to clientHandlerMap with the client's username being the key
 */
public class Handler implements Runnable{
    
    public static Map<String, Handler> clientHandlerMap = new HashMap<>();
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String clientUsername;

    /*
     * Constructor
     * Initializes the given socket, input stream and output stream
     * Receive the username message from the client
     * Send a message to the server
     */
    public Handler(Socket socket){

        try {
            this.socket = socket;
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            
            Message usernameObject = (Message)this.objectInputStream.readObject();
            this.clientUsername = usernameObject.getSender();
            clientHandlerMap.put(this.clientUsername, this);

            String body = "SERVER: " + clientUsername + " has entered the chat";
            System.out.println(body);
            Message messageToClient = new Message(false, clientUsername, body);

            sendMessage(messageToClient);
        } catch (Exception e) {
            removeHandler();
            System.out.println("Exception in Handler()");
        }
    }

    /*
     * Function to send a message from the client to the receivers of that message
     */
    public void sendMessage(Message messageToSend){

        //if the client tries to send a message to all users, send a message to each key present in the handler map
        try {
            if(messageToSend.getReceivers().get(0).equalsIgnoreCase("all")){
                for(String receiver : clientHandlerMap.keySet()){
                    try {
                        clientHandlerMap.get(receiver).objectOutputStream.writeObject(messageToSend);
                        clientHandlerMap.get(receiver).objectOutputStream.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            
        }

        //if the client tries to send a message to specific users, get their keys from the map and send the
        //message to them
        for(String receiver : messageToSend.getReceivers()){
            try {
                if(clientHandlerMap.containsKey(receiver)){
                    clientHandlerMap.get(receiver).objectOutputStream.writeObject(messageToSend);
                    clientHandlerMap.get(receiver).objectOutputStream.flush();
                }
            } catch (Exception e) {
                removeHandler();
                e.printStackTrace();
            }
        }
    }

    /*
     * Function to remove handler
     * Send a notification to the server
     * Close the input, output streams and the socket
     */
    public void removeHandler(){
        clientHandlerMap.remove(this.clientUsername);
        String body = "SERVER: " + clientUsername + " has left the chat";
        Message messageToClient = new Message(false, clientUsername, body);

        sendMessage(messageToClient);

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

    //Receive a message via the server and check if it is a quit message
    //If it is a quit message, send a notification to the server and remove the handler from the map
    //Otherwise, send the message to intended receivers
    @Override
    public void run() {
        Message messageFromClient;

        while(socket.isConnected()){
            try {
                messageFromClient = (Message)objectInputStream.readObject();

                //user wants to quit
                if(messageFromClient.isQuit()){
                    String message = this.clientUsername + " has left the chat";
                    sendMessage(new Message(false, "SERVER", message));
                    System.out.println("SERVER: " + message);
                    removeHandler();
                    break;
                }

                sendMessage(messageFromClient);
            } catch (Exception e) {
                removeHandler();
                e.printStackTrace();
                break;
            }
        }
    }
}