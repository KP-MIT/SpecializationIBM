package collectionsExample;

import java.util.HashMap;
import java.util.Scanner;

public class PhoneBookHashMap {
    private static boolean isNameValid(String name){
        if (!name.matches("^[a-zA-Z' -]+$")){
            System.out.println("invalid name");
            return false;
        }
        return true;
    }

    private static boolean isPhoneNumberValid(String phoneNumber) {
        if (!phoneNumber.matches("\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}")){
            System.out.println("invalid phone number");
            return false;
        }
        return true;
    }

    public static void main(String s[]) {
        try {
            Scanner scanner = new Scanner(System.in);
            HashMap<String, String> phonebook = new HashMap<>();

            while (true) {
                System.out.println("Press 1 to add an entry in the phonebook," +
                        "\n2 to view all the entries" +
                        "\n3 to search for entries with name" +
                        "\n4 to delete an entry" +
                        "\nAny other key to exit");

                String userAction = scanner.nextLine();

                if (userAction.equals("1")) {
                    System.out.println("Enter name: ");
                    String name = scanner.nextLine();
                    if (!isNameValid(name)) {
                        continue;
                    }
                    if (phonebook.containsKey(name)) {
                        System.out.println("Name already exists. update phone number? Y/N");
                        String respChoice = scanner.nextLine();
                        if (respChoice.equalsIgnoreCase("n")) {
                            continue;
                        }
                    }
                    System.out.println("Enter phone number: ");
                    String phoneNumber = scanner.nextLine();
                    if (!isPhoneNumberValid(phoneNumber)) {
                        continue;
                    }
                    phonebook.put(name, phoneNumber);
                    System.out.println("contact added to phonebook.");
                } else if (userAction.equals("2")) {
                    System.out.println("Printing phonebook -");
                    for (String name : phonebook.keySet()) {
                        System.out.println("name: " + name + ", phone number: " + phonebook.get(name));
                    }
                } else if (userAction.equals("3")) {
                    System.out.println("Enter name for search: ");
                    String name = scanner.nextLine();
                    if (phonebook.containsKey(name)) {
                        System.out.println("Contact found -");
                        System.out.println("name : " + name + ", phone number: " + phonebook.get(name));
                    } else {
                        System.out.println("No matching contact found in the phonebook.");
                    }
                } else if (userAction.equals("4")) {
                    System.out.println("Enter name to be deleted from phone book: ");
                    String name = scanner.nextLine();
                    if (phonebook.containsKey(name)) {
                        phonebook.remove(name);
                    } else {
                        System.out.println("No contact found with the entered name.");
                    }

                } else {
                    break;
                }
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number. Please enter a valid number.");
        }
    }
}
