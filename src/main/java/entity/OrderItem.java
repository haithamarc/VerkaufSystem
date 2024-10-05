package entity;

import entity.config.OrderItemId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@IdClass(OrderItemId.class)
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(name = "Item_Id")
    private Integer itemId;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @NotNull(message = "A value should be entered hier.")
    @Max(value = 1, message = "Discount more than 1 is not allowed.")
    @Min(value = 0, message = "Discount lower  than 0  is not allowed")
    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "list_price")
    private BigDecimal listPrice;

    @Column(name = "quantity")
    @Positive(message = "it should be bigger as 0.")
    @NotNull(message = "A number must be entered.")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItem() {
    }

    public OrderItem(Product product) {
        this.product = product;
        this.listPrice = product.getListPrice();
    }
    public Order getOrder() {
        return order;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getTotalPrice() {
        return listPrice.multiply(BigDecimal.ONE.subtract(discount)).multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.DOWN);
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @NotNull(message = "A value should be entered hier.")
    @Max(value = 100, message = "Discount more than 100%  is not allowed.")
    @Min(value = 0, message = "Discount lower  than 0% is not allowed")
    public BigDecimal getDiscountPercent(){
        return discount==null? null : discount.multiply(BigDecimal.valueOf(100));
    }

    public void setDiscountPercent(@Min(value = 0, message = "Discount lower  than 0% is not allowed.") @Max(value = 100, message = "Es kann kein Rabatt über 100% gegeben werden.") @NotNull(message = "Es muss ein Rabatt angegeben werden.") BigDecimal discount){
        setDiscount(discount==null?null:discount.divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP));
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Integer getItemId() {
        return itemId;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public OrderItem clone(){
        OrderItem result = new OrderItem();
        result.discount = this.discount;
        result.listPrice = this.listPrice;
        result.product = this.product;
        result.quantity = this.quantity;
        return result;
    }

}