package entity;

import jakarta.persistence.*;
@NamedQuery(name = "Brand.getAll", query = "SELECT b FROM Brand b ORDER BY b.brandId")
@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_Id")
    private Integer brandId;

    @Column(name = "brand_name")
    private String brandName;

    public Brand() {
    }

    //Getters and Setters
    public Integer getBrandId() { return brandId; }

    public String getBrandName() { return brandName; }
}