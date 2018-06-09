package client;

import util.Message;

import java.io.*;
import java.net.InetAddress;
import java.util.Date;

public class MessageSender {
    private ObjectOutputStream output;
    private String username;
    private InetAddress address;

    public MessageSender (ObjectOutputStream o, String u, InetAddress ip) {
        output = o;
        username = u;
        address = ip;
    }

    public void sendMessage(String text) {
        text = text.trim();
        if (!"".equals(text)) {
            try {
                output.writeObject(new Message(text, username, address, new Date()));
                System.out.println("Sent message " + text);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
