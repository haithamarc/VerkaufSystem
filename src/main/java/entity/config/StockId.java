package entity.config;

import entity.Product;
import entity.Store;

import java.io.Serializable;
import java.util.Objects;


public class StockId implements Serializable {
    private Product product;
    private Store store;

    public StockId() {
    }

    public StockId(Product product, Store store) {
        this.product = product;
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockId stockId = (StockId) o;
        return Objects.equals(product, stockId.product) && Objects.equals(store, stockId.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, store);
    }
}
