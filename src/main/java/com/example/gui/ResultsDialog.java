package com.example.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class ResultsDialog extends JDialog {

    public ResultsDialog(Frame parent, String title, boolean modal, String filePath) {
        super(parent, title, modal);
        maximizeDialog();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents(filePath);
    }

    private void maximizeDialog() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        Rectangle bounds = config.getBounds();
        setBounds(bounds);
        setMaximumSize(bounds.getSize());
    }

    private void initComponents(String filePath) {
        // Set the size of the dialog
//        setSize(600, 400);
//        setLocationRelativeTo(getParent());

        // Create a text area to display the JSON content
        JTextArea textArea = new JTextArea(15, 50);
        textArea.setEditable(false); // Make the text area non-editable

        // Add a JScrollPane to make the text area scrollable
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Read the JSON file and display it in the text area
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            textArea.setText(content);
        } catch (IOException e) {
            textArea.setText("Failed to load the file: " + e.getMessage());
        }

        // Add the scroll pane to the content pane of the dialog
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Add a button to close the dialog
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}
