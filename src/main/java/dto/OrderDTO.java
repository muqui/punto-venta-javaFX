/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrderDTO {

    
    private int id;
    private String date;
    private List<OrderDetailDTO> orderDetails;
    private UserDTO user;

    public OrderDTO() {

    }
    // Getters y Setters
    // Constructor

    public ObservableList<OrderDetailDTO> getOrderDetails() {
        // Convert List to ObservableList when called
        return FXCollections.observableArrayList(orderDetails);
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
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the orderDetails
     */
    /**
     * @param orderDetails the orderDetails to set
     */
    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    /**
     * @return the user
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }
    
   

}
