/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.CategoriesApi;
import api.ExpenseApi;
import api.IncomeApi;
import api.OrderApi;
import api.ReportApi;
import api.UserApi;
import beans.OrderDetail;
import beans.VentaDetalle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DepartmentDTO;
import dto.ExpenseDTO;
import dto.ExpenseNameDTO;
import dto.ExpenseResponseDTO;
import dto.IncomeDTO;
import dto.IncomeNameDTO;
import dto.IncomesResponseDTO;

import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.OrderDetailsResponseDTO;
import dto.ReportCompleteDTO;
import dto.TransaccionDTO;
import dto.TransaccionDTO;
import dto.UserDTO;
import java.io.IOException;
import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class ReportesController implements Initializable {

    CategoriesApi categoriesApi = new CategoriesApi();
    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();
    IncomeApi incomeApi = new IncomeApi();
    ExpenseApi expenseApi = new ExpenseApi();
    ReportApi reportApi = new ReportApi();

    @FXML
    private Label labelProfitCompleReport;

    private ObservableList<OrderDetail> orderDetailsList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<OrderDTO, String> TableColumnBarcode;

    @FXML
    private ComboBox<IncomeNameDTO> comboIncomeCategory;
    @FXML
    private TableColumn<OrderDTO, Integer> TableColumnId;

    @FXML
    private Label txtTotalExpense;

    @FXML
    private TableView<OrderDetailDTO> tableSales;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private ComboBox<UserDTO> comboUser;

    @FXML
    private ComboBox<DepartmentDTO> comobDepartament;

    @FXML
    private DatePicker datePickerINcomeEndDay;

    @FXML
    private DatePicker datePickerINcomeStatDay;

    @FXML
    private DatePicker dateStar;
    @FXML
    private DatePicker datePickerExpenseEndDay;

    @FXML
    private DatePicker datePickerExpenseStartDay;

    ObservableList<VentaDetalle> data;

    @FXML
    private ComboBox<ExpenseNameDTO> comboExpenseName;

    @FXML
    private Label txtProfit;

    @FXML
    private Label txttotalPrice;

    @FXML
    private Label txttotalPurchasePrice;

    @FXML
    private TableView<ExpenseDTO> tableViewExpenses;

    @FXML
    private TableView<IncomeDTO> tableViewIncomes;

    @FXML
    private Label txtTotalIncome;

    @FXML
    private TableView<TransaccionDTO> tableAllReport;

    @FXML
    private DatePicker datePickerCompleteEnd;

    @FXML
    private DatePicker datePickerCompleteStart;

    @FXML
    void onActionVentasUpdate(ActionEvent event) {
        updateTableSells();
    }

    @FXML
    void onActonComboDepartament(ActionEvent event) {
        updateTableSells();

    }

    @FXML
    void onActonComboUser(ActionEvent event) {
        updateTableSells();

    }

    @FXML
    void onActionUptadeTableExpense(ActionEvent event) {
        updateTableExpenses();
    }

    @FXML
    void onActionIcomeupdateReport(ActionEvent event) {
        updateTableIncomes();

    }

    @FXML
    void onActionDateEnd(ActionEvent event) {
        //  System.out.println("Fecha final " + dateEnd.getValue());
        //  llenarTablaVentas();
    }

    @FXML
    void onActionDateStart(ActionEvent event) {
        //  System.out.println("Fecha inicial " + dateStar.getValue());
        //   llenarTablaVentas();

    }

    @FXML
    void onActionUpdateTableReports(ActionEvent event) {
        updateTableReports();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateStar.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.now());
        datePickerINcomeStatDay.setValue(LocalDate.now());
        datePickerINcomeEndDay.setValue(LocalDate.now());
        datePickerExpenseEndDay.setValue(LocalDate.now());
        datePickerExpenseStartDay.setValue(LocalDate.now());
        datePickerCompleteStart.setValue(LocalDate.now());
        datePickerCompleteEnd.setValue(LocalDate.now());
        tableVievIncome();
        tableViexExpense();
        fillComboBoxExpenseName();
        fillTableAllReports();
        try {

            TableColumn<OrderDetailDTO, String> columnDate = new TableColumn<>("Order Date");
            columnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrder().getDate()));
            columnDate.setResizable(true);

            //codigo de barras
            TableColumn<OrderDetailDTO, String> columnBarcode = new TableColumn<>("Codigo de barras");
            columnBarcode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getBarcode()));
            columnBarcode.setResizable(true);

            TableColumn<OrderDetailDTO, String> columnUserName = new TableColumn<>("Usuario");
            columnUserName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrder().getUser().getName()));
            columnUserName.setResizable(true);
//
            TableColumn<OrderDetailDTO, Double> columnPrice = new TableColumn<>("Precio venta ");  //precio venta
            columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            columnPrice.setResizable(true);

            TableColumn<OrderDetailDTO, Double> columnPurchasePrice = new TableColumn<>("Precio compra");  //precio venta
            columnPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
            columnPurchasePrice.setResizable(true);

            TableColumn<OrderDetailDTO, Integer> columnAmount = new TableColumn<>("Cantidad");
            columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            columnAmount.setResizable(true);

            TableColumn<OrderDetailDTO, String> columnName = new TableColumn<>("Producto");
            columnName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
            columnName.setResizable(true);

            // Nueva columna para la diferencia
            TableColumn<OrderDetailDTO, BigDecimal> columnProfit = new TableColumn<>("Beneficio");
            columnProfit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderDetailDTO, BigDecimal>, javafx.beans.value.ObservableValue<BigDecimal>>() {
                @Override
                public javafx.beans.value.ObservableValue<BigDecimal> call(CellDataFeatures<OrderDetailDTO, BigDecimal> param) {
                    return new ReadOnlyObjectWrapper<>(param.getValue().getProfit());
                }
            });

            tableSales.getColumns().addAll(columnDate, columnUserName, columnBarcode, columnName, columnAmount, columnPrice, columnPurchasePrice, columnProfit);

            // Obtener la fecha de hoy
            LocalDate today = LocalDate.now();

            // Formatear la fecha como una cadena en el formato 'YYYY-MM-DD'
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = today.format(formatter);

            // Mostrar la fecha formateada
            System.out.println("Fecha de hoy: " + formattedDate);
            fetchOrderDetailsTable(formattedDate, formattedDate, "", "");

        } catch (Exception ex) {
            Logger.getLogger(ReportesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        fillChoiceBoxDepartament();
        fillChoiceBoxUser();
        fillComboBoxIncomeName();

    }

    private void fillChoiceBoxDepartament() {

        ObservableList<DepartmentDTO> departamentList = categoriesApi.fillChoiceBoxDepartament();
        System.out.println("DAtos combo = " + departamentList.get(0).getName());

        // Verificación de que los datos se carguen correctamente
        if (!departamentList.isEmpty()) {
            System.out.println("Datos combo = " + departamentList.get(0).getName());
        }

        comobDepartament.setItems(departamentList);

        // Configurar el StringConverter para mostrar solo el nombre
        comobDepartament.setConverter(new StringConverter<DepartmentDTO>() {
            @Override
            public String toString(DepartmentDTO incomeName) {
                return incomeName != null ? incomeName.getName() : "";
            }

            @Override
            public DepartmentDTO fromString(String string) {
                return departamentList.stream()
                        .filter(department -> department.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Establecer valor por defecto si es necesario
        if (!departamentList.isEmpty()) {
            comobDepartament.setValue(departamentList.get(0)); // Selecciona el primer ingreso como valor predeterminado
        }

        /*
        
         */
    }

    private void fillChoiceBoxUser() {

        ObservableList<UserDTO> userList = userApi.fillChoiceBoxUser();
        System.out.println("DAtos combo = " + userList.get(0).getName());

        // Verificación de que los datos se carguen correctamente
        if (!userList.isEmpty()) {
            System.out.println("Datos combo = " + userList.get(0).getName());
        }

        comboUser.setItems(userList);

        // Configurar el StringConverter para mostrar solo el nombre
        comboUser.setConverter(new StringConverter<UserDTO>() {
            @Override
            public String toString(UserDTO incomeName) {
                return incomeName != null ? incomeName.getName() : "";
            }

            @Override
            public UserDTO fromString(String string) {
                return userList.stream()
                        .filter(department -> department.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Establecer valor por defecto si es necesario
        if (!userList.isEmpty()) {
            comboUser.setValue(userList.get(0)); // Selecciona el primer ingreso como valor predeterminado
        }

    }

    private void fetchOrderDetailsTable(String startDay, String endDay, String user, String category) {
        try {
            OrderDetailsResponseDTO orderDetailsResponseDTO = orderApi.fetchOrderDetails(startDay, endDay, user, category);
            List<OrderDetailDTO> orders = orderDetailsResponseDTO.getOrderDetails();
            String totalPrice = orderDetailsResponseDTO.getTotalPrice();
            String totalPurchasePrice = orderDetailsResponseDTO.getTotalPurchasePrice();
            String profit = orderDetailsResponseDTO.getProfit();
            txttotalPrice.setText("Ventas: " + totalPrice);
            txttotalPurchasePrice.setText("Costo: " + totalPurchasePrice);
            txtProfit.setText("Ganancia: " + profit);
            ObservableList<OrderDetailDTO> orderDetailsList = FXCollections.observableArrayList(orders);
            tableSales.setItems(orderDetailsList);
        } catch (IOException ex) {
            Logger.getLogger(ReportesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void tableVievIncome() {
        //date
        TableColumn<IncomeDTO, Double> date = new TableColumn<>("Fecha");  //precio venta
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setResizable(true);
        //description
        TableColumn<IncomeDTO, Double> description = new TableColumn<>("Descripcion");  //precio venta
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        description.setResizable(true);
        //Name income
        TableColumn<IncomeDTO, String> name = new TableColumn<>("Nombre");  //precio venta
        // columnBarcode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getBarcode()));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIncomeNames().getName()));
        name.setResizable(true);
        //amount
        TableColumn<IncomeDTO, String> amount = new TableColumn<>("Cantidad");  //precio venta
        // columnBarcode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getBarcode()));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amount.setResizable(true);

        tableViewIncomes.getColumns().addAll(date, description, name, amount);

        // IncomesResponseDTO incomeResponseDTO = incomeApi.incomeResponseDTO(datePickerINcomeStatDay.getValue().toString(), datePickerINcomeEndDay.getValue().toString(), departmentDTO.getName());
        IncomesResponseDTO incomeResponseDTO = incomeApi.incomeResponseDTO("2024-10-24", "2024-10-24", "");

        List<IncomeDTO> incomes = incomeResponseDTO.getIncomes();
        ObservableList<IncomeDTO> incomeList = FXCollections.observableArrayList(incomes);

        tableViewIncomes.setItems(incomeList);

        txtTotalIncome.setText(incomeResponseDTO.getTotalAmount());

    }

    public void tableViexExpense() {
        //tableViewExpenses
        //date
        TableColumn<ExpenseDTO, Double> date = new TableColumn<>("Fecha");  //precio venta
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setResizable(true);
        //description
        TableColumn<ExpenseDTO, Double> description = new TableColumn<>("Descripcion");  //precio venta
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        description.setResizable(true);
        //Name income
        TableColumn<ExpenseDTO, String> name = new TableColumn<>("Nombre");  //precio venta
        //  name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIncomeNames().getName()));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpenseNames().getName()));
        name.setResizable(true);
        //amount
        TableColumn<ExpenseDTO, String> amount = new TableColumn<>("Cantidad");  //precio venta
        // columnBarcode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getBarcode()));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amount.setResizable(true);

        tableViewExpenses.getColumns().addAll(date, description, name, amount);
        ExpenseResponseDTO expenseResponseDTO = expenseApi.expenseResponseDTO(datePickerExpenseStartDay.getValue().toString(), datePickerExpenseEndDay.getValue().toString(), "");
        List<ExpenseDTO> incomes = expenseResponseDTO.getExpenses();
        ObservableList<ExpenseDTO> expenseList = FXCollections.observableArrayList(incomes);

        tableViewExpenses.setItems(expenseList);
        txtTotalExpense.setText(expenseResponseDTO.getTotalAmount());

    }

    public void fillComboBoxIncomeName() {

        ObservableList<IncomeNameDTO> departamentObservableList = incomeApi.ComboIncomeName();
        System.out.println("DAtos combo = " + departamentObservableList.get(0).getName());

        // Verificación de que los datos se carguen correctamente
        if (!departamentObservableList.isEmpty()) {
            System.out.println("Datos combo = " + departamentObservableList.get(0).getName());
        }

        comboIncomeCategory.setItems(departamentObservableList);

        // Configurar el StringConverter para mostrar solo el nombre
        comboIncomeCategory.setConverter(new StringConverter<IncomeNameDTO>() {
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
            comboIncomeCategory.setValue(departamentObservableList.get(0)); // Selecciona el primer ingreso como valor predeterminado
        }

    }

    private void updateTableSells() {

        DepartmentDTO departament = comobDepartament.getValue();
        UserDTO user = comboUser.getValue();

        try {
            // llenarTablaVentas();
            fetchOrderDetailsTable(dateStar.getValue().toString(), dateEnd.getValue().toString(), user.getName(), departament.getName());

        } catch (Exception ex) {
            Logger.getLogger(ReportesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateTableIncomes() {
        IncomeNameDTO incomeNameDTO = comboIncomeCategory.getValue();
        IncomesResponseDTO incomeResponseDTO = incomeApi.incomeResponseDTO(datePickerINcomeStatDay.getValue().toString(), datePickerINcomeEndDay.getValue().toString(), incomeNameDTO.getName());

        List<IncomeDTO> incomes = incomeResponseDTO.getIncomes();
        ObservableList<IncomeDTO> incomeList = FXCollections.observableArrayList(incomes);

        tableViewIncomes.setItems(incomeList);

        txtTotalIncome.setText(incomeResponseDTO.getTotalAmount());

    }

    //CODIGO PARA EL REPORTE DE EGRESOS
    public void fillComboBoxExpenseName() {

        ObservableList<ExpenseNameDTO> departamentObservableList = expenseApi.ComboExpenseName();
        System.out.println("DAtos combo = " + departamentObservableList.get(0).getName());

        // Verificación de que los datos se carguen correctamente
        if (!departamentObservableList.isEmpty()) {
            System.out.println("Datos combo = " + departamentObservableList.get(0).getName());
        }

        comboExpenseName.setItems(departamentObservableList);

        // Configurar el StringConverter para mostrar solo el nombre
        comboExpenseName.setConverter(new StringConverter<ExpenseNameDTO>() {
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
            comboExpenseName.setValue(departamentObservableList.get(0)); // Selecciona el primer ingreso como valor predeterminado
        }

    }

    private void updateTableExpenses() {
        System.out.println("ACTUALIZAR TABLA EGRESOS");
        ExpenseNameDTO expenseNameDTO = comboExpenseName.getValue();
        ExpenseResponseDTO expenseResponseDTO = expenseApi.expenseResponseDTO(datePickerExpenseStartDay.getValue().toString(), datePickerExpenseEndDay.getValue().toString(), expenseNameDTO.getName());
        List<ExpenseDTO> incomes = expenseResponseDTO.getExpenses();
        ObservableList<ExpenseDTO> expenseList = FXCollections.observableArrayList(incomes);

        tableViewExpenses.setItems(expenseList);
        txtTotalExpense.setText(expenseResponseDTO.getTotalAmount());
    }

    //REPORTE COMPLETO
    public void fillTableAllReports() {

        //Nombre
        TableColumn<TransaccionDTO, Double> name = new TableColumn<>("Nombre");  //precio venta
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setResizable(true);
        //Ingreso
        TableColumn<TransaccionDTO, Double> ingreso = new TableColumn<>("Ingreso");  //precio venta
        ingreso.setCellValueFactory(new PropertyValueFactory<>("income"));
        ingreso.setResizable(true);
        //EGRESO
        TableColumn<TransaccionDTO, Double> egreso = new TableColumn<>("Egreso");  //precio venta
        egreso.setCellValueFactory(new PropertyValueFactory<>("expense"));
        egreso.setResizable(true);
        //total
        TableColumn<TransaccionDTO, Double> total = new TableColumn<>("Total");  //precio venta
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        total.setResizable(true);

        tableAllReport.getColumns().addAll(name, ingreso, egreso, total);

        // Ajustar el ancho de las columnas
        tableAllReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ReportCompleteDTO reportCompleteDTO = reportApi.RepoertCompleResponseDTO(datePickerCompleteStart.getValue().toString(), datePickerCompleteEnd.getValue().toString());
        ObservableList<TransaccionDTO> expenseList = FXCollections.observableArrayList(reportCompleteDTO.getReports());
        tableAllReport.setItems(expenseList);

        labelProfitCompleReport.setText("INGRESO : " + reportCompleteDTO.getIncome() + "   EGRESO: " + reportCompleteDTO.getExpense() + "  GANANCIA: " + reportCompleteDTO.getProfit());

    }

    private void updateTableReports() {

        System.out.println("ACTUALIZAR TABLA reporte completo");

        //ExpenseResponseDTO expenseResponseDTO = expenseApi.expenseResponseDTO(datePickerExpenseStartDay.getValue().toString(), datePickerExpenseEndDay.getValue().toString(), expenseNameDTO.getName());
        ReportCompleteDTO reportCompleteDTO = reportApi.RepoertCompleResponseDTO(datePickerCompleteStart.getValue().toString(), datePickerCompleteEnd.getValue().toString());

        List<TransaccionDTO> reports = reportCompleteDTO.getReports();
        ObservableList<TransaccionDTO> reportList = FXCollections.observableArrayList(reports);

        tableAllReport.setItems(reportList);

        labelProfitCompleReport.setText("INGRESO : " + reportCompleteDTO.getIncome() + "   EGRESO: " + reportCompleteDTO.getExpense() + "  GANANCIA: " + reportCompleteDTO.getProfit());

    }
}
