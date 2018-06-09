package client;

import util.Message;
import util.User;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {

	public static final int PORT = 1234;

	private Socket socket;
	private String username;
	private Stack<String> recentHosts;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private List<Message> messageList = Collections.synchronizedList(new ArrayList<>());
	private User user;
	private MessageDisplay display;

	void setUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

	void readRecentHosts() {
		try {
			recentHosts = (Stack<String>) new ObjectInputStream(new FileInputStream("recent")).readObject();
		} catch (IOException | ClassNotFoundException e) {
			recentHosts = new Stack<>();
		}
	}

	void connectToServer() {
        while (true) {
            try {
                socket = new Socket(InetAddress.getByName(Dialogs.hostnameDialog(recentHosts)), PORT);
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
            } catch (UnknownHostException e) {
                JOptionPane.showMessageDialog(null, "Invalid host.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                if (JOptionPane.showConfirmDialog(null, "An IO error occurred while connecting to the server. Try again?", "Error", JOptionPane.YES_NO_OPTION) == 1) {
                    System.exit(1);
                }
                continue;
            }
            break;
        }
    }

    void authenticateUser() {
        boolean isRegistering = JOptionPane.showOptionDialog(null, "Log in or register?", "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Log in", "Register"}, null) == 1;
        try {
            output.writeBoolean(isRegistering);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
                User userInput = Dialogs.userAuthenticationDialog(isRegistering ? Dialogs.REGISTER : Dialogs.LOGIN);
                output.writeObject(userInput);
                output.flush();
                if (input.readBoolean()) {
                    user = userInput;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    void initializeGUI() {
	    display = new MessageDisplay(new MessageSender(output, user.getUsername(), socket.getInetAddress()));
	    new Thread(new ServerListener(input, display, messageList)).start();
    }

	void writeRecentHosts() {
		try {
			new ObjectOutputStream(new FileOutputStream("recent")).writeObject(recentHosts);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.setUI();
		client.readRecentHosts();
		client.connectToServer();
		client.authenticateUser();
		client.initializeGUI();
		client.writeRecentHosts();
	}
}
