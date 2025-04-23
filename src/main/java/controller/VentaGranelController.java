/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class VentaGranelController implements Initializable {
    
    private BigDecimal price;
     private BigDecimal totalPrice;
     private BigDecimal cantidad;

    @FXML
    private TextField txtAmount;
    
        @FXML
    private TextField txtTotal;
        
           @FXML
    void onKeyTipeAmount(KeyEvent event) {
         txtTotal.setText("" + calculatePrice());
    }

    @FXML
    void onKeyTypedTotal(KeyEvent event) {
      txtAmount.setText("" + calculateQuantity());
     
    }

    
    @FXML
    void onKeyPressedTotal(KeyEvent event) {
         if (event.getCode() == KeyCode.ENTER) {
            // Tu código para manejar el evento al presionar Enter
            System.out.println("Enter key pressed");
            // Añade aquí la lógica que deseas ejecutar al presionar Enter
            setPrice(new BigDecimal(getTxtAmount().getText()));
            
            
        // Lógica para cerrar la ventana
        Stage stage = (Stage) getTxtAmount().getScene().getWindow();
        stage.close();
        }
    }


    @FXML
    void keyPressedTxtAmount(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            // Tu código para manejar el evento al presionar Enter
            System.out.println("Enter key pressed");
            // Añade aquí la lógica que deseas ejecutar al presionar Enter
            setPrice(new BigDecimal(getTxtAmount().getText()));
            
            
        // Lógica para cerrar la ventana
        Stage stage = (Stage) getTxtAmount().getScene().getWindow();
        stage.close();
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
    }

  
    /**
     * @return the txtAmount
     */
    public TextField getTxtAmount() {
        return txtAmount;
    }

    /**
     * @param txtAmount the txtAmount to set
     */
    public void setTxtAmount(TextField txtAmount) {
        this.txtAmount = txtAmount;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
           System.out.println("precio recibido: " + this.price);
    }

    /**
     * @return the txtTotal
     */
    public TextField getTxtTotal() {
        return txtTotal;
    }

    /**
     * @param txtTotal the txtTotal to set
     */
    public void setTxtTotal(TextField txtTotal) {
        this.txtTotal = txtTotal;
    }

    /**
     * @return the cantidad
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal calculatePrice(){
        setCantidad(new BigDecimal(txtAmount.getText().toString()));
        this.totalPrice = price.multiply(cantidad);
        
        return  this.totalPrice;
        
    }
    
//    public BigDecimal calculateQuantity() {
//    try {
//        BigDecimal costToPay = new BigDecimal(txtTotal.getText().toString()); // Capturas el costo total
//        BigDecimal quantity = costToPay.divide(price, RoundingMode.HALF_UP); // Divides el costo total por el precio por unidad
//        setCantidad(quantity); // Estableces la cantidad calculada
//        return quantity; // Devuelves la cantidad
//    } catch (NumberFormatException e) {
//        System.out.println("Error: El valor ingresado no es un número válido.");
//        return BigDecimal.ZERO; // Devuelves 0 si hay error
//    } catch (ArithmeticException e) {
//        System.out.println("Error: División por cero.");
//        return BigDecimal.ZERO; // Devuelves 0 si el precio por unidad es cero
//    }
//}
    
    public BigDecimal calculateQuantity() {
    try {
        BigDecimal costToPay = new BigDecimal(txtTotal.getText().toString()); // Capturas el costo total
        BigDecimal quantity = costToPay.divide(price, 2, RoundingMode.HALF_UP); // Divides con escala de 2 decimales
        setCantidad(quantity.setScale(2, RoundingMode.HALF_UP)); // Estableces la cantidad con 2 decimales
        return quantity.setScale(2, RoundingMode.HALF_UP); // Devuelves la cantidad con 2 decimales
    } catch (NumberFormatException e) {
        System.out.println("Error: El valor ingresado no es un número válido.");
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP); // Devuelves 0.00 si hay error
    } catch (ArithmeticException e) {
        System.out.println("Error: División por cero.");
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP); // Devuelves 0.00 si el precio por unidad es cero
    }
}


    /**
     * @return the totalPrice
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
