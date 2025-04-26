package com.albertocoronanavarro.puntoventafx;

import controller.VentasController;
import dto.UserDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;

/**
 * JavaFX App
 */
public class App extends Application {
    private static UserDTO usuario;
    private static Stage stg;

   
   // private Usuario usuario;
    @Override
    public void start(Stage primaryStage) {
        
        try {
            setStg(primaryStage);

            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            primaryStage.setTitle("Ventas");
            primaryStage.setScene(new Scene(root));
        //   primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void changeScene(String primary, UserDTO usuario, ActionEvent event) throws IOException {
        
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(primary));
            Parent root = fxmlLoader.load();
            PrimaryController primaryController = fxmlLoader.getController();
         //  primaryController.setUsuario(usuario);
            
            this.setUsuario(usuario);
    
        getStg().setTitle("" + usuario.getEmail());
     //    getStg().getScene().setRoot(root);
     
     Scene scene = new Scene(root);
getStg().setScene(scene);
           
    getStg().setResizable(true);  
     getStg().setMaximized(true);  
     getStg().show(); // <- Forzar que se muestre en estado actualizado
      getStg().setMaximized(true); 
         
      

    }

   
    public static Stage getStage() {
        return getStg();
    }

    /**
     * @return the usuario
     */
    public static UserDTO getUsuario() {
        return usuario;
    }

    /**
     * @param aUsuario the usuario to set
     */
    public static void setUsuario(UserDTO aUsuario) {
        usuario = aUsuario;
    }

    /**
     * @return the stg
     */
    public static Stage getStg() {
        return stg;
    }

    /**
     * @param aStg the stg to set
     */
    public static void setStg(Stage aStg) {
        stg = aStg;
    }
    
  

 

   

}
