package client;

import util.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Dialogs {

    public static String REGISTER = "Register";
    public static String LOGIN = "Log in";

    public static String hostnameDialog(Stack<String> l) {
        final String[] hostname = new String[1];
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.add(new JLabel("Recent hosts:"));
        JComboBox<String> dropdown = new JComboBox<>();
        while (!l.isEmpty()) {
            dropdown.addItem(l.pop());
        }
        for (int a = dropdown.getItemCount() - 4; a < dropdown.getItemCount(); a++) {
            try {
                l.push(dropdown.getItemAt(a));
            } catch (IndexOutOfBoundsException e) {
            }
        }
        contents.add(dropdown);
        contents.add(new JLabel("Or, enter a new hostname here:"));
        JTextField input = new JTextField();
        contents.add(input);
        JButton enter = new JButton("Enter");
        contents.add(enter);
        Thread t = Thread.currentThread();
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input.getText().trim().equals("")) {
                    hostname[0] = (String) dropdown.getSelectedItem();
                } else {
                    hostname[0] = input.getText().trim();
                }
                synchronized (t) {
                    t.notify();
                }
                ((JFrame) SwingUtilities.getRoot(enter)).dispose();
            }
        });
        JFrame frame = new JFrame();
        frame.add(contents);
        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        synchronized (t) {
            try {
                t.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return hostname[0];
    }

    public static User userAuthenticationDialog(String type) {
        assert "Register".equals(type) || "Login".equals(type);
        // Ignore till line 75.
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.add(new JLabel("Username"));
        JTextField usernameInput = new JTextField();
        contents.add(usernameInput);
        contents.add(new JLabel("Password"));
        JPasswordField passwordInput = new JPasswordField();
        contents.add(passwordInput);
        JButton enter = new JButton(type);
        contents.add(enter);
        Thread t = Thread.currentThread();
        final User[] user = new User[1];

        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!usernameInput.getText().trim().equals("") && !(passwordInput.getPassword().length == 0)) {
                    user[0] = new User(usernameInput.getText(), passwordInput.getPassword());
                    synchronized (t) {
                        // Notify it's time to move on.
                        t.notify();
                    }
                    ((JFrame) SwingUtilities.getRoot(enter)).dispose();
                }
            }
        });

        JFrame frame = new JFrame();
        frame.add(contents);
        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        synchronized (t) {
            try {
                // Pause until notified. (Is this bad practice?)
                t.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return user[0];
    }
}
