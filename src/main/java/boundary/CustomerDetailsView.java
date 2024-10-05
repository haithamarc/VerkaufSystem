package boundary;

import control.CustomerOrderService;
import control.CustomerService;
import entity.Customer;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class CustomerDetailsView implements Serializable {

    @Inject
    CustomerService customerService;
    @Inject
    CustomerOrderService customerOrderService;
    private Integer customerId;
    private Customer customer;
    private Customer tempCustomer;
    private boolean showEditForm = false;
    private boolean showDialog = false;
    private boolean showDeleteMessage = false;
    private boolean showUpdateMessage = false;
    private String updateStatus = "";
    private String deleteStatus = "";

    /**
     * Initializes the customer object by retrieving the customer with the specified ID
     * from the customer service.
     */
    public void init(Integer pCustomerId){
        System.out.println(pCustomerId+"DLC");
        customerId = pCustomerId;
        customer = customerService.getCustomerById(pCustomerId);
        tempCustomer = new Customer(customer.getCity(),
                                    customer.getEmail(),
                                    customer.getFirstName(),
                                    customer.getLastName(),
                                    convertPhoneToOnlyNumber(customer.getPhone()),
                                    customer.getState(),
                                    customer.getStreet(),
                                    customer.getZipCode());
    }

    /**
     * Updates the customer information with the provided customer object.
     */
    public void updateCustomer(){
        customer.setCity(tempCustomer.getCity());
        customer.setEmail(tempCustomer.getEmail());
        customer.setFirstName(tempCustomer.getFirstName());
        customer.setLastName(tempCustomer.getLastName());
        customer.setPhone(formatPhoneNumber(tempCustomer.getPhone()));
        customer.setState(tempCustomer.getState());
        customer.setStreet(tempCustomer.getStreet());
        customer.setZipCode(tempCustomer.getZipCode());
        customerService.updateCustomer(customer);
        init(customerId);
        updateStatus = "The customer is updated successfully.";
        showDialog = false;
        showUpdateMessage = true;
    }

    /**
     * Deletes the customer with the given ID, if they have no associated orders.
     */
    public void deleteCustomerWithNoOrders(){
        customerService.deleteCustomer(customerId);
        deleteStatus = "The customer is deleted successfully.";
        this.showDeleteMessage = true;
    }

    /**
     * Checks if the customer can be safely deleted from the system by determining if the customer has any associated orders.
     *
     * @return true if the customer has no associated orders, false otherwise.
     */
    public boolean canBeDeleted(){
        return !customerOrderService.getAllOrdersByCustomerIdSize(customerId).equals(0);
    }

    /**
     * Hides the edit form from view.
     * This method sets the value of the `showEditForm` variable to `false`, which
     * will cause the edit form to be hidden if it is being used to control its visibility
     * on the screen.
     */
    public void hideEditForm(){
        showEditForm = false;
        init(customerId);
    }

    /**
     * Activates the edit form for display.
     * This method sets the value of the `showEditForm` variable to `true`, which
     * will cause the edit form to be displayed if it is being used to control its visibility
     * on the screen.
     */
    public void activeShowEditForm(){
        showEditForm = true;
    }

    /**
     * Activates the dialog for display.
     * This method sets the value of the `showDialog` variable to `true`, which
     * will cause the dialog to be displayed if it is being used to control its visibility
     * on the screen.
     */
    public void activeShowDialog(){
        showDialog = true;
    }

    /**
     * Hides the dialog from view.
     * This method sets the value of the `showDialog` variable to `false`, which
     * will cause the dialog to be hidden if it is being used to control its visibility
     * on the screen.
     */
    public void hideDialog(){
        showDialog = false;
    }

    /**
     * Hides the update message and edit form.
     */
    public void hideUpdateMessage(){
        deleteStatus = "";
        updateStatus = "";
        showDialog = false;
        showUpdateMessage = false;
        hideEditForm();
    }

    /**
     * Takes a phone number string and returns the same number, with all non-digit characters removed.
     *
     * @param phone the phone number string to be converted
     * @return a string containing only the digits in the phone number
     */
    public String convertPhoneToOnlyNumber(String phone){
        if(phone != null && !phone.isBlank()) {
            return phone.replaceAll("[^\\d]", "");
        }else {
            return null;
        }
    }

    /**
     * Formats the phone number of the customer in the format (XXX) XXX-XXXX.
     * Sets the formatted phone number to the customer object.
     */
    public String formatPhoneNumber(String phone){
        if(phone != null && !phone.isBlank()) {
            return String.format("(%s) %s-%s",
                    phone.substring(0, 3),
                    phone.substring(3, 6),
                    phone.substring(6, 10));
        }else{
            return null;
        }
    }

    public List<Customer> customers(){
        List<Customer> customers = new ArrayList<>();
        customers.add(this.customer);
        return customers;
    }

    //Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isShowEditForm() {
        return showEditForm;
    }

    public void setShowEditForm(boolean showEditForm) {
        this.showEditForm = showEditForm;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public boolean isShowDeleteMessage() {
        return showDeleteMessage;
    }

    public void setShowDeleteMessage(boolean showDeleteMessage) {
        this.showDeleteMessage = showDeleteMessage;
    }

    public boolean isShowUpdateMessage() {
        return showUpdateMessage;
    }

    public void setShowUpdateMessage(boolean showUpdateMessage) {
        this.showUpdateMessage = showUpdateMessage;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Customer getTempCustomer() {
        return tempCustomer;
    }

    public void setTempCustomer(Customer tempCustomer) {
        this.tempCustomer = tempCustomer;
    }
}
