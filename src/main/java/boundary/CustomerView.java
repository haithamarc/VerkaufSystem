package boundary;

import boundary.abstracts.AbstractPaginatedView;
import control.CustomerService;
import entity.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class CustomerView extends AbstractPaginatedView implements Serializable {

    @Inject
    CustomerService customerService;

    private List<Customer> filteredCustomers;
    private List<String> filterItems = new ArrayList<>();
    private String errorMessage = "";
    private String dropdownValue = "";
    private String attributeValue = "";
    private Integer filterId = 0;
    private String filterFirstName;
    private String filterLastName;
    private static Integer allCustomerSize;
    private final boolean wasFiltered = false;

    public CustomerView() {
    }

    /**
     * Initializes the component by setting default filter items, getting filtered customers and their size,
     * and setting them to the relevant instance variables.
     */
    @PostConstruct
    public void init() {
        if (filterItems.isEmpty()) {
            List<String> toAdd = Arrays.asList("Customer id","First name", "Last name");
            filterItems.addAll(toAdd);
        }
        filteredCustomers = customerService.getFilteredCustomer(currentPage, itemsPerPage, filterId, filterFirstName, filterLastName);
        allCustomerSize = customerService.getFilteredCustomersSize("", "");
    }

    /**
     * updates the number of customer.
     *
     * @return The number all customers.
     */
    public Integer refreshSize() {
        return allCustomerSize;
    }

    /**
     * Filters and refresh the list and number of all customers.
     *
     * @param wasFiltered The parameter to know if the filter button was clicked.
     */
    public void refreshCustomers(boolean wasFiltered) {
        filterId = 0;
        filterFirstName = "";
        filterLastName = "";
        errorMessage = "";
        if (wasFiltered) {
            currentPage = 1;
        }
        switch (dropdownValue) {
            case "Customer id" -> {
                if (isNumeric(attributeValue)) {
                    filterId = Integer.parseInt(attributeValue);
                }else {
                    errorMessage = "Error! Please enter a valid number.";
                }
            }
            case "First name" -> filterFirstName = attributeValue;
            case "Last name" -> filterLastName = attributeValue;
            default -> {
            }
        }
        if (filterId != 0){
            allCustomerSize = 1;
        }else {
            allCustomerSize = customerService.getFilteredCustomersSize(filterFirstName, filterLastName);
        }
        filteredCustomers = customerService.getFilteredCustomer(currentPage, itemsPerPage, filterId, filterFirstName, filterLastName);
    }

    // Set the page to the initial state.
    public void resetFilter() {
        dropdownValue = "";
        attributeValue = "";
        resetElementsPage();
        errorMessage = "";
        refreshCustomers(wasFiltered);
    }

    // To check if a string input only contains numbers
    public static boolean isNumeric(String str) {
        return str.matches("\\d{1,10}");
    }

    /**
     * Returns whether the filter field is disable or not.
     *
     * @return {@code true} the filter field is disable, {@code false} otherwise
     */
    public boolean disableFilterField() {
        if (dropdownValue == null) {
            return true;
        } else return dropdownValue.isEmpty();
    }

    /**
     * Returns whether the filter button is disable or not.
     *
     * @return {@code true} the filter button is disable, {@code false} otherwise
     */
    public boolean disableFilterButton(){
        return attributeValue.isBlank() && errorMessage.isEmpty();
    }
    // Calculate the total page count based on the size of all customers and items per page
    @Override
    public Integer getTotalPageCount() {
        return (allCustomerSize + itemsPerPage - 1) / itemsPerPage;
    }

    // Refresh the current page of elements, displaying customers on the page
    public void refreshElementsPage() {
        this.refreshCustomers(wasFiltered);
    }

    // Reset the elements page to the first page and refresh it
    public void resetElementsPage() {
        firstPage();
        this.refreshElementsPage();
    }

    // Getter and Setter
    public List<Customer> getFilteredCustomers() {
        return filteredCustomers;
    }

    public void setFilteredCustomers(List<Customer> filteredCustomers) {
        this.filteredCustomers = filteredCustomers;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDropdownValue() {
        return dropdownValue;
    }

    public void setDropdownValue(String dropdownValue) {
        this.dropdownValue = dropdownValue;
    }

    public List<String> getFilterItems() {
        return filterItems;
    }

    public void setFilterItems(List<String> filterItems) {
        this.filterItems = filterItems;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

}

