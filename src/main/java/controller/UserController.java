/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.UserApi;
import dto.OrderServiceDTO;
import dto.UserDTO;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class UserController implements Initializable {

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

}
