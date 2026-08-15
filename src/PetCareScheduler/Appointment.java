package PetCareScheduler;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment implements Serializable {
    private String appointmentType;
    private LocalDate date;
    private LocalTime time;
    private String notes;

    public Appointment(String appointmentType, LocalDate date, LocalTime time){
        this.appointmentType = appointmentType;
        this.date = date;
        this.time = time;
    }

    public Appointment(String appointmentType, LocalDate date,
                       LocalTime time, String notes){
        this.appointmentType = appointmentType;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentType='" + appointmentType + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", notes='" + notes + '\'' +
                '}';
    }
}
