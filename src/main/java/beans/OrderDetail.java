package beans;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.math.BigDecimal;

public class OrderDetail {
    private final IntegerProperty id;
    private BigDecimal price;
    private BigDecimal amount;
    private final Product product;
    private BigDecimal purchasePrice;

    public OrderDetail() {
        this.id = new SimpleIntegerProperty();
        this.price = BigDecimal.ZERO.setScale(2);
        this.amount = BigDecimal.ZERO.setScale(2);
        this.product = new Product();
        this.purchasePrice = BigDecimal.ZERO.setScale(2);
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
