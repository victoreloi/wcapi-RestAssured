package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util {

    public static String getCurrentDate(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter brFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(brFormat);
    }

    public static String getFutureDate(Integer days){
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(days);
        DateTimeFormatter brFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return futureDate.format(brFormat);
    }

}
