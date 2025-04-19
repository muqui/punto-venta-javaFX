/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderServiceDTO {


    private String  status;

    
    private int id;
   
  
    private String folio;
    
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date date;
    
    @JsonIgnore
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
     private Date updatedAt;

    private String service;

    private String client;

    private String cellphone;

    @JsonProperty("repair_cost")
    private Double repairCost;

    private Double paid;
    
     private Double replacementCost;
     
      private Double profit;

    private Double left;

    private String note;

    private String email;

    private String brand;

    private String model;

    private String issue;

    @JsonProperty("received_condition")
    private String receivedCondition;

    @JsonProperty("password_cell_phone")
    private String passwordCellPhone;

    private String imei;
    
     private ArrayList <SparePartDTO> spareParts;

    public OrderServiceDTO() {
    }

    @Override
    public String toString() {
        return "OrderServiceDTO{" + "id=" + getId() + ", folio=" + getFolio() + ", date=" + getDate() + ", service=" + getService() + ", client=" + getClient() + ", cellphone=" + getCellphone() + ", repairCost=" + getRepairCost() + ", paid=" + getPaid() + ", left=" + getLeft() + ", note=" + getNote() + ", email=" + getEmail() + ", brand=" + getBrand() + ", model=" + getModel() + ", issue=" + getIssue() + ", receivedCondition=" + getReceivedCondition() + ", passwordCellPhone=" + getPasswordCellPhone() + ", imei=" + getImei() + '}';
    }
    
    

    public OrderServiceDTO(String service, String client, String cellphone, Double repairCost, Double paid, Double left, String note, String email, String brand, String model, String issue, String receivedCondition, String passwordCellPhone, String imei, int id, String folio, Date date) {
        this.id = id;
        this.folio = folio;
        this.date = date;
        this.service = service;
        this.client = client;
        this.cellphone = cellphone;
        this.repairCost = repairCost;
        this.paid = paid;
        this.left = left;
        this.note = note;
        this.email = email;
        this.brand = brand;
        this.model = model;
        this.issue = issue;
        this.receivedCondition = receivedCondition;
        this.passwordCellPhone = passwordCellPhone;
        this.imei = imei;
    }

    public OrderServiceDTO(String service, String client, String cellphone, Double repairCost, Double paid, Double left, String note, String email, String brand, String model, String issue, String receivedCondition, String passwordCellPhone, String imei) {

        this.service = service;
        this.client = client;
        this.cellphone = cellphone;
        this.repairCost = repairCost;
        this.paid = paid;
        this.left = left;
        this.note = note;
        this.email = email;
        this.brand = brand;
        this.model = model;
        this.issue = issue;
        this.receivedCondition = receivedCondition;
        this.passwordCellPhone = passwordCellPhone;
        this.imei = imei;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Double getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(Double repairCost) {
        this.repairCost = repairCost;
    }

    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getReceivedCondition() {
        return receivedCondition;
    }

    public void setReceivedCondition(String receivedCondition) {
        this.receivedCondition = receivedCondition;
    }

    public String getPasswordCellPhone() {
        return passwordCellPhone;
    }

    public void setPasswordCellPhone(String passwordCellPhone) {
        this.passwordCellPhone = passwordCellPhone;
    }

    /**
     * @return the imei
     */
    public String getImei() {
        return imei;
    }

    /**
     * @param imei the imei to set
     */
    public void setImei(String imei) {
        this.imei = imei;
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
     * @return the folio
     */
    public String getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
        /**
     * @return the replacementCost
     */
    public Double getReplacementCost() {
        return replacementCost;
    }

    /**
     * @param replacementCost the replacementCost to set
     */
    public void setReplacementCost(Double replacementCost) {
        this.replacementCost = replacementCost;
    }

    /**
     * @return the profit
     */
    public Double getProfit() {
        return profit;
    }

    /**
     * @param profit the profit to set
     */
    public void setProfit(Double profit) {
        this.profit = profit;
    }

    /**
     * @return the spareParts
     */
    public ArrayList <SparePartDTO> getSpareParts() {
        return spareParts;
    }

    /**
     * @param spareParts the spareParts to set
     */
    public void setSpareParts(ArrayList <SparePartDTO> spareParts) {
        this.spareParts = spareParts;
    }

    /**
     * @return the updatedAt
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

   

}
