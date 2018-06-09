package server;

import util.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class ClientUpdater implements Runnable {
    String username;
    ObjectOutputStream output;
    List<Message> messageList;
    private int lastIndex = 0;

    public ClientUpdater(String u, ObjectOutputStream i, List<Message> l) {
        username = u;
        this.output = i;
        this.messageList = l;
    }

    public static void print(String s) {
        System.out.println(s);
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (messageList) {
                    messageList.wait();
                }
                for (; lastIndex < messageList.size(); lastIndex++) {
                    output.writeObject((Message) messageList.get(lastIndex));
                }
                System.out.println("Updated clients.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
