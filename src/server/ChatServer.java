package server;

import util.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            List<Message> messageList = Collections.synchronizedList(new ArrayList<>());
            Map<String, char[]> usersList = Collections.synchronizedMap(new HashMap<String, char[]>());
            System.out.println("Server hostname: " + InetAddress.getLocalHost().getHostName());
            while (true) {
                try {
                    // Accepts a client when a connection is requested.
                    // Starts an IncomingClientHandler thread so the registration/login process doesn't block other clients.
                    Socket client = server.accept();
                    new Thread(new IncomingClientHandler(messageList, usersList, client)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
