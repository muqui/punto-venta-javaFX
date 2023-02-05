package com.albertocoronanavarro.puntoventafx;

import beans.Usuario;
import controller.VentasController;
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
    private static Usuario usuario;
    private static Stage stg;

   
   // private Usuario usuario;
    @Override
    public void start(Stage primaryStage) {
        
        try {
            stg = primaryStage;

            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            primaryStage.setTitle("Ventas");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void changeScene(String primary, Usuario usuario, ActionEvent event) throws IOException {
        
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(primary));
            Parent root = fxmlLoader.load();
            PrimaryController primaryController = fxmlLoader.getController();
            primaryController.prueba(usuario);
            
          //  this.setUsuario(usuario);
        
        stg.setTitle("" + usuario.getNombre());
         stg.getScene().setRoot(root);
         stg.setMaximized(true);
         
         this.setUsuario(usuario);

    }

   
    public static Stage getStage() {
        return stg;
    }
    
     public static Usuario getUser() {
        return getUsuario();
    }

    /**
     * @return the usuario
     */
    public static Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param aUsuario the usuario to set
     */
    public static void setUsuario(Usuario aUsuario) {
        usuario = aUsuario;
    }

   

}
