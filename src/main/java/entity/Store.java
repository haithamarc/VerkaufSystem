package entity;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "store_id", nullable = false)
    private Integer storeId;
    @Column(name = " city")
    private String city;
    private String email;
    private String phone;
    private String state;
    @Column(name = "store_name")
    private String storeName;
    private String street;
    @Column(name = "zip_code")
    private String zipCode;

    @OneToMany
    @JoinColumn(name = "store_id")
    private Set<Staff> staffs;

    @OneToMany
    @JoinColumn(name = "store_id")
    private Set<Stock> stocks;

    @OneToMany
    private Set<Order> orders;

    public Store() {
    }

    //------------------------GETTERS AND SETTERS----------------------

    //region GETTER

    public Integer getStoreId() {
        return storeId;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getState() {
        return state;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStreet() {
        return street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Set<Staff> getStaffs() {
        return staffs;
    }

    public Set<Stock> getProducts() {
        return stocks;
    }

    public Set<Order> getOrders() {
        return orders;
    }
    //endregion

}

