package dateTimeExample;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class TravelPlanner {
    /**
     * Calculate trip duration
     * standard date format "dd/MM/yyyy"
     * @param departureDate date of departure
     * @param returnDate date of return
     */
    public static long calculateTripDuration(LocalDate departureDate, LocalDate returnDate){
        return ChronoUnit.DAYS.between(departureDate, returnDate);
    }

    public static boolean validateTravelDates(LocalDate departureDate, LocalDate returnDate){
        LocalDate today = LocalDate.now();
        if (departureDate.isEqual(today) || departureDate.isAfter(today) || returnDate.isAfter(departureDate)) {
            return calculateTripDuration(departureDate, returnDate) <= 90;
        }
        return false;
    }

    public static String calculateHotelDates(LocalDate departureDate, LocalDate returnDate){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String checkIn = departureDate.format(dateFormat);
        String checkOut = returnDate.format(dateFormat);
        String stayDuration = Long.toString(calculateTripDuration(departureDate, returnDate));
        return "Hotel stay summary: check-in date: "+checkIn+", check-out date: "
                +checkOut+", total stay of"+stayDuration+" day(s).";

    }

    public static boolean tripOverlapsHoliday(LocalDate departureDate, LocalDate returnDate, LocalDate holiday){
        return holiday.equals(departureDate) || holiday.equals(returnDate)
                || holiday.isAfter(departureDate) && holiday.isBefore(returnDate);
    }

    public static LocalDate getDateFromUser(Scanner scanner){
        String rawDate = scanner.nextLine();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(rawDate,dateFormat);
    }
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)){
            System.out.println("Welcome to travel planner!");
            boolean running = true;
            while (running) {
                System.out.println("Enter the number from the following options: ");
                System.out.println("1 - Calculate trip duration");
                System.out.println("2 - Calculate hotel dates");
                System.out.println("3 - Check if trip overlaps with holidays");
                System.out.println("4- exit.");

                LocalDate departureDate;
                LocalDate returnDate;
                int input = Integer.parseInt(scanner.nextLine());

                switch (input){
                    case 1 -> {
                        System.out.println("Enter departure date(dd/MM/yyyy) : ");
                        departureDate = getDateFromUser(scanner);
                        System.out.println("Enter return date(dd/MM/yyyy) : ");
                        returnDate = getDateFromUser(scanner);
                        if (validateTravelDates(departureDate, returnDate)){
                            System.out.println("Trip duration: "+calculateTripDuration(departureDate, returnDate));
                        } else {
                            System.out.println("Invalid date, try again!");
                        }
                    }
                    case 2 -> {
                        System.out.println("Enter departure date(dd/MM/yyyy) : ");
                        departureDate = getDateFromUser(scanner);
                        System.out.println("Enter return date(dd/MM/yyyy) : ");
                        returnDate = getDateFromUser(scanner);
                        if (validateTravelDates(departureDate, returnDate)){
                            System.out.println(calculateHotelDates(departureDate, returnDate));
                        } else {
                            System.out.println("invalid dates, try again!");
                        }
                    }
                    case 3 -> {
                        System.out.println("Enter departure date(dd/MM/yyyy) : ");
                        departureDate = getDateFromUser(scanner);
                        System.out.println("Enter return date(dd/MM/yyyy) : ");
                        returnDate = getDateFromUser(scanner);
                        System.out.println("Enter date of holiday(dd/MM/yyyy) :");
                        LocalDate holiday = getDateFromUser(scanner);
                        if (tripOverlapsHoliday(departureDate, returnDate, holiday)){
                            System.out.println("Your trip overlaps with holiday.");
                        } else {
                            System.out.println("Your trip does not overlap with holiday.");
                        }
                    }
                    case 4 -> {
                        running = false;
                        System.out.println("Thank you for using travel planner");
                    }
                    default -> System.out.println("invalid option, choose again.");
                }
            }
        } catch (DateTimeParseException e){
            System.out.println("invalid date format, try again!");
        } catch (Exception e){
            System.out.println("Error occurred: "+e.getMessage());
        }
    }
}
