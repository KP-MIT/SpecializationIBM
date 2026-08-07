package fileHandlingExample;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSystemManager {
    private File currentDirectory;
    private final Scanner scanner;
    private final SimpleDateFormat dateFormat;

    public FileSystemManager() {
        currentDirectory = new File(System.getProperty("user.dir"));
        scanner = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public void start() {
        System.out.println("Welcome to the File System Manager!");
        System.out.println("Type 'help' to see available commands.");

        boolean running = true;
        while (running) {
            // Display the current directory path as a prompt
            System.out.print(currentDirectory.getAbsolutePath() + "> ");

            // Read user command
            String command = scanner.nextLine().trim();

            // Process the command
            if (command.equalsIgnoreCase("exit")) {
                running = false;
            } else {
                running = processCommand(command);
            }
        }

        // Close the scanner before exiting
        scanner.close();
        System.out.println("Thank you for using File System Manager. Goodbye!");
    }

    private boolean processCommand(String command) {
        if (command.isEmpty()) {
            return true;
        }
        String[] parts = command.trim().split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String args = (parts.length > 1) ? parts[1] : "";

        switch (commandName) {
            case "help" -> displayHelp();
            case "ls" -> listFiles();
            case "cd" -> changeDirectory(args);
            case "pwd" -> System.out.println(currentDirectory.getAbsolutePath());
            case "mkdir" -> {
                if (args.isEmpty()) {
                    System.out.println("Error: Directory name is required");
                } else {
                    createDirectory(args);
                }
            }
            case "touch" -> {
                if (args.isEmpty()) {
                    System.out.println("Error: File name is required");
                } else {
                    createFile(args);
                }
            }
            case "rm" -> {
                if (args.isEmpty()) {
                    System.out.println("Error: File or directory name is required");
                } else {
                    delete(args);
                }
            }
            case "rename" -> {
                String[] nameParts = args.split("\\s+", 2);
                if (nameParts.length < 2) {
                    System.out.println("Error: Both old and new names are required");
                } else {
                    rename(nameParts[0], nameParts[1]);
                }
            }
            case "find" -> {
                if (args.isEmpty()) {
                    System.out.println("Error: Search pattern is required");
                } else {
                    findFiles(args);
                }
            }
            case "info" -> {
                if (args.isEmpty()) {
                    System.out.println("Error: File name is required");
                } else {
                    displayFileInfo(args);
                }
            }
            case "exit" -> {
                return false;
            }
            default -> {
                System.out.println("Unknown command: " + commandName);
                System.out.println("Type 'help' for available commands.");
            }
        }

        return true;
    }

    private void displayHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  help              - Display this help message");
        System.out.println("  ls                - List files in current directory");
        System.out.println("  cd <directory>    - Change to specified directory (use .. for parent)");
        System.out.println("  pwd               - Print current directory path");
        System.out.println("  mkdir <name>      - Create a new directory");
        System.out.println("  touch <name>      - Create a new file");
        System.out.println("  rm <name>         - Delete a file or directory");
        System.out.println("  rename <old> <new> - Rename a file or directory");
        System.out.println("  find <pattern>    - Search for files matching pattern");
        System.out.println("  info <name>       - Display file information");
        System.out.println("  exit              - Exit the program");
    }
    private void listFiles() {
        // Get the list of files and directories in the current directory
        File[] files = currentDirectory.listFiles();

        // Check if the directory is empty or cannot be accessed
        if (files == null || files.length == 0) {
            System.out.println("Directory is empty or cannot be accessed.");
            return;
        }
        // Display the list of files and directories
        System.out.println("Contents of " + currentDirectory.getAbsolutePath() + ":");
        System.out.println("Type | Size (bytes) | Last Modified       | Name");
        System.out.println("-------------------------------------------------");

        for (File file : files) {
            // Show 'd' if it's a directory or '-' if it's a file
            char type = file.isDirectory() ? 'd' : '-';

            // Format the last modified date
            String lastModified = dateFormat.format(new Date(file.lastModified()));

            // Display file information
            System.out.printf(" %c   | %11d | %s | %s%n",
                    type, file.length(), lastModified, file.getName());
        }
    }
    private void changeDirectory(String dirName) {
        if (dirName.isEmpty()) {
            // If no directory is specified, don't change directory
            System.out.println("Current directory: " + currentDirectory.getAbsolutePath());
            return;
        }

        if (dirName.equals("..")) {
            // Go to parent directory
            File parent = currentDirectory.getParentFile();
            if (parent != null) {
                currentDirectory = parent;
            } else {
                System.out.println("Already at root directory.");
            }
        } else {
            // Change to the specified directory
            File newDir = new File(currentDirectory, dirName);

            if (newDir.exists() && newDir.isDirectory()) {
                currentDirectory = newDir;
            } else {
                System.out.println("Error: Directory does not exist: " + dirName);
            }
        }
    }
    private void createDirectory(String dirName) {
        File newDir = new File(currentDirectory, dirName);

        if (newDir.exists()) {
            System.out.println("Error: A file or directory with that name already exists.");
            return;
        }

        boolean created = newDir.mkdir();
        if (created) {
            System.out.println("Directory created: " + dirName);
        } else {
            System.out.println("Error: Failed to create directory: " + dirName);
        }
    }

    private void createFile(String fileName) {
        File newFile = new File(currentDirectory, fileName);

        if (newFile.exists()) {
            System.out.println("Error: A file or directory with that name already exists.");
            return;
        }

        try {
            boolean created = newFile.createNewFile();
            if (created) {
                System.out.println("File created: " + fileName);
            } else {
                System.out.println("Error: Failed to create file: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    private void delete(String name) {
        File fileToDelete = new File(currentDirectory, name);

        if (!fileToDelete.exists()) {
            System.out.println("Error: File or directory does not exist: " + name);
            return;
        }

        if (fileToDelete.isDirectory()) {
            // Check if directory is empty
            File[] contents = fileToDelete.listFiles();
            if (contents != null && contents.length > 0) {
                System.out.println("Warning: Directory is not empty. Delete anyway? (y/n)");
                String response = scanner.nextLine().trim().toLowerCase();

                if (!response.equals("y")) {
                    System.out.println("Deletion cancelled.");
                    return;
                }

                // Recursively delete directory contents
                boolean allDeleted = deleteRecursively(fileToDelete);
                if (allDeleted) {
                    System.out.println("Directory and its contents deleted: " + name);
                } else {
                    System.out.println("Error: Failed to delete some contents of directory: " + name);
                }
                return;
            }
        }

        boolean deleted = fileToDelete.delete();
        if (deleted) {
            System.out.println((fileToDelete.isDirectory() ? "Directory" : "File") + " deleted: " + name);
        } else {
            System.out.println("Error: Failed to delete " +
                    (fileToDelete.isDirectory() ? "directory" : "file") + ": " + name);
        }
    }

    private boolean deleteRecursively(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File file : contents) {
                if (file.isDirectory()) {
                    deleteRecursively(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    private void rename(String oldName, String newName) {
        File oldFile = new File(currentDirectory, oldName);
        File newFile = new File(currentDirectory, newName);

        if (!oldFile.exists()) {
            System.out.println("Error: File or directory does not exist: " + oldName);
            return;
        }

        if (newFile.exists()) {
            System.out.println("Error: A file or directory with the new name already exists: " + newName);
            return;
        }

        boolean renamed = oldFile.renameTo(newFile);
        if (renamed) {
            System.out.println((oldFile.isDirectory() ? "Directory" : "File") +
                    " renamed from " + oldName + " to " + newName);
        } else {
            System.out.println("Error: Failed to rename " +
                    (oldFile.isDirectory() ? "directory" : "file") + ".");
        }
    }

    private void findFiles(String pattern) {
        System.out.println("Searching for files matching pattern: " + pattern);
        int count = findFilesRecursively(currentDirectory, pattern, 0);
        System.out.println("Found " + count + " matching files/directories.");
    }

    private int findFilesRecursively(File directory, String pattern, int currentDepth) {
        int count = 0;
        File[] files = directory.listFiles();

        if (files == null) {
            return 0;
        }

        for (File file : files) {
            // Check if the file name matches the pattern
            if (file.getName().contains(pattern)) {
                // Print indentation based on depth
                for (int i = 0; i < currentDepth; i++) {
                    System.out.print("  ");
                }

                // Print file information
                System.out.println(file.isDirectory() ? "[Dir] " : "[File] " + file.getName());
                count++;
            }

            // Recursively search in subdirectories
            if (file.isDirectory()) {
                count += findFilesRecursively(file, pattern, currentDepth + 1);
            }
        }

        return count;
    }

    private void displayFileInfo(String fileName) {
        File file = new File(currentDirectory, fileName);

        if (!file.exists()) {
            System.out.println("Error: File or directory does not exist: " + fileName);
            return;
        }

        System.out.println("Information for: " + file.getName());
        System.out.println("----------------------------------");
        System.out.println("Type:          " + (file.isDirectory() ? "Directory" : "File"));
        System.out.println("Path:          " + file.getAbsolutePath());
        System.out.println("Size:          " + file.length() + " bytes");
        System.out.println("Last Modified: " + dateFormat.format(new Date(file.lastModified())));
        System.out.println("Readable:      " + file.canRead());
        System.out.println("Writable:      " + file.canWrite());
        System.out.println("Executable:    " + file.canExecute());
        System.out.println("Hidden:        " + file.isHidden());

        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            int fileCount = 0;
            int dirCount = 0;

            if (contents != null) {
                for (File child : contents) {
                    if (child.isDirectory()) {
                        dirCount++;
                    } else {
                        fileCount++;
                    }
                }
            }

            System.out.println("Content:       " + fileCount + " files, " + dirCount + " directories");
        }
    }
    public static void main(String[] args) {
        FileSystemManager manager = new FileSystemManager();
        manager.start();
    }
}
