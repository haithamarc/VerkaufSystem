package boundary;

import boundary.abstracts.AbstractPaginatedView;
import control.CustomerService;
import control.OrderService;
import entity.*;
import entity.enums.OrderStatusEnum;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static entity.enums.OrderStatusEnum.NO_STATE;


@Named
@ViewScoped
public class OrderView  extends AbstractPaginatedView implements Serializable {
    private String inputOrderId  ="";
    private List<String> attributeList = new ArrayList<>();
    private String dropdownValue = "";
    private String attributeValue = "";
    private int allOrderSize;
    private List <Order> orderList;
    private  OrderStatusEnum OrderStatus =NO_STATE;
    private int selectedCustomerId ;
    private String errorMessage = "";
    @Inject
    OrderService orderService;
    @Inject
    CustomerService customerService;

    /**
     * Initializes the list of all orders and populates the extra filter list, if empty.
     * This method is called after the bean's properties have been initialized, following dependency injection.
     * The extra filter list is set to include "OrderDate", "RequiredDate", and "ShippedDate".
     * The total number of orders and a filtered list of orders with pagination are also retrieved.
     */
    @PostConstruct
    public void listAllOrders() {
        if(this.attributeList.isEmpty()) {
            this.attributeList.add("OrderDate");
            this.attributeList.add("RequiredDate");
            this.attributeList.add("ShippedDate");
        }
        this.allOrderSize = orderService.getAllOrders().size();
        this.orderList = orderService.getAllOrdersFilter("", NO_STATE, "", "", currentPage, itemsPerPage);
    }
    // Calculate the total page count based on the size of all products and items per page
    public Integer getTotalPageCount() {
        return (this.allOrderSize + itemsPerPage - 1) / itemsPerPage;
    }

    // Refresh the current page of elements, displaying products on the page
    public void refreshElementsPage() {
        this.listOrderFiltered();
    }
    // regenerate the list of order after using filter
    public void listOrderFiltered() {

        List<Order> tempList = orderService.getAllOrdersFilter(
                this.inputOrderId, this.OrderStatus, this.dropdownValue, this.attributeValue, 1, 10000
        );

        this.allOrderSize = (tempList != null) ? tempList.size() : this.allOrderSize;

        tempList = orderService.getAllOrdersFilter(
                this.inputOrderId, this.OrderStatus, this.dropdownValue, this.attributeValue, currentPage, itemsPerPage
        );

        if(tempList != null) {
            this.orderList = tempList;
            this.errorMessage = "";
        } else {
            this.errorMessage = "Error! Please enter a valid number.";
            this.attributeValue = "";
        }
    }

    // Reset the elements page to the first page and refresh it
    public void resetElementsPage() {
        firstPage();
        this.refreshElementsPage();
    }

    // to reset the values of filters in the  page
    public String resetFilter() {
        this.OrderStatus = NO_STATE;
        this.inputOrderId ="" ;
        this.attributeValue ="";
        this.dropdownValue = "" ;

        orderList =orderService.getAllOrdersFilter("", NO_STATE, "", "", currentPage, itemsPerPage);
        return "orders?faces-redirect=true";
    }

    //to make sure that Date input text is always disabled when the type of Date is not selected
    public boolean isDateDisabled(){
        if  (dropdownValue.equals(0) || dropdownValue =="0" || dropdownValue.equals("0") ) return true ;
        if  (dropdownValue != "" ) return false ;
        return true ;
    }
    //to make sure that Date input text is always disabled when the type of Date is not selected
    public boolean filterButtonEnable(){
        return !((this.OrderStatus != NO_STATE) || (!this.inputOrderId.isEmpty() ) ||   (!this.isDateDisabled()) );
    }
    // Getter and Setter
    public List<Customer> getCustomerList(){
        return this.customerService.getAllCustomers();
    }

    //Getters and setters
    public OrderStatusEnum getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.OrderStatus = orderStatus;
    }
    public String getDropdownValue() {
        return dropdownValue;
    }

    public void setDropdownValue(String dropdownValue) {
        this.dropdownValue = dropdownValue;
    }
    public List<String> getAttributeList() {
        return attributeList;
    }
    public void setAttributeList(List<String> pAttributeList) {
        this.attributeList = pAttributeList;
    }
    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String pAttributeValue) {
        this.attributeValue = pAttributeValue;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getInputOrderId() {
        return inputOrderId;
    }
    public void setInputOrderId(String inputOrderId) {
        this.inputOrderId = inputOrderId;
    }
    public List<Order> getOrderList() {
        return orderList;
    }
    public void setOrderList(List<Order> orderlist) {
        this.orderList = orderlist;
    }
    public int getAllOrderSize() {
        return allOrderSize;
    }
    public void setAllOrderSize(int allOrderSize) {
        this.allOrderSize = allOrderSize;
    }
    public int getSelectedCustomerId() {
        return selectedCustomerId;
    }
    public void setSelectedCustomerId(int selectedCustomerId) {
        this.selectedCustomerId = selectedCustomerId;
    }
}
