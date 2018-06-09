package server;

import java.util.*;
import java.io.*;
import java.net.*;

import util.*;

public class ClientListener implements Runnable {
    String username;
    ObjectInputStream input;
    List<Message> messageList;
    ObjectOutputStream logger;

    public ClientListener(String u, ObjectInputStream i, List<Message> l) {
        try {
            username = u;
            input = i;
            messageList = l;
            logger = new ObjectOutputStream(new FileOutputStream(new File("log"), true));

        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public void recordMessage(Message m) throws IOException {
        messageList.add(m);
        logger.writeObject(m);
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Reads a string from the socket's InputStream. I think this blocks until new
                // string is read?
                Message message = (Message) input.readObject();
                recordMessage(message);
                System.out.println("Client sent " + message.getMessage());
                synchronized (messageList) {
                    messageList.notifyAll();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                recordMessage(new Message(username + " left.", Message.SERVER, new Date()));
            } catch (IOException e1) {
                //e1.printStackTrace();
            }
        }
    }
}
