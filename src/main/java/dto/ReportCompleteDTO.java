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
public class ReportCompleteDTO {
   private String expense;
   private String income;
   private String profit;
   private List<TransaccionDTO> reports;

    public ReportCompleteDTO() {
    }

    /**
     * @return the expense
     */
    public String getExpense() {
        return expense;
    }

    /**
     * @param expense the expense to set
     */
    public void setExpense(String expense) {
        this.expense = expense;
    }

    /**
     * @return the income
     */
    public String getIncome() {
        return income;
    }

    /**
     * @param income the income to set
     */
    public void setIncome(String income) {
        this.income = income;
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

    /**
     * @return the reports
     */
    public List<TransaccionDTO> getReports() {
        return reports;
    }

    /**
     * @param reports the reports to set
     */
    public void setReports(List<TransaccionDTO> reports) {
        this.reports = reports;
    }
    
}
