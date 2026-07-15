package fileHandlingExample;

// ContactReader.java
// This program reads contact information from a file and displays it in a formatted way

// Step 1: Import necessary packages for file I/O operations
// Hint: You'll need classes from java.io or java.nio.file packages
// You'll also need Scanner for user input

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ContactReader {
    public static void main(String[] args) {
        // Step 2: Create a Scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        // Step 3: Prompt the user to enter the file name containing contacts
        System.out.println("Enter the name of the contacts file : ");
        String fileName = scanner.nextLine();
        // Step 4: Read the file name entered by the user
        int contactCount = 0;
        try {
            // Step 5: Create a FileReader or similar object to read the file
            // Hint: You can use FileReader, BufferedReader, or Files class
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            // Step 6: Read the file line by line
            // Hint: Use a loop to process each line
            while((line = reader.readLine()) != null){
                if(!line.trim().isEmpty()){
                    String[] contactInfo = line.split(":");
                    if (contactInfo.length == 2){
                        String name = contactInfo[0].trim();
                        String contactNumber = contactInfo[1].trim();
                        System.out.println("Contact: Name - "+name+", Contact Number - "+contactNumber);
                        contactCount++;
                    } else {
                        System.out.println("Contact info incomplete or not formatted properly");
                    }
                }
            }
            // Step 7: Parse each line to extract name and phone number
            // Hint: Use String methods like split() to separate by colon

            // Step 8: Display the contact information in a formatted way
            // Example: "Contact: John Doe | Phone: +1-555-123-4567"

            // Step 9: Close the file reader when done
            reader.close();
            System.out.println("Total contacts read: "+contactCount);

        } catch (FileNotFoundException e) {
            // Step 10: Handle exceptions appropriately
            // Display a user-friendly error message

        } catch (IOException e) {

        } catch (Exception e) {

        } finally {
            scanner.close();
        }

        // Optional Bonus:
        // Step 11: Add a feature to count and display the total number of contacts read
    }
}
