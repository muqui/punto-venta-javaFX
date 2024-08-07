/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.math.BigDecimal;

/**
 *
 * @author albert
 */
public class PackageContent {
        private int productId;
    private BigDecimal quantity;

    // Getters y Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

  
}
