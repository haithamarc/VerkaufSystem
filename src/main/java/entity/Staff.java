package entity;

import entity.enums.StaffStatusEnums;
import jakarta.persistence.*;

@NamedQuery(name = "Staff.findByEmail", query = "SELECT s FROM Staff s WHERE s.email = :email")
@Entity
@Table(name = "staffs")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_Id")
    private Integer staffId;

    @Column(name = "active")
    private StaffStatusEnums active;

    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Staff manager;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Staff(){
    }

    public Staff(StaffStatusEnums active, String email, String firstName, String lastName, String phone) {
        this.active = active;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    //------------------------GETTERS AND SETTERS----------------------

    //region GETTERS
    public Integer getStaffId() { return staffId; }

    public StaffStatusEnums getActive() { return active; }

    public String getEmail() { return email; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getPhone() { return phone; }
    public Staff getManager() { return manager; }

    public Store getStore() { return store; }


    //endregion

    //region SETTERS

    public void setActive(StaffStatusEnums active) { this.active = active; }

    public void setEmail(String email) { this.email = email; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setManager(Staff manager) { this.manager = manager; }

    public void setStore(Store store) { this.store = store; }

    //endregion
}
