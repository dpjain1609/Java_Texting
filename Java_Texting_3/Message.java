import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable{

    private static int id;
    private boolean messageToServer;
    private String sender;
    private List<String> receivers;
    private String messageBody;

    private Message(){
        this.messageToServer = false;
        this.sender = "";
        this.receivers = new ArrayList<>();
        this.messageBody = "";
    }

    public Message(boolean messageToServer, String sender, List<String> receivers, String messageBody){
        this.messageToServer = messageToServer;
        this.sender = sender;
        this.receivers = receivers;
        this.messageBody = messageBody;
    }

    public static int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public String getMessageBody() {
        return messageBody;
    }

}