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
public class ExpenseResponseDTO {
    
     private String totalAmount; 
    private List<ExpenseDTO> expenses;

    /**
     * @return the totalAmount
     */
    public String getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the expenses
     */
    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }

    /**
     * @param expenses the expenses to set
     */
    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }

    
}
