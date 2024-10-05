package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@NamedQuery(name = "Product.getModelYears", query = "SELECT DISTINCT p.modelYear FROM Product p ORDER BY p.modelYear")
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_Id")
    private Integer productId;

    @Column(name = "list_price")
    @NotNull(message = "List price field must not be empty!")
    @Positive(message = "List price must be a numerical value greater than 0!")
    private BigDecimal listPrice;

    @Column(name = "model_year")
    @NotNull(message = "Model year field must not be empty!")
    @Positive(message = "The model year must be a number and at least be greater than 0!")
    private Integer modelYear;

    @Column(name = "product_name")
    @NotBlank(message = "Product name field must not be empty!")
    private String productName;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany
    private Set<Stock> stocks;

    public Product() {
    }

    public Product(BigDecimal listPrice, Integer modelYear, String productName) {
        this.listPrice = listPrice;
        this.modelYear = modelYear;
        this.productName = productName;
    }

    public Product(BigDecimal listPrice, Integer modelYear, String productName, Brand brand, Category category) {
        this.listPrice = listPrice;
        this.modelYear = modelYear;
        this.productName = productName;
        this.brand = brand;
        this.category = category;
    }

    //------------------------GETTERS AND SETTERS----------------------

    //region GETTERS
    public Integer getProductId() { return this.productId; }

    public BigDecimal getListPrice() { return this.listPrice; }

    public Integer getModelYear() { return this.modelYear; }

    public String getProductName() { return this.productName; }

    public Brand getBrand() { return this.brand; }

    public Category getCategory() { return this.category; }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    //endregion

    //region SETTERS

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setListPrice(BigDecimal listPrice) { this.listPrice = listPrice; }

    public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }

    public void setProductName(String productName) { this.productName = productName; }

    public void setBrand(Brand brand) { this.brand = brand; }

    public void setCategory(Category category) { this.category = category; }

    //endregion

}
