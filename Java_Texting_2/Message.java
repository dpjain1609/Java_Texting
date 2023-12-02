import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Message implements Serializable{

    private static int id;
    private boolean messageToServer;
    private String sender;
    private List<String> receivers;
    private String messageBody;

    private Message(){
        this.messageToServer = false;
        this.sender = "";
        this.messageBody = "";
        this.receivers = new ArrayList<>();
    }

    public Message(boolean messageToServer, String sender, String messageBody){
        this.messageToServer = messageToServer;
        this.sender = sender;
        this.messageBody = messageBody;
        this.receivers = new ArrayList<>();
        parser();
    }

    private void parser(){
        StringTokenizer receiverTokenizer = new StringTokenizer(messageBody, ";");
        while(receiverTokenizer.hasMoreTokens()){
            this.receivers.add(receiverTokenizer.nextToken());
        }

        this.messageBody = this.receivers.get(this.receivers.size() - 1);
        this.receivers.remove(this.receivers.size() - 1);
    }

    public static int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public List<String> getReceivers() {
        return receivers;
    }
}