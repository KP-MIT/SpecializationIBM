package dateTimeExample;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DateDiffCalculator {

    public static String getDiff(LocalDate d1, LocalDate d2){
        Period period = d1.until(d2);
        String diffStr = "The difference is ";
        if (period.getYears() != 0){
            diffStr += period.getYears() +" year(s) ";
        }
        if (period.getMonths() != 0){
            diffStr += period.getMonths()+" month(s) ";
        }
        if (period.getDays() != 0){
            diffStr += period.getDays()+" day(s) ";
        }
        return diffStr;
    }

    public static void main(String s[]) {
        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("The date is "+todayDate.format(dateFormat));
        Scanner scanner = new Scanner(System.in);
        try{
            System.out.println("Enter the date in dd/MM/yyyy format: ");
            String inputDate = scanner.nextLine();
            LocalDate givenDate = LocalDate.parse(inputDate, dateFormat);
            System.out.println("Entered date is: "+ givenDate.format(dateFormat));
            if (givenDate.equals(todayDate)){
                System.out.println("Both dates are same");
            } else {
                if (givenDate.isBefore(todayDate)){
                    System.out.println("The difference is: "+ getDiff(givenDate, todayDate));
                } else {
                    System.out.println("The difference is: "+ getDiff(todayDate, givenDate));
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("invalid input. Try again! \n" + e.getMessage());
        } finally {
            scanner.close();
        }




    }
}
