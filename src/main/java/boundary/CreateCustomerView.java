package boundary;

import control.CustomerService;
import entity.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class CreateCustomerView implements Serializable {

    @Inject
    CustomerService customerService;

    @Inject
    CustomerView customerView;

    private Customer customer;
    private boolean showCreateForm = false;
    private boolean showDialog = false;
    private String createMessage;
    private String createStatus = "";
    private boolean showCreateMessage = false;
    private String phoneNumber;

    /**
     * Initializes the customer object and sets the createMessage string to an empty value.
     */
    @PostConstruct
    public void init(){
        this.customer = new Customer();
        this.createMessage = "";
    }

    /**
     * Creates a new customer by calling the customerService to create a customer object
     * with the details entered by the user. If the customer is not already exist, the new
     * customer is created successfully and a success message is displayed. Otherwise, an
     * error message is displayed indicating that a customer with the same email already exists.
     *
     */
    public void createCustomer(){
        this.showDialog = false;
        this.showCreateMessage = true;
        if (!isAlreadyExist()) {
            if(!(phoneNumber == null)){
                formatPhoneNumber();
                this.phoneNumber = null;
            }
            customerService.createCustomer(customer);
            customerView.refreshCustomers(true);
            createMessage = customer.getFirstName() + " " + customer.getLastName() + " is created successfully.";
            this.customer = new Customer();
            createStatus = "success";
        }else {
            createMessage = "Error! A customer with this e-mail already exists!";
            createStatus = "failed";
        }
    }

    /**
     * Formats the phone number of the customer in the format (XXX) XXX-XXXX.
     * Sets the formatted phone number to the customer object.
     */
    public void formatPhoneNumber() {
        if (phoneNumber != null &&!phoneNumber.isBlank()){
            String formattedPhoneNumber = phoneNumber;
            formattedPhoneNumber = String.format("(%s) %s-%s",
                formattedPhoneNumber.substring(0, 3),
                formattedPhoneNumber.substring(3, 6),
                formattedPhoneNumber.substring(6, 10));
        customer.setPhone(formattedPhoneNumber);
        }
    }

    /**
     * Sets the flag to display the create form to true and the flag to display the add button to false.
     * This method is called when the user clicks on a button or link to create a new object.
     */
    public void activeShowCreateForm(){
        showCreateForm = true;
        this.customer = new Customer();
    }

    /**
     * Sets the flag to display the create form to false and the flag to display the add button to true.
     * This method is called when the user cancels the creation of a new object.
     */
    public void hideCreateForm(){
        showCreateForm = false;
        this.customer = new Customer();
    }

    /**
     * Resets the message for creating a new object to an empty string and sets the flag to display
     * the message to false.
     * This method is called when the user closes the message dialog.
     */
    public void hideCreateMessage(){
        this.createMessage = "";
        this.createStatus = "";
        this.showCreateMessage = false;
    }

    /**
     * Checks if a customer with the same email address already exists in the system by calling
     * the customerService to retrieve the customer with the same email address. If the list of
     * retrieved customers is empty, it returns true indicating that the customer does not exist
     * in the system. Otherwise, it returns false indicating that the customer already exists.
     *
     * @return a boolean indicating whether the customer already exists in the system (false) or not (true).
     */
    public boolean isAlreadyExist(){
        return !customerService.getCustomerByEmail(customer.getEmail()).isEmpty();
    }

    /**
     * Sets the flag to display the dialog to true. This method is called when the user clicks on a button or link
     * to show a dialog.
     */
    public void activeShowDialog(){
        showDialog = true;
    }

    /**
     * Sets the flag to display the dialog to false. This method is called when the user clicks on a button or link
     * to hide a dialog.
     */
    public void hideDialog(){
        showDialog = false;
    }


    //Getters and setters
    public Customer getCustomer() {
        return customer;
    }

    public boolean isShowCreateForm() {
        return showCreateForm;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public String getCreateMessage() {
        return createMessage;
    }

    public boolean isShowCreateMessage() {
        return showCreateMessage;
    }

    public void setShowCreateForm(boolean showCreateForm) {
        this.showCreateForm = showCreateForm;
    }

    public void setCreateMessage(String createMessage) {
        this.createMessage = createMessage;
    }

    public void setShowCreateMessage(boolean showCreateMessage) {
        this.showCreateMessage = showCreateMessage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreateStatus() {
        return createStatus;
    }

    public void setCreateStatus(String createStatus) {
        this.createStatus = createStatus;
    }
}
