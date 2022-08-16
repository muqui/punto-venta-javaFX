module com.albertocoronanavarro.puntoventafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    
    opens com.albertocoronanavarro.puntoventafx to javafx.fxml;
    exports com.albertocoronanavarro.puntoventafx;
    
     opens controller to javafx.fxml;
    exports controller;
    
    opens conexionMYSQL to javafx.fxml;
    exports conexionMYSQL;
    
     opens beans to javafx.fxml;
    exports beans;
    
    
    
    
   
}
