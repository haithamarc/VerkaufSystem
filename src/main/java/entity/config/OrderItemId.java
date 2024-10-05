package entity.config;

import entity.Order;
import entity.Product;

import java.io.Serializable;
import java.util.Objects;


public class OrderItemId implements Serializable {
    private Product product;
    private Order order;

    public OrderItemId() {
    }

    public OrderItemId(Product product, Order order) {
        this.product = product;
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemId that = (OrderItemId) o;
        return Objects.equals(product, that.product) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, order);
    }
}
