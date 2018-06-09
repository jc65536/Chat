package client;

import util.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class ServerListener implements Runnable {
    MessageDisplay display;
    List<Message> messageList;
    ObjectInputStream input;

    public ServerListener(ObjectInputStream i, MessageDisplay m, List<Message> l) {
        input = i;
        display = m;
        messageList = l;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = (Message) input.readObject();
                System.out.println("Message " + message.getMessage() + " received.");
                messageList.add(message);
                display.addMessage(message.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
