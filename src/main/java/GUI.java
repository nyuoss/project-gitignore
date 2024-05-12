import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class GUI extends JFrame {
    private JTextField gitignorePathField;
    private JTextField startDirField;
    private JTextField resultsDirField;
    private JCheckBox overwriteCheckbox;
    private JButton runButton;
    private JButton cancelButton;

    public GUI() {
        createUI();
    }

    private void createUI() {
        setTitle("Gitignore Tool");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridLayout(5, 2, 10, 10));

        gitignorePathField = new JTextField();
        startDirField = new JTextField();
        resultsDirField = new JTextField();
        overwriteCheckbox = new JCheckBox("Overwrite existing files?");
        runButton = new JButton("Run");
        cancelButton = new JButton("Cancel");

        add(new JLabel("Path to .gitignore:"));
        add(gitignorePathField);
        add(new JLabel("Start Directory:"));
        add(startDirField);
        add(new JLabel("Results Directory:"));
        add(resultsDirField);
        add(new JLabel(""));
        add(overwriteCheckbox);
        add(runButton);
        add(cancelButton);

        runButton.addActionListener(this::runTool);
        cancelButton.addActionListener(e -> System.exit(0));

        pack();  // Fit the layout
    }

    private void runTool(ActionEvent e) {
        String gitignorePath = gitignorePathField.getText();
        String startDir = startDirField.getText();
        String resultsDir = resultsDirField.getText();
        boolean overwrite = overwriteCheckbox.isSelected();

        // Run the tool here using the provided parameters
        System.out.println("Running with parameters:");
        System.out.println("Gitignore path: " + gitignorePath);
        System.out.println("Start directory: " + startDir);
        System.out.println("Results directory: " + resultsDir);
        System.out.println("Overwrite: " + overwrite);

        // You would replace this with the actual command to run your tool
        // Example: runCommand(gitignorePath, startDir, resultsDir, overwrite);
        runCommand(gitignorePath, startDir, resultsDir, overwrite);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GUI ex = new GUI();
            ex.setVisible(true);
        });
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