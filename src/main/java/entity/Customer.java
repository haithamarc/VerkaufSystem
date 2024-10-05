package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Set;

@Entity
@Table(name = "customers")
@NamedQueries({
        @NamedQuery (name = "searchAllCustomer", query = "SELECT c FROM Customer c ORDER BY c.customerId")
})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Email(message = "Invalid e-mail!")
    @NotBlank(message = "E-mail can not be empty!")
    private String email;

    @NotBlank(message = "First name can not be empty!")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name can not be empty!")
    @Column(name = "last_name")
    private String lastName;

    private String phone;

    @NotBlank(message = "State can not be empty!")
    private String state;

    @NotBlank(message = "City can not be empty!")
    private String city;

    @NotBlank(message = "Zip code can not be empty!")
    @Column(name = "zip_code")
    private String zipCode;

    @NotBlank(message = "Street can not be empty!")
    private String street;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<Order> orders;

    public Customer() {
    }

    public Customer(String city, String email, String firstName, String lastName, String phone, String state, String street, String zipCode) {
        this.city = city;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.state = state;
        this.street = street;
        this.zipCode = zipCode;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public String getCity() { return city; }

    public String getEmail() { return email; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getPhone() { return phone; }

    public String getState() { return state; }

    public String getStreet() { return street; }

    public String getZipCode() { return zipCode; }

    public Set<Order> getOrders() { return orders; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}