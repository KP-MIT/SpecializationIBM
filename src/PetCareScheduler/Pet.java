package PetCareScheduler;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Pet implements Serializable {
    private final String petId;
    private static int idSerial = 1;
    private String petName;
    private String species;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDate registrationDate;
    private ArrayList<Appointment> appointments;

    public Pet(String petName, String species, int age,
               String ownerName, String contactInfo) {
        this.petId = "p"+idSerial++;
        this.petName = petName;
        this.species = species;
        this.age = age;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.registrationDate = LocalDate.now();
        this.appointments = new ArrayList<>();
    }

    public static void setIdSerial(int idSerial) {
        Pet.idSerial = idSerial;
    }

    public String getPetId() {
        return petId;
    }

    public String getPetName() {
        return petName;
    }

    public String getSpecies() {
        return species;
    }

    public int getAge() {
        return age;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setName(String petName) {
        this.petName = petName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }
}
