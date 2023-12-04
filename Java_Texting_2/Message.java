import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.StringTokenizer;

/*
 * Message class to encapsulate key features of message objects used communication
 * between clients and the server
 */
public class Message implements Serializable{

    private boolean quit;
    private String sender;
    private List<String> receivers;
    private String messageBody;
    int shiftValue;

    //default constructor
    private Message(){
        this.sender = "";
        this.messageBody = "";
        this.receivers = new ArrayList<>();
    }

    //initializing constructor
    //Once the object is created, parse it and set the field
    //generate a random number between 1 and 24 for encryption
    //encrypt the message body of message using the random number
    public Message(boolean quit, String sender, String messageBody){
        this.quit = quit;
        this.sender = sender;
        this.messageBody = messageBody;
        this.receivers = new ArrayList<>();
        parser();
        this.shiftValue = (new SplittableRandom()).nextInt(24) + 1;
        this.messageBody = encrypt(messageBody, shiftValue);
    }

    /*
     * Parser function for the message body
     * Sepearate the plaintext messages by semi colons
     * Add all, but the last string to the list of receivers
     * The last string is the actual message which needs to go in the messageBody field
     */
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
    public static String encrypt(String message, int shift) {
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

    //Getters and setters
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