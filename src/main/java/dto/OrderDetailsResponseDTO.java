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
public class OrderDetailsResponseDTO {

    private List<OrderDetailDTO> orderDetails;
    private String totalPrice;
    private String totalPurchasePrice;
    private String profit;

    // Getters and setters
    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the totalPurchasePrice
     */
    public String getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    /**
     * @param totalPurchasePrice the totalPurchasePrice to set
     */
    public void setTotalPurchasePrice(String totalPurchasePrice) {
        this.totalPurchasePrice = totalPurchasePrice;
    }

    /**
     * @return the profit
     */
    public String getProfit() {
        return profit;
    }

    /**
     * @param profit the profit to set
     */
    public void setProfit(String profit) {
        this.profit = profit;
    }
}
