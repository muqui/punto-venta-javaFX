package beans;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Product {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty barcode;
    private BigDecimal price;
    private final DoubleProperty stock;
    private final StringProperty imgUrl;
    private final IntegerProperty categoryId;
    private BigDecimal amount;  // cantidad de productos
    private BigDecimal total;  // total
    private Button botonAgregar;
    private Button botonBorrar;
    private Button botonEliminar;

    //private  StringProperty howToSell 
    private final StringProperty howToSell;
    private BigDecimal purchasePrice;
    private BigDecimal wholesalePrice;
    private final BooleanProperty stocktaking;
    private final BooleanProperty minimumStock;

    //cambios
    private final StringProperty supplier = new SimpleStringProperty();
    private final IntegerProperty entry = new SimpleIntegerProperty();
    private final IntegerProperty output = new SimpleIntegerProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private List<PackageContent> packageContents = new ArrayList<>();

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", description=" + description + ", barcode=" + barcode + ", price=" + price + ", stock=" + stock + ", imgUrl=" + imgUrl + ", categoryId=" + categoryId + ", amount=" + amount + ", total=" + total + ", botonAgregar=" + botonAgregar + ", botonBorrar=" + botonBorrar + ", botonEliminar=" + botonEliminar + ", howToSell=" + howToSell + ", purchasePrice=" + purchasePrice + ", wholesalePrice=" + wholesalePrice + ", stocktaking=" + stocktaking + ", minimumStock=" + minimumStock + '}';
    }

    public Product() {
        this.minimumStock = new SimpleBooleanProperty();
        this.stocktaking = new SimpleBooleanProperty();
        this.wholesalePrice = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.purchasePrice = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.howToSell = new SimpleStringProperty();
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.barcode = new SimpleStringProperty();
        this.price = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.stock = new SimpleDoubleProperty();
        this.imgUrl = new SimpleStringProperty();
        this.categoryId = new SimpleIntegerProperty();
        this.amount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.botonAgregar = new Button("+");
        this.botonBorrar = new Button("-");
        this.botonEliminar = new Button("Borrar");
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getBarcode() {
        return barcode.get();
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public StringProperty barcodeProperty() {
        return barcode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public Double getStock() {
        return stock.get();
    }

    public void setStock(double stock) {
        this.stock.set(stock);
    }

   

    public String getImgUrl() {
        return imgUrl.get();
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl.set(imgUrl);
    }

    public StringProperty imgUrlProperty() {
        return imgUrl;
    }

    public int getCategoryId() {
        return categoryId.get();
    }

    public void setCategoryId(int categoryId) {
        this.categoryId.set(categoryId);
    }

    public IntegerProperty categoryIdProperty() {
        return categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public Button getBotonAgregar() {
        return botonAgregar;
    }

    public void setBotonAgregar(Button botonAgregar) {
        this.botonAgregar = botonAgregar;
    }

    public Button getBotonBorrar() {
        return botonBorrar;
    }

    public void setBotonBorrar(Button botonBorrar) {
        this.botonBorrar = botonBorrar;
    }

    public Button getBotonEliminar() {
        return botonEliminar;
    }

    public void setBotonEliminar(Button botonEliminar) {
        this.botonEliminar = botonEliminar;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(BigDecimal wholesalePrice) {
        this.wholesalePrice = wholesalePrice.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isStocktaking() {
        return stocktaking.get();
    }

    public void setStocktaking(boolean stocktaking) {
        this.stocktaking.set(stocktaking);
    }

    public BooleanProperty stocktakingProperty() {
        return stocktaking;
    }

    public boolean isMinimumStock() {
        return minimumStock.get();
    }

    public void setMinimumStock(boolean minimumStock) {
        this.minimumStock.set(minimumStock);
    }

    public BooleanProperty minimumStockProperty() {
        return minimumStock;
    }

    public String getHowToSell() {
        return this.howToSell.get();
    }

    public void setHowToSell(String howToSell) {
        this.howToSell.set(howToSell);
    }

  

    /**
     * @return the packageContents
     */
    public List<PackageContent> getPackageContents() {
        return packageContents;
    }

    /**
     * @param packageContents the packageContents to set
     */
    public void setPackageContents(List<PackageContent> packageContents) {
        this.packageContents = packageContents;
    }
}
