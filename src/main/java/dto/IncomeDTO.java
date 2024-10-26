/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author albert
 */
public class IncomeDTO {
    private int id;
    private BigDecimal amount;
    private String description;
    private int incomeNamesId;
    private String date;
    private IncomeNameDTO incomeNames;

    @Override
    public String toString() {
        return "IncomeDTO{" + "id=" + id + ", amount=" + amount + ", description=" + description + ", incomeNamesId=" + getIncomeNamesId() + ", date=" + date + '}';
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
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * @return the incomeNamesId
     */
    public int getIncomeNamesId() {
        return incomeNamesId;
    }

    /**
     * @param incomeNamesId the incomeNamesId to set
     */
    public void setIncomeNamesId(int incomeNamesId) {
        this.incomeNamesId = incomeNamesId;
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
     * @return the incomeNames
     */
    public IncomeNameDTO getIncomeNames() {
        return incomeNames;
    }

    /**
     * @param incomeNames the incomeNames to set
     */
    public void setIncomeNames(IncomeNameDTO incomeNames) {
        this.incomeNames = incomeNames;
    }

   
    
}
