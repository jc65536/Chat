package server;

import util.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class IncomingClientHandler implements Runnable {
    private List<Message> messageList;
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Map<String, char[]> usersList;

    public IncomingClientHandler(List<Message> l, Map<String, char[]> u, Socket c) {
        usersList = u;
        messageList = l;
        client = c;
        try {
            output = new ObjectOutputStream(c.getOutputStream());
            input = new ObjectInputStream(c.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("New client accepted at " + client.getInetAddress());
        try {
            boolean isRegistering = input.readBoolean();
            User incomingUser;
            while (true) {
                try {
                    incomingUser = (User) input.readObject();
                    boolean isUserValid;
                    if (isRegistering) {
                        // The user will not be valid if the same username is already registered.
                        isUserValid = !usersList.containsKey(incomingUser.getUsername());
                    } else {
                        try {
                            // The user will not be valid if the password doesn't match.
                            isUserValid = Arrays.equals(usersList.get(incomingUser.getUsername()), incomingUser.getPassword());
                        } catch (NullPointerException e) {
                            isUserValid = false;
                        }
                    }
                    System.out.println("The entered user info is " + (isUserValid ? "valid." : "invalid."));
                    output.writeBoolean(isUserValid);
                    output.flush();
                    if (isUserValid) break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (isRegistering) {
                usersList.put(incomingUser.getUsername(), incomingUser.getPassword());
            }
            // Create both the client updater and listener and starts both threads.
            ClientUpdater updater = new ClientUpdater(incomingUser.getUsername(), output, messageList);
            new Thread(updater, "updater").start();
            ClientListener listener = new ClientListener(incomingUser.getUsername(), input, messageList);
            new Thread(listener, "listener").start();
            listener.recordMessage(new Message(incomingUser.getUsername() + " joined.", Message.SERVER, new Date()));
            synchronized (messageList) {
                messageList.notifyAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
