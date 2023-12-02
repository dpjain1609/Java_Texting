public class Test {
    public static void main(String[] args) {
        String msg = "me;myself;main;hello world";
        Message message = new Message(false, "me", msg);
        for(String str : message.getReceivers()){
            System.out.println(str);
        }

        System.out.println();
        System.out.println(message.getMessageBody());
    }
}
