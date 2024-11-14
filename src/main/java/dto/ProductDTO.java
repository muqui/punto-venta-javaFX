/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import beans.PackageContent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author albert
 */


public class ProductDTO {

    private int id;
    private String name;
    private String description;
    private String barcode;
    private BigDecimal price;
    private double stock;
    private String imgUrl;
    private int categoryId;
    private String  howToSell;
    private double purchasePrice;
    private double wholesalePrice;
    private boolean stocktaking;
    private int minimumStock;
    private String supplier;
    private boolean isActive;
     private CategoryDTO category;
    
    private double entriy;
    private double output;
    
    private double quantity;
    
     private List<PackageContent> packageContents = new ArrayList<>();

    @Override
    public String toString() {
        return "ProductDTO{" + "id=" + getId() + ", name=" + getName() + ", description=" + getDescription() + ", barcode=" + getBarcode() + ", price=" + getPrice() + ", stock=" + getStock() + ", imgUrl=" + getImgUrl() + ", categoryId=" + getCategoryId() + ", howToSell=" + getHowToSell() + ", purchasePrice=" + getPurchasePrice() + ", wholesalePrice=" + getWholesalePrice() + ", stocktaking=" + isStocktaking() + ", minimumStock=" + getMinimumStock() + ", entriy=" + getEntriy() + ", output=" + getOutput() + '}';
    }

   

    public ProductDTO(){
        
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
    }

   

    /**
     * @return the imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl the imgUrl to set
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the howToSell
     */
    public String getHowToSell() {
        return howToSell;
    }

    /**
     * @param howToSell the howToSell to set
     */
    public void setHowToSell(String howToSell) {
        this.howToSell = howToSell;
    }

    /**
     * @return the purchasePrice
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * @param purchasePrice the purchasePrice to set
     */
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * @return the wholesalePrice
     */
    public double getWholesalePrice() {
        return wholesalePrice;
    }

    /**
     * @param wholesalePrice the wholesalePrice to set
     */
    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    /**
     * @return the stocktaking
     */
    public boolean isStocktaking() {
        return stocktaking;
    }

    /**
     * @param stocktaking the stocktaking to set
     */
    public void setStocktaking(boolean stocktaking) {
        this.stocktaking = stocktaking;
    }

    /**
     * @return the minimumStock
     */
    public int getMinimumStock() {
        return minimumStock;
    }

    /**
     * @param minimumStock the minimumStock to set
     */
    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    /**
     * @return the entriy
     */
    public double getEntriy() {
        return entriy;
    }

    /**
     * @param entriy the entriy to set
     */
    public void setEntriy(double entriy) {
        this.entriy = entriy;
    }

    /**
     * @return the output
     */
    public double getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(double output) {
        this.output = output;
    }

    /**
     * @return the supplier
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * @return the packageContents
     */
    public List<PackageContent> getPackageContents() {
        return packageContents;
    }

    /**
     * @param packageContents the packageContents to set
     */
    public void setPackageContents(List<PackageContent> packageContents) {
        this.packageContents = packageContents;
    }

    /**
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the category
     */
    public CategoryDTO getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    /**
     * @return the isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the stock
     */
    public double getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(double stock) {
        this.stock = stock;
    }


   
}
