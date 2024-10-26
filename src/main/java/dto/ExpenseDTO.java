/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author albert
 */
public class ExpenseDTO {

    private int id;
    private BigDecimal amount;
    private String description;
    private int expenseNamesId;
    private String date;
    private ExpenseNameDTO expenseNames;

    @Override
    public String toString() {
        return "ExpenseDTO{" + "id=" + getId() + ", amount=" + getAmount() + ", description=" + getDescription() + ", incomeNamesId=" + getExpenseNamesId() + ", date=" + getDate() + '}';
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
     * @return the expenseNamesId
     */
    public int getExpenseNamesId() {
        return expenseNamesId;
    }

    /**
     * @param expenseNamesId the expenseNamesId to set
     */
    public void setExpenseNamesId(int expenseNamesId) {
        this.expenseNamesId = expenseNamesId;
    }

    /**
     * @return the expenseNames
     */
    public ExpenseNameDTO getExpenseNames() {
        return expenseNames;
    }

    /**
     * @param expenseNames the expenseNames to set
     */
    public void setExpenseNames(ExpenseNameDTO expenseNames) {
        this.expenseNames = expenseNames;
    }

  

}
