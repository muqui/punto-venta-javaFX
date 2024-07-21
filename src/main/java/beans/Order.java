package beans;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order {
    private final IntegerProperty id;
    private final StringProperty date;
    private final ObservableList<OrderDetail> orderDetails;
    private User user;

    public Order() {
        this.id = new SimpleIntegerProperty();
        this.date = new SimpleStringProperty();
        this.orderDetails = FXCollections.observableArrayList();
        this.user = new User();
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

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }

    public ObservableList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    
        public void addOrderDetail(OrderDetail detail) {
        this.getOrderDetails().add(detail);
    }
}
