import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    
    private String username;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private Socket clientSocket;
    
    public ClientHandler(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        this.inputStream = new ObjectInputStream(this.clientSocket.getInputStream());
        this.outputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
    }


    @Override
    public void run() {
        Message messageReceived;

        while(!clientSocket.isClosed()){
            try {
                messageReceived = (Message) this.inputStream.readObject();
                if(messageReceived.getMessageBody().equalsIgnoreCase("quit")){
                    this.clientSocket.close();
                    break;
                }

                System.out.println(messageReceived.getSender() + ": " + messageReceived.getMessageBody());
                for(ClientHandler clientHandler : Server.clientHandlerList){
                    if(messageReceived.getReceivers().contains(clientHandler.username)){
                        clientHandler.outputStream.writeObject(messageReceived);
                    }
                }

            } catch (ClassNotFoundException c) {
                
            } catch(IOException i){
                i.printStackTrace();
            }
        }

        closeClientHandler();
    }

    public void closeClientHandler(){

        try {

            this.outputStream.close();
            this.inputStream.close();
            if(this.clientSocket != null){
                this.clientSocket.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
