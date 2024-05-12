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
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html"); // Set content type to HTML
        textPane.setEditable(false); // Make it non-editable

        JScrollPane scrollPane = new JScrollPane(textPane);

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String styledContent = applyStyles(content); 
            textPane.setText(styledContent);
        } catch (IOException e) {
            textPane.setText("<html><body style='color: red;'>Failed to load the file: " + e.getMessage() + "</body></html>");
        }

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private String applyStyles(String content) {
        StringBuilder styledContent = new StringBuilder("<html><body>");
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.replace("[INCLUDED]", "<span style='color: green;'>[INCLUDED]</span>");
            line = line.replaceAll("\\[EXCLUDED by `Rule\\{pattern='[^']+?', isNegation=false\\}`\\]", "<span style='color: red;'>$0</span>");
    
            styledContent.append("<div>").append(line).append("</div>");
        }

        styledContent.append("</body></html>");
    
        return styledContent.toString();
    }
}

