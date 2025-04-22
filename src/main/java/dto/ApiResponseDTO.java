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
public class ApiResponseDTO {
     private Object message;                            //private String error;
   // private String message;
    private String statusCode;
    // Getters y setters

     private String error;
   


    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }




    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the message
     */
    public Object getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(Object message) {
        this.message = message;
    }
}
