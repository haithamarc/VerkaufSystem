package entity;

import entity.enums.OrderStatusEnum;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "order_status")
    private OrderStatusEnum orderStatus;

    public void setRequiredDate(LocalDate requiredDate) {
        this.requiredDate = requiredDate;
    }

    @Column(name = "required_date")
    private LocalDate requiredDate;

    @Column(name = "shipped_date")
    private LocalDate shippedDate;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @ManyToOne
    @JoinColumn(name ="staff_id")
    private Staff staff;

    public void setStore(Store store) {
        this.store = store;
    }

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private Set<OrderItem> orderItems;

    public Order(LocalDate orderDate, OrderStatusEnum orderStatus, LocalDate requiredDate, LocalDate shippedDate, Customer customer, Store store, Staff staff) {
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.customer = customer;
        this.store = store;
        this.staff = staff;
        this.orderItems = new HashSet<>();
    }
    public Order() {

    }
    // Setter and Getter methods
    public Integer getOrderId() { return orderId; }

    public LocalDate getOrderDate() { return orderDate; }

    public OrderStatusEnum getOrderStatus() { return orderStatus; }

    public LocalDate getRequiredDate() { return requiredDate; }

    public LocalDate getShippedDate() { return shippedDate; }

    public Customer getCustomer() { return customer;}

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setOrderStatus(OrderStatusEnum pOrdnerStatus) { this.orderStatus = pOrdnerStatus; }

    public void setShippedDate(LocalDate shippedDate) { this.shippedDate = shippedDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(getOrderId(), order.getOrderId()) && Objects.equals(getOrderDate(), order.getOrderDate()) && getOrderStatus() == order.getOrderStatus() && Objects.equals(getRequiredDate(), order.getRequiredDate()) && Objects.equals(getShippedDate(), order.getShippedDate()) && Objects.equals(getCustomer(), order.getCustomer());
    }


    public Staff getStaff() { return staff; }

    public Store getStore() { return store; }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId());
    }
    public void addOrderItem(OrderItem orderItem){
        orderItem.setItemId(orderItems.size()+1);
        orderItem.setOrder(this);
        orderItems.add(orderItem);
    }
}


