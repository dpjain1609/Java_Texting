import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.StringTokenizer;

public class Message implements Serializable{

    private boolean quit;
    private String sender;
    private List<String> receivers;
    private String messageBody;
    int shiftValue;

    private Message(){
        this.sender = "";
        this.messageBody = "";
        this.receivers = new ArrayList<>();
    }

    public Message(boolean quit, String sender, String messageBody){
        this.quit = quit;
        this.sender = sender;
        this.messageBody = messageBody;
        this.receivers = new ArrayList<>();
        parser();
        this.shiftValue = (new SplittableRandom()).nextInt(24) + 1;
        this.messageBody = encrypt(messageBody, shiftValue, shiftValue);
    }

    private void parser(){
        StringTokenizer receiverTokenizer = new StringTokenizer(messageBody, ";");
        while(receiverTokenizer.hasMoreTokens()){
            this.receivers.add(receiverTokenizer.nextToken());
        }

        if(receivers.size() > 0){
            this.messageBody = this.receivers.get(this.receivers.size() - 1);
            this.receivers.remove(this.receivers.size() - 1);
        }
    }

    // Encrypt a message using Caesar cipher
    public static String encrypt(String message, int shift, int shiftIteration) {
        StringBuilder encryptedMessage = new StringBuilder();

        for (char c : message.toCharArray()) {
            // Encryption logic for each character
            if (Character.isLetter(c)) {
                char shiftedChar = (char) (c + shift);
                if ((Character.isLowerCase(c) && shiftedChar > 'z') ||
                    (Character.isUpperCase(c) && shiftedChar > 'Z')) {
                    shiftedChar -= 26; // Wrap around the alphabet
                }
                encryptedMessage.append(shiftedChar);
            } else {
                encryptedMessage.append(c); // Keep non-letter characters unchanged
            }
        }

        return encryptedMessage.toString();
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

    public int getShiftValue() {
        return shiftValue;
    }

    public boolean isQuit() {
        return quit;
    }
}