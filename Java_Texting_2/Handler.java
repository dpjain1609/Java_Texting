import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Handler implements Runnable{
    
    public static Map<String, Handler> clientHandlerMap = new HashMap<>();
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String clientUsername;

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

    public void sendMessage(Message messageToSend){

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