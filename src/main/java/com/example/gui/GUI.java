package com.example.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;

public class GUI extends JFrame {
    private JTextField gitignorePathField;
    private JTextField startDirField;
    private JTextField resultsDirField;
    private JCheckBox overwriteCheckbox;
    private JButton runButton;
    private JButton cancelButton;
    private JButton gitignoreChooseButton;
    private JButton startDirChooseButton;
    private JButton resultsDirChooseButton;
    private JButton viewResultsButton;

    public GUI() {
        createUI();
    }

    private void createUI() {
        setTitle("Gitignore Tool");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridLayout(5, 3, 10, 10));  // Updated to accommodate file chooser buttons

        gitignorePathField = new JTextField();
        startDirField = new JTextField();
        resultsDirField = new JTextField();
        overwriteCheckbox = new JCheckBox("Overwrite existing files?");
        runButton = new JButton("Run");
        cancelButton = new JButton("Cancel");
        viewResultsButton = new JButton("View Results");  // Initialize the new button

        gitignoreChooseButton = new JButton("Choose...");
        startDirChooseButton = new JButton("Choose...");
        resultsDirChooseButton = new JButton("Choose...");

        add(new JLabel("Path to .gitignore:"));
        add(gitignorePathField);
        add(gitignoreChooseButton);
        add(new JLabel("Start Directory:"));
        add(startDirField);
        add(startDirChooseButton);
        add(new JLabel("Results Directory:"));
        add(resultsDirField);
        add(resultsDirChooseButton);
        add(new JLabel(""));
        add(overwriteCheckbox);
        add(new JLabel(""));  // Placeholder for alignment
        add(runButton);
        add(cancelButton);
        add(viewResultsButton);  // Add the new button to the com.example.gui.GUI

        runButton.addActionListener(this::runTool);
        cancelButton.addActionListener(e -> System.exit(0));
        viewResultsButton.addActionListener(this::viewResults);  // Add an action listener to the new button

        gitignoreChooseButton.addActionListener(e -> chooseFile(gitignorePathField));
        startDirChooseButton.addActionListener(e -> chooseDirectory(startDirField));
        resultsDirChooseButton.addActionListener(e -> chooseDirectory(resultsDirField));

        pack();  // Fit the layout
    }

    private void viewResults(ActionEvent e) {
        String resultsPath = resultsDirField.getText() + "/results/Human Readable Summary.txt";
        ResultsDialog resultsDialog = new ResultsDialog(this, "View Results", true, resultsPath);
        resultsDialog.setVisible(true);
    }

    private void chooseFile(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); // Set the start directory to the current working directory
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(true); // Allows selection of all types of files
        fileChooser.setFileHidingEnabled(false); // Enable the visibility of hidden files

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            textField.setText(file.getAbsolutePath());
        }
    }

    private void chooseDirectory(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); // Set the start directory to the current working directory
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setFileHidingEnabled(false); // Enable the visibility of hidden files

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            textField.setText(file.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GUI ex = new GUI();
            ex.setVisible(true);
        });
    }

private void runTool(ActionEvent e) {
    // Reset borders to default initially
    resetBorders();

    String gitignorePath = gitignorePathField.getText().trim();
    String startDir = startDirField.getText().trim();
    String resultsDir = resultsDirField.getText().trim();
    boolean overwrite = overwriteCheckbox.isSelected();

    // Check if any fields are empty and set borders and focus
    if (gitignorePath.isEmpty()) {
        showError(gitignorePathField, "Please enter the path to the .gitignore file.");
        return;
    }

    if (startDir.isEmpty()) {
        showError(startDirField, "Please enter the start directory path.");
        return;
    }

    if (resultsDir.isEmpty()) {
        showError(resultsDirField, "Please enter the results directory path.");
        return;
    }

    // Further validations for directory existence and correctness
    File gitignoreFile = new File(gitignorePath);
    File startDirectory = new File(startDir);
    File resultsDirectory = new File(resultsDir);

    if (!gitignoreFile.exists() || !gitignoreFile.isFile()) {
        showError(gitignorePathField, "The specified .gitignore path does not point to a valid file.");
        return;
    }

    if (!startDirectory.exists() || !startDirectory.isDirectory()) {
        showError(startDirField, "The start directory path is not a valid directory.");
        return;
    }

    if (!resultsDirectory.exists() || !resultsDirectory.isDirectory()) {
        showError(resultsDirField, "The results directory path is not a valid directory.");
        return;
    }

    // If all checks pass, run the command
    System.out.println("Running with parameters:");
    System.out.println("Gitignore path: " + gitignorePath);
    System.out.println("Start directory: " + startDir);
    System.out.println("Results directory: " + resultsDir);
    System.out.println("Overwrite: " + overwrite);

    runCommand(gitignorePath, startDir, resultsDir, overwrite);
}

// Helper method to show an error with red border and focus
private void showError(JTextField field, String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    field.setBorder(BorderFactory.createLineBorder(Color.RED));
    field.requestFocus();
}

// Resets the borders of all input fields to the default
private void resetBorders() {
    Border defaultBorder = UIManager.getBorder("TextField.border");
    gitignorePathField.setBorder(defaultBorder);
    startDirField.setBorder(defaultBorder);
    resultsDirField.setBorder(defaultBorder);
}

    // You may want to add a method to execute your tool's logic here
    private void runCommand(String gitignorePath, String startDir, String resultsDir, boolean overwrite) {
        try {
            // Prepare the command with parameters
            List<String> commands = new ArrayList<>();
            commands.add("java");
            commands.add("-jar");
            commands.add("target/demo-1.0-SNAPSHOT.jar");
            commands.add(gitignorePath);
            commands.add(startDir);
            commands.add(resultsDir);
            commands.add(String.valueOf(overwrite));  // Convert boolean to string

            // Use ProcessBuilder to start the process
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectErrorStream(true);  // Redirect errors to standard output
            Process process = builder.start();

            // Read output from the process
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete and check if the execution was successful
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                JOptionPane.showMessageDialog(this, "Process completed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error occurred during process execution", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(this, "Error executing the tool: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}