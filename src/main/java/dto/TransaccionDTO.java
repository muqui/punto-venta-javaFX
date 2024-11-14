/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author albert
 */
public class TransaccionDTO {

    private String name;
  
    private String income;
    private String expense;
    private String total;
    
    
      // Constructor sin argumentos
    public TransaccionDTO() {
    }


    public TransaccionDTO(String name,  String income, String expense, String total) {
        this.name = name;
      
        this.income = income;
        this.expense = expense;
        this.total = total;
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
     * @return the total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }
}
