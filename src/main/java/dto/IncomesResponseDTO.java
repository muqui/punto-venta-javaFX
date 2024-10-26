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
public class IncomesResponseDTO {

    private String totalAmount; 
    private List<IncomeDTO> incomes;

 
    /**
     * @return the incomes
     */
    public List<IncomeDTO> getIncomes() {
        return incomes;
    }

    /**
     * @param incomes the incomes to set
     */
    public void setIncomes(List<IncomeDTO> incomes) {
        this.incomes = incomes;
    }

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

}
