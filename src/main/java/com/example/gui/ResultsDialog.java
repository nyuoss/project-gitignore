package com.example.gui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResultsDialog extends JDialog {
    private JTextArea displayArea;

    public ResultsDialog(Frame owner, String title, boolean modal, String resultsFilePath) {
        super(owner, title, modal);
        setSize(500, 300);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createUI(resultsFilePath);
    }

    private void createUI(String resultsFilePath) {
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        displayResults(resultsFilePath);
    }

    private void displayResults(String resultsFilePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(resultsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            displayArea.setText(contentBuilder.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to read result file: " + ex.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
