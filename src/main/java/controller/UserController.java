/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.UserApi;
import dto.OrderServiceDTO;
import dto.UserDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class UserController implements Initializable {
    private ToggleGroup paperOrderServiceSizeGroup;
    UserApi userApi = new UserApi();
    @FXML
    private Button btnCreatedUSer;

    @FXML
    private Button btnUser;

    @FXML
    private TextField txtUserName;

    @FXML
    private TableView<UserDTO> tableUsers;
    @FXML
    private RadioButton RadioButtonOrderService58;

    @FXML
    private RadioButton RadioButtonOrderService80;

    @FXML
    private RadioButton RadioButtonOrderServiceLetter;

    @FXML
    void onActionCreatedUser(ActionEvent event) {
        OpenModalCreateUser(null);
    }

    @FXML
    void onActionSearchUser(ActionEvent event) {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // userApi.fillChoiceBoxUser();
        initializeTableColumns();
        
            paperOrderServiceSizeGroup = new ToggleGroup();

    RadioButtonOrderService58.setToggleGroup(paperOrderServiceSizeGroup);
    RadioButtonOrderService80.setToggleGroup(paperOrderServiceSizeGroup);
    RadioButtonOrderServiceLetter.setToggleGroup(paperOrderServiceSizeGroup);

       // Leer el valor guardado
    String savedPrinter = loadPrinterNameFromProperties();

    if (savedPrinter != null) {
        switch (savedPrinter) {
            case "58mm":
                RadioButtonOrderService58.setSelected(true);
                break;
            case "80mm":
                RadioButtonOrderService80.setSelected(true);
                break;
            case "Letter":
                RadioButtonOrderServiceLetter.setSelected(true);
                break;
            default:
                RadioButtonOrderServiceLetter.setSelected(true); // valor por defecto si no coincide
        }
    } else {
        RadioButtonOrderServiceLetter.setSelected(true); // valor por defecto si no hay archivo
    }
    paperOrderServiceSizeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
    if (newToggle != null) {
        RadioButton selectedRadio = (RadioButton) newToggle;
        String printerName = selectedRadio.getText(); // o usa .getId() si prefieres el ID

        savePrinterNameToProperties(printerName);
    }
});

    }

    private void initializeTableColumns() {

        tableUsers.getColumns().clear(); // Limpiar las columnas de la tabla antes de agregar nuevas

        TableColumn<UserDTO, String> columnName = new TableColumn<>("Nombre");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<UserDTO, String> columnEmail = new TableColumn<>("Correo");
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<UserDTO, String> columnLevel = new TableColumn<>("Tipo");
        columnLevel.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));

        TableColumn<UserDTO, Void> actionColumn = new TableColumn<>("Acción");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Ver");

            {
                button.setOnAction(event -> {
                    //  OrderServiceDTO order = getTableView().getItems().get(getIndex());
                    //   System.out.println("Botón presionado para: " + order.getFolio());
                    // OpenModalUpdateOrderService(order.getFolio());
                    UserDTO user = getTableView().getItems().get(getIndex());

                    OpenModalCreateUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        tableUsers.getColumns().addAll(columnName, columnEmail, columnLevel, actionColumn);

        tableUsers.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tableWidth = newVal.doubleValue();
            columnName.setPrefWidth(tableWidth * 0.20);
            columnEmail.setPrefWidth(tableWidth * 0.30);
            columnLevel.setPrefWidth(tableWidth * 0.25);
            actionColumn.setPrefWidth(tableWidth * 0.25);

        });
        tableUsers.setItems(userApi.fillChoiceBoxUser());
    }

    private void OpenModalCreateUser(UserDTO user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/createUser.fxml"));
            Parent root = fxmlLoader.load();
            CreateUserController createUserController = fxmlLoader.getController();
            if (user != null) {
                createUserController.setUser(user);
            }

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            tableUsers.setItems(userApi.fillChoiceBoxUser());
        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void savePrinterNameToProperties(String printerName) {
    Properties config = new Properties();
    File file = new File("config.properties");

    try {
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                config.load(in);
            }
        }

        config.setProperty("printer.order.service.name", printerName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            config.store(out, "Configuración de impresora actualizada");
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private String loadPrinterNameFromProperties() {
    Properties config = new Properties();
    File file = new File("config.properties");

    try (FileInputStream in = new FileInputStream(file)) {
        config.load(in);
        return config.getProperty("printer.order.service.name"); // puede devolver null si no existe
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}


}
