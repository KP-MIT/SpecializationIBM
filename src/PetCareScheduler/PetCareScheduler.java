package PetCareScheduler;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PetCareScheduler {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String,Pet> pets = new HashMap<>();

    private static void storeDataInFile() {
        try{
            ObjectOutputStream out =  new ObjectOutputStream(
                    new FileOutputStream("petCare.ser")
            );
            out.writeObject(pets);
            System.out.println("Data successfully stored.");
        } catch (IOException e) {
            System.out.println("Error saving data in file: "+e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadDataFromFile(){
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("petCare.ser"))){
            {
                pets = (Map<String, Pet>) in.readObject();
                System.out.println("Pets data loaded.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved data file found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: "+e.getMessage());
        }
    }
    private static void updatePetIdSerial() {

        int maxId = 0;

        for (String petId : pets.keySet()) {

            int idNumber = Integer.parseInt(
                    petId.substring(1)
            );

            if (idNumber > maxId) {
                maxId = idNumber;
            }
        }

        Pet.setIdSerial(maxId + 1);
    }

    private static void addPet(String id, Pet newPet){
        if(pets.containsKey(id)){
            throw new IllegalArgumentException("A pet with the id "+id+" already exists.");
        }
        pets.put(id,newPet);
        System.out.println("New pet "+ newPet.getPetName() +" added successfully");
    }
    private static void addAppointment(String id, Appointment appointment){
        if (pets.get(id) == null){
            throw new IllegalArgumentException("No pet found with given id");
        }
        Pet pet = pets.get(id);
        if (!appointment.getDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Invalid appointment date, must be in the future.");
        }
        String type = appointment.getAppointmentType();
        if (type.equalsIgnoreCase("vet visit")
                || type.equalsIgnoreCase("vaccination")
                || type.equals("grooming")){
            pet.addAppointment(appointment);
            System.out.println(
                    "Appointment for: " + pet.getPetName()
                            + ", Owner name - " + pet.getOwnerName()
                            + " on " + appointment.getDate()
                            + " at " + appointment.getTime()
                            + " scheduled successfully."
            );
        } else {
            throw new IllegalArgumentException("Appointment type must be " +
                    "visit, vaccination, or grooming");
        }
    }

    private static void generateReports(Map<String, Pet> pets){
        System.out.println("===   Generating report   ===" +
                "\nPets with upcoming appointment in next week: ");
        for (Pet pet : pets.values()){
            for (Appointment apt : pet.getAppointments()){
                if (ChronoUnit.DAYS.between(LocalDate.now(), apt.getDate()) <= 7){
                    System.out.println("Pet name: "+pet.getPetName()+
                            "(id: "+pet.getPetId()+")" +
                            ", appointment date: "+apt.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+
                            "at: "+apt.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }
        }
        System.out.println("Pets with overdue vet visit:");
        for (Pet pet : pets.values()){
            boolean overdue = true;
            boolean found = false;
            for (Appointment apt: pet.getAppointments()){
                if (apt.getAppointmentType().equalsIgnoreCase("vet visit")
                || ChronoUnit.MONTHS.between(LocalDate.now(),apt.getDate()) < 6){
                    overdue = false;
                } else {
                    if (overdue) {
                        found = true;
                        System.out.println("Pet name: "+pet.getPetName()+
                                " (id: "+pet.getPetId()+"), "+
                                "4Last appointment: "+apt.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    }
                }
            }
            if (!found){
                System.out.println("No overdue appointments.");
            }
        }
    }
    public static void main(String[] args){
        loadDataFromFile();
        updatePetIdSerial();
        System.out.println("Welcome to Pet Care Scheduler!");
        boolean running = true;
        try{
            while(running){
                System.out.println("""
                    Choose an option from the following menu:
                    1 - Register pets
                    2 - Schedule appointments
                    3 - Store data
                    4 - Display records
                    5 - Generate reports
                    6 - exit""");
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice){
                    case 1 -> {
                        while (true){
                            try{
                                System.out.println("Enter pet details for registration: " +
                                        "\nPet name: ");
                                String petName = scanner.nextLine().trim();
                                if (petName.strip().equalsIgnoreCase("")){
                                    throw new IllegalArgumentException("Pet name cannot be blank");
                                }
                                System.out.println("Enter breed/species: ");
                                String species = scanner.nextLine().trim();
                                if (species.strip().equalsIgnoreCase("")){
                                    throw new IllegalArgumentException("Breed/Species cannot be blank");
                                }
                                System.out.println("Enter age of the pet: ");
                                int age = Integer.parseInt(scanner.nextLine());
                                if (age<0) throw new IllegalArgumentException("Age cannot be less than zero.");
                                System.out.println("Enter owner's name: ");
                                String ownerName = scanner.nextLine().trim();
                                if (ownerName.strip().equalsIgnoreCase("")){
                                    throw new IllegalArgumentException("Owner's name cannot be empty.");
                                }
                                System.out.println("Enter contact info: ");
                                String contactInfo = scanner.nextLine().trim();
                                if (contactInfo.strip().equalsIgnoreCase("")){
                                    throw new IllegalArgumentException("Contact info cannot be blank");
                                }
                                Pet petToRegister = new Pet(petName, species, age, ownerName, contactInfo);
                                String id = petToRegister.getPetId();
                                addPet(id, petToRegister);
                                break;
                            } catch (NumberFormatException e){
                                System.out.println("invalid input, enter a positive number.");
                            } catch (IllegalArgumentException e){
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    case 2 -> {
                        while(true){
                            try{
                                System.out.println("Enter type of appointment(vet visit/ vaccination/ grooming): ");
                                String aptType = scanner.nextLine().trim();
                                if (aptType.strip().equalsIgnoreCase("")){
                                    throw new IllegalArgumentException("Appointment type cannot be blank.");
                                }
                                System.out.println("Enter date of appointment(dd/MM/yyyy): ");
                                String rawDate = scanner.nextLine().trim();
                                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                LocalDate appDate = LocalDate.parse(rawDate, dateFormat);
                                System.out.println("Enter desired time of appointment(HH:mm)");
                                String rawTime = scanner.nextLine();
                                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                                LocalTime appTime = LocalTime.parse(rawTime, timeFormat);
                                System.out.println("Do you want to provide more info for appointment?(Y/N) : ");
                                String opt = scanner.nextLine().trim();
                                Appointment apt;
                                if (opt.equalsIgnoreCase("n")){
                                    apt = new Appointment(aptType, appDate, appTime);
                                } else {
                                    System.out.println("Enter notes:");
                                    String notes = scanner.nextLine();
                                    apt =  new Appointment(aptType, appDate, appTime, notes);
                                }
                                System.out.println("Enter your pet's registration id: ");
                                String id = scanner.nextLine().trim();
                                if (id.strip().equalsIgnoreCase("")){
                                    throw new IllegalArgumentException("Pet id cannot be empty.");
                                }
                                addAppointment(id, apt);
                                break;
                            } catch (IllegalArgumentException e){
                                System.out.println(e.getMessage());
                            } catch (DateTimeParseException e){
                                System.out.println("Invalid date/time format, try again!");
                            }
                        }
                    }
                    case 3 -> {
                        System.out.println("Writing data about pets and appointments to a file.");
                        storeDataInFile();
                    }
                    case 4 -> {
                        System.out.println("Select an option from the following to display records:" +
                                "\n1 - All registered pets" +
                                "\n2 - All appointments for a specific pet" +
                                "\n3 - Upcoming appointments for all pets" +
                                "\n4 - Past appointment history for each pet");
                        try{
                            int opt = Integer.parseInt(scanner.nextLine());
                            switch (opt){
                                case 1 -> {
                                    System.out.println("Displaying all registered pets: ");
                                    if (pets == null || pets.isEmpty()){
                                        System.out.println("No registered pets in the system.");
                                    } else {
                                        for (Pet pet : pets.values()){
                                            System.out.println("Pet name: "+ pet.getPetName()+
                                                    "\nid: "+ pet.getPetId()+
                                                    "\nbreed: "+pet.getSpecies()+
                                                    "\nage: "+String.valueOf(pet.getAge())+
                                                    "\nowner name: "+pet.getOwnerName()+
                                                    "\ncontact: "+pet.getContactInfo()+
                                                    "\nregistration date: "+pet.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                        }
                                    }
                                }
                                case 2 -> {
                                    System.out.println("Enter pet's registration id to see all appointments: ");
                                    String petId = scanner.nextLine().trim();
                                    if (pets.get(petId) == null){
                                        System.out.println("No matching pet found.");
                                    } else {
                                        System.out.println(pets.get(petId).getAppointments());
                                    }
                                }
                                case 3 -> {
                                    System.out.println("Displaying all upcoming appointments for all pets: ");
                                    for ( Pet pet : pets.values()){
                                        System.out.println("Upcoming appointment for "+pet.getPetName()+
                                                " (id: "+pet.getPetId()+") - ");
                                        boolean none = true;
                                        for(Appointment apt : pet.getAppointments()){
                                            if (apt.getDate().isAfter(LocalDate.now())){
                                                System.out.println(apt.toString());
                                                none = false;
                                            }
                                        }
                                        if (none) System.out.println("No upcoming appointments");
                                    }
                                }
                                case 4 -> {
                                    System.out.println("Displaying past appointments for each pet: ");
                                    for (Pet pet : pets.values()){
                                        System.out.println("Previous appointments of "+pet.getPetName()+
                                                " (id: "+pet.getPetId()+"): ");
                                        boolean none = true;
                                        for (Appointment apt : pet.getAppointments()){
                                            if (apt.getDate().isBefore(LocalDate.now())){
                                                System.out.println(apt.toString());
                                                none = false;
                                            }
                                        }
                                        if (none) System.out.println("No previous appointments.");
                                    }
                                }
                                default -> System.out.println("invalid choice");
                            }
                        } catch (IllegalArgumentException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    case 5 -> {
                        generateReports(pets);
                    }
                    case 6 -> {
                        System.out.println("Thanks for using Pet Care Scheduler!");
                        running = false;

                    }
                    default -> System.out.println("Invalid choice, try again!");
                }
            }

        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
