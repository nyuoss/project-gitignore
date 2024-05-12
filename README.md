# Gitignore Verifier Project

## Overview (Project's purpose)
The Gitignore Verifier is a powerful tool designed to enhance the visibility and management of file exclusion in projects using `.gitignore` rules. It traverses a specified directory structure, applies `.gitignore` rules, and generates detailed reports about the files it processes. This tool helps developers understand which files are ignored by `.gitignore` and why, making it invaluable for debugging and verifying `.gitignore` configurations.

## Features
- **File System Traversal**: Recursively walks through the file system starting from a specified root directory.
- **Rule Application**: Utilizes `.gitignore` rules to determine whether files should be ignored.
- **Detailed Reporting**: Provides comprehensive reports in both human-readable and machine-readable (JSON) formats, detailing which files are ignored or included and the specific `.gitignore` rules that affect them.
- **Conflict Detection**: Identifies and reports conflicts where multiple `.gitignore` rules might apply contradictorily to the same file.
- **Performance Metrics**: Captures and reports metrics such as total files processed, files included and excluded, execution time, and detected conflicts.

## Usage
To use the Gitignore Verifier, you need to provide paths to your `.gitignore` file and the directory you want to verify. The tool will then process the directory and generate reports that can be used to audit your `.gitignore` settings.

### Prerequisites
- Java 11 or higher
- Maven (for dependency management and building the project)

### Installation instructions
Clone the repository to your local machine and build the project using Maven:
```bash
git clone https://github.com/nyuoss/project-gitignore.git
cd gitignore-verifier
```

### Building the Project
To build the project, navigate to the project directory where the `pom.xml` file is located and run:

  ```bash
  mvn clean install package
  ```
This command will compile the source code, run the tests, and package the compiled code into a JAR file. 
The resulting JAR file, target/demo-1.0-SNAPSHOT.jar, is what you will use to run the tool.

**Note:** If you dont do this, you will encounter the message: 
"Error: Unable to access jarfile target/demo-1.0-SNAPSHOT.jar"
Please run this command again to ensure the jar to run the tool gets created to proceed.

### Running the Tool (Usage instructions)
**Compilation and Execution**
To compile and run the tool, use the following commands:
   ```bash
  java -jar target/demo-1.0-SNAPSHOT.jar <path_to_gitignore> <start_directory> <results_directory> <overwrite_flag>
  ```
Parameters:
  - <path_to_gitignore>: The path to the .gitignore file.
  - <start_directory>: The directory path where the file system traversal should start.
  - <results_directory>: The directory where the output files will be saved.
  - <overwrite_flag>: Boolean flag (true or false) indicating whether to overwrite existing files in the results directory.

  Example Commands:
  - Running with Overwrite Enabled
       ```bash
      java -jar target/demo-1.0-SNAPSHOT.jar /Users/yourusername/projects/myproject/.gitignore /Users/yourusername/projects/myproject /Users/yourusername/projects/myproject/results true
      ```

**GUI Mode:**
If no command line arguments are provided, the tool launches in GUI mode, providing a graphical interface for interaction:
  ```bash
  java -jar target/demo-1.0-SNAPSHOT.jar
  ```
In this mode, users can interact with the GUI to input parameters and execute file system traversal visually.

**Error Handling:**
- If an invalid path is provided for the .gitignore file, start directory, or results directory, the tool will print an error message indicating the specific issue.
- If fewer than the required four arguments are provided, the tool will display usage information and exit.

## Testing

The Gitignore Verifier project employs a robust testing strategy to ensure the application functions as intended and is free from defects. Our testing framework utilizes JUnit 5 for both unit and integration testing, with Mockito for dependency mocking, ensuring comprehensive coverage and reliability of our codebase.

### Unit Testing
- Framework Used: JUnit 5
- Mocking Framework: Mockito
- Execution: To run unit tests, navigate to the project directory and execute:
  
  ```bash
  mvn test
  ```
  
### Integration Testing
  - Framework Used: JUnit 5
  - Focus: Integration tests are designed to test the interactions between components such as file system traversal, rule application, and report generation. These tests involve reading from actual .gitignore files and processing a directory structure to validate the entire workflow of the application.
  - Execution: Integration tests are executed alongside unit tests but can be distinguished by their annotations and setup requirements.

## Circle CI Build Status

[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/KYnFbY2qNNDLwRPNicQYzN/JCe2pdy1WyY4eU5bckY53j/tree/main.svg?style=svg&circle-token=2c19527cd3f0e1b351c32ad7862299196d120a5d)](https://dl.circleci.com/status-badge/redirect/circleci/KYnFbY2qNNDLwRPNicQYzN/JCe2pdy1WyY4eU5bckY53j/tree/main)


## Component Specification:
Our repository is structured with three main components: the src directory containing all source code, the test directory housing unit tests, and the gui directory for user interface related files and components. These components interact to ensure code functionality and integrity.


## Contribution guidelines
Contributions are welcome! Please refer to `CONTRIBUTING.md` for contribution guidelines.


## Issue Template

When opening an issue, please include the following information for better resolution:

- **Title**: A concise description of the issue.
- **Description**: Detailed information about the issue being reported.
- **Steps to Reproduce**: List the steps to reproduce the issue (if applicable).
- **Expected Behavior**: Describe what you expected to happen.
- **Actual Behavior**: Describe what actually happened. Include error messages and screenshots if possible.
- **Environment Details**: Java version, operating system, and any other relevant environment details.
- **Additional Information**: Any other details or context that might be helpful.

## Pull Request Template

Please ensure your pull request adheres to the following guidelines:

- **Title**: A brief, descriptive summary of your changes.
- **Description**: Explain the changes you've made and why you've made them.
- **Branch Naming**: Prefix the type of change in the branch name and/or select the option relevant:
  - [ ] `chore/` This change requires a documentation update
  - [ ] Bug fix `bugfix/` (non-breaking change which fixes an issue)
  - [ ] New feature `feature/` (non-breaking change which adds functionality)
  - [ ] Breaking change `fix/` (fix or feature that would cause existing functionality to not work as expected)
- **Related Issue(s)**: Mention any issues that are related to these changes.
- **Testing**: Detail the testing that was done to validate the changes.
- **Checklist**:
  - [ ] I have performed a self-review of my own code.
  - [ ] I have added tests that prove my fix is effective or that my feature works.
- **Screenshots/Output**: Include any relevant screenshots or output demonstrating the changes (if applicable).
- **Additional Notes**: Any further comments or notes for the reviewers.  

## LICENSE

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
