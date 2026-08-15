package EcoPointTracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EcoPointRecyclingTracker {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Household> households = new HashMap<>();

    private static void generateReports(Map<String, Household> households){
        if(households.isEmpty()){
            System.out.println("No household registered");
            return;
        }

        Household top = null;
        for (Household h : households.values()){
            if (top == null || h.getTotalPoints() > top.getTotalPoints()){
                top =h;
            }
        }
        System.out.println("Household with highest points: ");
        System.out.println("ID: "+top.getId()+
                "\nName: "+top.getName()+
                "\nPoints: "+top.getTotalPoints());

        double totalWeight = 0.0;
        for (Household h : households.values()){
            totalWeight += h.getTotalWeight();
        }
        System.out.println("Total community recycling weight: "+totalWeight+" kg");
    }

    private static void saveHouseholdToFile() {
        try{
            ObjectOutputStream out =  new ObjectOutputStream(
                    new FileOutputStream("household.ser")
            );
            out.writeObject(households);
        } catch (IOException e) {
            System.out.println("Error saving data to file: "+e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadHouseholdsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("household.ser"))){
            {
                households = (Map<String, Household>) in.readObject();
                System.out.println("Household data loaded.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved data file found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: "+e.getMessage());
        }
    }

    public static void main(String[] args){
        loadHouseholdsFromFile();
        boolean running = true;
        while(running){
            System.out.println("\n===   Eco-Points Recycling Tracker   ===");
            System.out.println("1. Register Household");
            System.out.println("2. Log Recycling Event");
            System.out.println("3. Display Households");
            System.out.println("4. Display Household Recycling Events");
            System.out.println("5. Generate Reports");
            System.out.println("6. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice){
                case 1 -> {
                    System.out.println("Enter household id: ");
                    String id = scanner.nextLine().trim();
                    System.out.println("Enter name of the household: ");
                    String name = scanner.nextLine().trim();
                    System.out.println("Enter address: ");
                    String address = scanner.nextLine().trim();
                    Household householdToRegister = new Household(id, name, address);
                    households.put(id, householdToRegister);
                    System.out.println("Household registered!");
                }
                case 2 -> {
                    System.out.println("Enter household id to log recycling event: ");
                    String id = scanner.nextLine().trim();
                    Household household = households.get(id);
                    if (household ==  null){
                        System.out.println("Invalid household id, try again!");
                        return;
                    }
                    System.out.println("Enter material type: ");
                    String materialType = scanner.nextLine().trim();
                    double weight =0.0;
                    while(true){
                        try{
                            System.out.println("Enter weight (in Kg): ");
                            weight = Double.parseDouble(scanner.nextLine().trim());
                            if (weight <= 0) throw new IllegalArgumentException();
                            break;
                        } catch (NumberFormatException e){
                            System.out.println("Invalid weight, must be a positive number.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid weight, must be a positive number."+e.getMessage());
                        }
                    }
                    RecyclingEvent event = new RecyclingEvent(materialType, weight);
                    household.addEvent(event);
                    households.put(id, household);
                    System.out.println("Recycling event logged! Points earned: " + event.getEcoPoints());
                }
                case 3 -> {
                    if (households.isEmpty()){
                        System.out.println("No households registered yet!");
                    }
                    System.out.println("Displaying households: ");
                    for (Household household : households.values()){
                        System.out.println("ID: " + household.getId()+
                                "\nName: "+household.getName()+
                                "\nAddress: "+household.getAddress()+
                                "\njoined: "+household.getJoinDate());
                    }
                }
                case 4 -> {
                    System.out.println("Enter household id: ");
                    String id = scanner.nextLine().trim();
                    Household household = households.get(id);
                    if (household == null){
                        System.out.println("invalid household id, try again!");
                        return;
                    }
                    System.out.println("Displaying recycling events for "+household.getName()+": ");
                    if (household.getEvents() == null){
                        System.out.println("No logged recycling event for the household");
                    } else {
                        for (RecyclingEvent event: household.getEvents()){
                            System.out.println(event.toString());
                        }
                        System.out.println("Total weight: "+household.getTotalWeight());
                        System.out.println("Total Points: "+household.getTotalPoints());
                    }
                }
                case 5 -> {
                    generateReports(households);
                }
                case 6 -> {
                    saveHouseholdToFile();
                    System.out.println("Thanks for using Eco Points Recycling Tracker!");
                    running = false;
                }
                default -> System.out.println("Invalid choice, try again!");
            }


        }
    }
}
