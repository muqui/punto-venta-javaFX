/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.ExpenseApi;
import api.IncomeApi;
import dto.ExpenseDTO;
import dto.ExpenseNameDTO;
import dto.IncomeDTO;
import dto.IncomeNameDTO;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CajaController implements Initializable {

    IncomeApi incomeApi = new IncomeApi();
    ExpenseApi expenseApi = new ExpenseApi();
    
    @FXML
    private ComboBox<IncomeNameDTO> comboNombreIngreso;

    @FXML
    private DatePicker choosedateIncome;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtAmount;
    
    //datos del formulario egreso
     @FXML
    private ComboBox<ExpenseNameDTO> comboNombreEgreso;
    
     @FXML
    private TextField txtAmountExpense;
     
     
    @FXML
    private DatePicker datePickerEgreso;
    
    @FXML
    private TextArea txtExpenseDescription;

    @FXML
    void onActionCrearIngreso(ActionEvent event) {
        System.out.println("MOSTRAR DIALOGO PARA CREAR INGRESO");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/crearNombreIngreso.fxml"));
            Parent root = fxmlLoader.load();
            CrearNombreIngresoController crearNombreIngreso = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            String nombreIngreso = crearNombreIngreso.nombre;
            System.out.println("Nombre= " + nombreIngreso);
        } catch (Exception e) {
        }
    }
    @FXML
    void onActionCrearEgreso(ActionEvent event) {
        System.out.println("MOSTRAR DIALOGO PARA CREAR EGRESO");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CrearNombreEgreso.fxml"));
            Parent root = fxmlLoader.load();
            CrearNombreEgresoController crearNombreEgreso = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            String nombreEgreso = crearNombreEgreso.nombre;
            System.out.println("Nombre= " + nombreEgreso);
        } catch (Exception e) {
        }
    }

    @FXML
    void onActionSaveIncome(ActionEvent event) {
        IncomeDTO incomeDTO = new IncomeDTO();
        // incomeDTO.setDate(choosedateIncome.getValue());
       
        // Establecer la fecha en el IncomeDTO
        incomeDTO.setDate(choosedateIncome.getValue().toString());
        incomeDTO.setAmount(new BigDecimal(txtAmount.getText()));
        incomeDTO.setDescription(txtDescription.getText());
        incomeDTO.setIncomeNamesId(getIdComboBoxIncomeName());

        incomeApi.crearIngreso(incomeDTO);

    }
    @FXML
    void onActionAltaEgreso(ActionEvent event) {
         System.out.println("GUARADAR EGRESO");
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDate(datePickerEgreso.getValue().toString());
        expenseDTO.setAmount(new BigDecimal(txtAmountExpense.getText()));
        expenseDTO.setDescription(txtExpenseDescription.getText());
        expenseDTO.setExpenseNamesId(getIdComboBoxExpenseName());
        System.out.println(expenseDTO.toString());
         incomeApi.crearEgreso(expenseDTO);
       
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fillComboBoxIncomeName();
        fillComboBoxExpenseName();
    }
    
        public void fillComboBoxIncomeName() {
        ObservableList<IncomeNameDTO> departamentObservableList = incomeApi.ComboIncomeName();
        System.out.println("DAtos combo = " + departamentObservableList.get(0).getName());

        // Verificación de que los datos se carguen correctamente
        if (!departamentObservableList.isEmpty()) {
            System.out.println("Datos combo = " + departamentObservableList.get(0).getName());
        }

        comboNombreIngreso.setItems(departamentObservableList);

        // Configurar el StringConverter para mostrar solo el nombre
        comboNombreIngreso.setConverter(new StringConverter<IncomeNameDTO>() {
            @Override
            public String toString(IncomeNameDTO incomeName) {
                return incomeName != null ? incomeName.getName() : "";
            }

            @Override
            public IncomeNameDTO fromString(String string) {
                return departamentObservableList.stream()
                        .filter(department -> department.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Establecer valor por defecto si es necesario
        if (!departamentObservableList.isEmpty()) {
            comboNombreIngreso.setValue(departamentObservableList.get(0)); // Selecciona el primer ingreso como valor predeterminado
        }

    }

    public void fillComboBoxExpenseName() {
        ObservableList<ExpenseNameDTO> departamentObservableList = expenseApi.ComboExpenseName();
        System.out.println("DAtos combo egreso = " + departamentObservableList.get(0).getName());

        // Verificación de que los datos se carguen correctamente
        if (!departamentObservableList.isEmpty()) {
            System.out.println("Datos combo = " + departamentObservableList.get(0).getName());
        }

        comboNombreEgreso.setItems(departamentObservableList);

        // Configurar el StringConverter para mostrar solo el nombre
        comboNombreEgreso.setConverter(new StringConverter<ExpenseNameDTO>() {
            @Override
            public String toString(ExpenseNameDTO incomeName) {
                return incomeName != null ? incomeName.getName() : "";
            }

            @Override
            public ExpenseNameDTO fromString(String string) {
                return departamentObservableList.stream()
                        .filter(department -> department.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Establecer valor por defecto si es necesario
        if (!departamentObservableList.isEmpty()) {
            comboNombreEgreso.setValue(departamentObservableList.get(0)); // Selecciona el primer ingreso como valor predeterminado
        }

    }

    public int getIdComboBoxIncomeName() {
        int selectedId = 0;
        // Suponiendo que el ComboBox se llama comboNombreIngreso
        IncomeNameDTO selectedIncomeName = comboNombreIngreso.getValue();

        if (selectedIncomeName != null) {
            selectedId = selectedIncomeName.getId();
            System.out.println("ID seleccionado = " + selectedId);
        } else {
            System.out.println("No se ha seleccionado ningún ingreso.");
        }
        return selectedId;
    }
    
     public int getIdComboBoxExpenseName() {
        int selectedId = 0;
        // Suponiendo que el ComboBox se llama comboNombreIngreso
        ExpenseNameDTO selectedExpenseName = comboNombreEgreso.getValue();

        if (selectedExpenseName != null) {
            selectedId = selectedExpenseName.getId();
            System.out.println("ID seleccionado = " + selectedId);
        } else {
            System.out.println("No se ha seleccionado ningún ingreso.");
        }
        return selectedId;
    }

}
