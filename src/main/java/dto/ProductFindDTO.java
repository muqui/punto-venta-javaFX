/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.control.Button;

/**
 *
 * @author albert
 */
public class ProductFindDTO extends ProductDTO{
    @JsonIgnore
     private transient Button button; // Transient para evitar la serialización del botón

    
    public ProductFindDTO() {
      this.button = new Button("Add");
    }

    /**
     * @return the button
     */
    public Button getButton() {
        return button;
    }

    /**
     * @param button the button to set
     */
    public void setButton(Button button) {
        this.button = button;
    }

  
  

    
}
