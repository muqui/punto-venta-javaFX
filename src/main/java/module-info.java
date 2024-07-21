module com.albertocoronanavarro.puntoventafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.net.http; // Agregado para HttpClient
    requires unirest.java;  // Agregado para Unirest
     requires org.json;      // Agregado para la biblioteca JSON 
     requires com.fasterxml.jackson.databind;
     
      opens beans to com.fasterxml.jackson.databind;
   
    opens com.albertocoronanavarro.puntoventafx to javafx.fxml;
    exports com.albertocoronanavarro.puntoventafx;
    
     opens controller to javafx.fxml;
    exports controller;
    
    opens conexionMYSQL to javafx.fxml;
    exports conexionMYSQL;
    
     
    exports beans;
    
    exports dto;
    
    
    
    
   
}
