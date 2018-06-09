package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MessageDisplay extends JFrame {
    private JEditorPane messageFrame = new JEditorPane();
    private JScrollPane messageScrollPane = new JScrollPane(messageFrame);
    private Container inputFrame = new Container();
    private JTextField inputField = new JTextField();
    private JButton sendButton = new JButton("Send");

    public MessageDisplay(MessageSender s) {
        setLayout(new BorderLayout());
        messageFrame.setEditable(false);
        inputFrame.setLayout(new BorderLayout());
        inputFrame.add(inputField, BorderLayout.CENTER);
        inputFrame.add(sendButton, BorderLayout.EAST);
        ActionListener sendMessageListener = actionEvent -> {
            String message = inputField.getText();
            s.sendMessage(message);
            inputField.setText("");
        };
        sendButton.addActionListener(sendMessageListener);
        inputField.addActionListener(sendMessageListener);
        add(messageScrollPane, BorderLayout.CENTER);
        add(inputFrame, BorderLayout.SOUTH);
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void addMessage(String message) {
        messageFrame.setText(messageFrame.getText() + "\n" + message);
        messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum());
    }

}
