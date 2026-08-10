package finalProject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MoodTracker {

    public static boolean isMoodValid(Mood moodToAdd, ArrayList<Mood> moods){
        try{
            for (Mood mood : moods){
                if (mood.equals(moodToAdd)){
                    throw new InvalidMoodException("Invalid! Mood already exists");
                } else {
                    if (mood.getTime().equals(moodToAdd.getTime()) &&
                            mood.getDate().isEqual(moodToAdd.getDate())){
                        throw new InvalidMoodException("Invalid! Mood for given date and time already exists");
                    }
                }
            }
        } catch (InvalidMoodException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public static void main(String[] args){
        ArrayList<Mood> moods = new ArrayList<>();
        LocalDate inputDate;
        LocalTime inputTime;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        while(running) {
            try {
                System.out.println("Welcome to Mood Tracker!" +
                        "\n Please choose from the following options: " +
                        "\n a - add a mood" +
                        "\n d - delete a mood" +
                        "\n e - edit a mood" +
                        "\n s - search a mood" +
                        "\n M - to get all moods" +
                        "\n w - writing on to a file" +
                        "\n exit - to exit the application");

                String userInput = scanner.nextLine().toLowerCase();
                switch (userInput) {
                    case "a" -> {
                        Mood moodToAdd = null;
                        System.out.println("Enter mood name: ");
                        String moodName = scanner.nextLine().trim();
                        if (moodName.isBlank()) {
                            throw new InvalidMoodException("Mood name can't be blank.");
                        } else {
                            System.out.println("Do you wish to add mood for current day? (y/n)");
                            String choice = scanner.nextLine().trim();
                            if (choice.equalsIgnoreCase("n")) {
                                System.out.println("Enter date(dd/MM/yyyy) : ");
                                String rawDate = scanner.nextLine().trim();
                                inputDate = LocalDate.parse(rawDate, dateFormat);
                                System.out.println("Enter time(HH:mm) : ");
                                String rawTime = scanner.nextLine().trim();
                                inputTime = LocalTime.parse(rawTime, timeFormat);
                                System.out.println("Enter notes for the mood: ");
                                String inputNotes = scanner.nextLine();
                                if (inputNotes.strip().equalsIgnoreCase("")) {
                                    moodToAdd = new Mood(moodName, inputDate, inputTime);
                                } else {
                                    moodToAdd = new Mood(moodName, inputDate, inputTime, inputNotes);
                                }
                            } else {
                                if (choice.equalsIgnoreCase("y")) {
                                    System.out.println("Enter notes for the mood: ");
                                    String inputNotes = scanner.nextLine();
                                    if (inputNotes.strip().equalsIgnoreCase("")) {
                                        moodToAdd = new Mood(moodName);
                                    } else {
                                        moodToAdd = new Mood(moodName, inputNotes);
                                    }
                                } else {
                                    System.out.println("invalid choice, try again! (y/n) ");
                                }
                            }
                        }
                        if (isMoodValid(moodToAdd, moods) && moodToAdd != null) {
                            moods.add(moodToAdd);
                            System.out.println("Mood successfully added");
                        }
                    }
                    case "d" -> {
                        System.out.println("Delete mood menu:" +
                                "\n 1 - Delete all mood from a date" +
                                "\n 2 - Delete entry by name, date and time");
                        int choice = Integer.parseInt(scanner.nextLine().trim());
                        switch (choice) {
                            case 1 -> {
                                System.out.println("Enter date(dd/MM/yyyy) for deletion: ");
                                String rawDate = scanner.nextLine();
                                inputDate = LocalDate.parse(rawDate, dateFormat);
                                boolean deleted = false;
                                for (Mood mood : moods) {
                                    if (mood.getDate().isEqual(inputDate)) {
                                        moods.remove(mood);
                                        deleted = true;
                                    }
                                }
                                if (deleted) {
                                    System.out.println("All records from the given date have been deleted");
                                } else {
                                    System.out.println("No records found from given date");
                                }
                            }
                            case 2 -> {
                                System.out.println("Enter the name of mood to be deleted: ");
                                String moodName = scanner.nextLine().trim();
                                System.out.println("Enter the date(dd/MM/yyyy) of mood to be deleted: ");
                                String rawDate = scanner.nextLine();
                                System.out.println("Enter the time(HH:mm) of mood to be deleted: ");
                                String rawTime = scanner.nextLine();
                                inputDate = LocalDate.parse(rawDate, dateFormat);
                                inputTime = LocalTime.parse(rawTime, timeFormat);
                                Mood moodToDelete = new Mood(moodName, inputDate, inputTime);
                                boolean deleted = false;
                                for (Mood mood : moods) {
                                    if (mood.equals(moodToDelete)) {
                                        moods.remove(mood);
                                        deleted = true;
                                    }
                                }
                                if (deleted) {
                                    System.out.println("Mood has been deleted");
                                } else {
                                    System.out.println("No mood found with given details");
                                }
                            }
                            default -> System.out.println("Invalid choice, try again.");
                        }
                    }
                    case "e" -> {
                        System.out.println("Edit notes for a mood:" +
                                "\n Enter mood name:");
                        String moodName = scanner.nextLine().trim();
                        System.out.println("Enter date(dd/MM/yyyy): ");
                        String rawDate = scanner.nextLine();
                        System.out.println("Enter time(HH:mm): ");
                        String rawTime = scanner.nextLine();
                        inputDate = LocalDate.parse(rawDate, dateFormat);
                        inputTime = LocalTime.parse(rawTime, timeFormat);
                        Mood moodToEdit = new Mood(moodName, inputDate, inputTime);
                        boolean edited = false;
                        for (Mood mood : moods) {
                            if (mood.equals(moodToEdit)) {
                                System.out.println("Current notes: \n" + mood.getNotes() +
                                        "\n Edit notes from the mood now: ");
                                String editedNotes = scanner.nextLine();
                                if (editedNotes.strip().equalsIgnoreCase("")) {
                                    System.out.println("No notes entered");
                                } else {
                                    mood.setNotes(editedNotes);
                                    edited = true;
                                }
                            }
                        }
                        if (edited) {
                            System.out.println("Notes edited successfully");
                        } else {
                            System.out.println("No matching mood found");
                        }
                    }
                    case "s" -> {
                        System.out.println("Search Moods menu:" +
                                "\n 1 - search all records by a date" +
                                "\n 2- search a record by name, date and time");
                        int choice = Integer.parseInt(scanner.nextLine());
                        switch (choice) {
                            case 1 -> {
                                System.out.println("Enter the date of records to be searched: ");
                                String rawDate = scanner.nextLine();
                                inputDate = LocalDate.parse(rawDate, dateFormat);
                                ArrayList<Mood> moodsFound = new ArrayList<>();
                                for (Mood mood : moods) {
                                    if (mood.getDate().isEqual(inputDate)) {
                                        moodsFound.add(mood);
                                    }
                                }
                                if (moodsFound.isEmpty()) {
                                    System.out.println("No records found from the given date.");
                                } else {
                                    for (Mood mood : moodsFound) {
                                        System.out.println(mood);
                                    }
                                }
                            }
                            case 2 -> {
                                System.out.println("Enter name of the mood to be searched: ");
                                String moodName = scanner.nextLine().trim();
                                System.out.println("Enter date(dd/MM/yyyy): ");
                                String rawDate = scanner.nextLine();
                                System.out.println("Enter time(HH:mm): ");
                                String rawTime = scanner.nextLine();
                                inputDate = LocalDate.parse(rawDate, dateFormat);
                                inputTime = LocalTime.parse(rawTime, timeFormat);
                                Mood moodToFind = new Mood(moodName, inputDate, inputTime);
                                boolean found = false;
                                for (Mood mood : moods) {
                                    if (mood.equals(moodToFind)) {
                                        System.out.println("Mood Found: \n" + mood);
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    System.out.println("No matching record found");
                                }
                            }
                            default -> System.out.println("Invalid choice, try again!");
                        }
                    }
                    case "m" -> {
                        System.out.println("Getting all moods: ");
                        if (moods.isEmpty()) {
                            System.out.println("No moods present in the list");
                        } else {
                            for (Mood mood : moods) {
                                System.out.println(mood);
                            }
                        }
                    }
                    case "w" -> {
                        try (PrintWriter writer = new PrintWriter(new FileWriter("Moods.txt"))) {
                            for (Mood mood : moods) {
                                writer.println(mood + "\n");
                            }
                        } catch (IOException e) {
                            System.out.println("Error writing file: " + e.getMessage());
                        }
                    }
                    case "exit" -> {
                        System.out.println("Thank you for using Mood Tracker.");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice, try again!");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time format, try again!" + e.getMessage());
            } catch (InvalidMoodException e) {
                System.out.println(e.getMessage() + " Try again!");
            }
        }
        scanner.close();
    }
}
