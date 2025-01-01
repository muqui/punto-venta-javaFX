/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;

/**
 *
 * @author albert
 */
public class InventoryResponseDTO {
    private String categoryName;
    private String totalInventoryCost;
    private List<ProductDTO> products;

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the totalInventoryCost
     */
    public String getTotalInventoryCost() {
        return totalInventoryCost;
    }

    /**
     * @param totalInventoryCost the totalInventoryCost to set
     */
    public void setTotalInventoryCost(String totalInventoryCost) {
        this.totalInventoryCost = totalInventoryCost;
    }

    /**
     * @return the products
     */
    public List<ProductDTO> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

  
    
}
