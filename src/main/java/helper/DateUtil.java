package helper;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author albert
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String getTodayDateString() {
        // Obtener la fecha actual
        LocalDate today = LocalDate.now();
        
        // Formatear la fecha al formato "YYYY-MM-DD"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }

  
}
