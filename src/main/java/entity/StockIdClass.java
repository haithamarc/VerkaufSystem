package entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class is so we can use a composite primary key for the entity Stock
 */
public class StockIdClass implements Serializable {

    private Product product;
    private Store store;

    public StockIdClass(){
    }

    public StockIdClass(Product product, Store store) {
        this.product = product;
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockIdClass that = (StockIdClass) o;
        return product.equals(that.product) && store.equals(that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, store);
    }
}
