package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Objects;

@NamedQuery(name = "Stock.getFilteredStockPage", query = "SELECT s FROM Stock s WHERE s.store = :store AND (:modelYear IS NULL OR s.product.modelYear = :modelYear) AND (:brand IS NULL OR s.product.brand = :brand) AND (:category IS NULL OR s.product.category = :category) AND (UPPER(s.product.productName) LIKE UPPER(:name)) ORDER BY s.product.productId")
@NamedQuery(name = "Stock.getFilteredStockSize", query = "SELECT COUNT(s) FROM Stock s WHERE s.store = :store AND (:modelYear IS NULL OR s.product.modelYear = :modelYear) AND (:brand IS NULL OR s.product.brand = :brand) AND (:category IS NULL OR s.product.category = :category) AND (UPPER(s.product.productName) LIKE UPPER(:name))")

@Entity
//@IdClass(StockId.class)
@Table(name = "stocks")
public class Stock {

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @PositiveOrZero
    private int quantity;

    public Stock() {
    }

    public Stock(Product product, Store store, Integer quantity) {
        this.product = product;
        this.store = store;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }

    public Store getStore() { return store; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public void decreaseQuantity(Integer amount){
        this.quantity -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return quantity == stock.quantity && Objects.equals(product, stock.product) && Objects.equals(store, stock.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, store, quantity);
    }
}
