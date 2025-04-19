/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import javafx.scene.control.Alert;

/**
 *
 * @author albert
 */
public class AlertMessage {
    
    
    
    private AlertMessage() {
        // Constructor privado para prevenir instancias
    }
    
      public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);  // Usar el tipo de alerta proporcionado como parámetro
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
}
