package boundary;

import control.CustomerOrderService;
import control.CustomerService;
import entity.Customer;
import entity.Order;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CustomerOrdersView implements Serializable {

    @Inject
    CustomerService customerService;
    @Inject
    CustomerOrderService customerOrderService;

    private Customer customer;
    private Integer customerId;
    private List<Order> ordersList;
    private Integer allOrdersSize;

    public CustomerOrdersView() {
    }

    //Initialize attributes
    public void init(Integer pCustomerId){
        customerId = pCustomerId;
        ordersList = customerOrderService.getAllOrdersByCustomerId(pCustomerId);
        customer = customerService.getCustomerById(pCustomerId);
        allOrdersSize = customerOrderService.getAllOrdersByCustomerIdSize(pCustomerId);
    }

    /**
     * Retrieves the first and last name via the ID of Customers.
     *
     * @return The first and last name of customer in a same String.
     */
    public String holdFirstAndLastName(){
        return customerService.getCustomerById(customerId).getFirstName() + " " +customerService.getCustomerById(customerId).getLastName();
    }

    //Getter and Setter
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public List<Order> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Integer getAllOrdersSize() {
        return allOrdersSize;
    }
}
