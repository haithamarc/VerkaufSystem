package boundary;

import boundary.abstracts.AbstractPaginatedView;
import control.StoreService;
import entity.Store;
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
public class StoreView extends AbstractPaginatedView implements Serializable {


    @Inject
    StoreService storeService;

    private List<String> filterItems = new ArrayList<>();
    private List<Store> storesList;
    private Integer allStoreSize;
    private String dropdownValue = "";
    private String errorMessage = "";
    private String attributeValue = "";
    private boolean wasFiltered=false;

    /**
     * Initializes the bean by setting the filter items list and retrieving the stores data from the store service.
     * If the filter items list is empty, it initializes it with default filter items. It retrieves the stores data
     * by calling the store service to retrieve a page of stores with the given parameters and sets the current page,
     * items per page, and the total number of stores.
     */
    @PostConstruct
    public void init(){
        if (filterItems.isEmpty()) {
            List<String> toAdd = Arrays.asList("Store id","Store name");
            filterItems.addAll(toAdd);
        }
        this.storesList = storeService.getStoresPage(currentPage, itemsPerPage, "");
        this.allStoreSize = storeService.getStoresSize("");
    }

    /**
     * Retrieves the total number of stores from the store service and returns it as an Integer.
     * This method is called to refresh the size of the stores list after new stores have been added or deleted.
     *
     * @return the total number of stores in the system as an Integer.
     */
    public Integer refreshSize(){return allStoreSize;}

    /**
     * Refreshes the stores list based on the given filter attributes and whether or not the list was previously filtered.
     * If the list was previously filtered, the current page is set to the first page. The filter attributes are obtained from
     * the relevant variables and are used to retrieve a page of stores from the store service. The size of the stores list is
     * also retrieved and set. If the filter attribute "Store id" is selected, the attribute value is checked for numeric
     * validity and used to retrieve a single store from the store service with the given id.
     *
     * @param wasFiltered a boolean value indicating whether or not the stores list was previously filtered.
     */
    public void refreshStores(boolean wasFiltered){
        Integer filterId = null;
        String filterName = "";
        errorMessage = "";
        if(wasFiltered){
            currentPage = 1;
        }
        switch (dropdownValue) {
            case "Store id" -> {
                if(isNumeric(attributeValue)) {
                    filterId = Integer.parseInt(attributeValue);
                    storesList = new ArrayList<>();
                    Store store = storeService.getStoreById(filterId);
                    if(store != null){
                        storesList.add(store);
                    }
                    allStoreSize = storesList.size();
                }else {
                    errorMessage = "Error! Please enter a valid number.";
                }
            }
            case "Store name" -> filterName = attributeValue;
            default -> {
            }
        }
        if (filterId == null){
            allStoreSize = storeService.getStoresSize(filterName);
            storesList = storeService.getStoresPage(currentPage, itemsPerPage, filterName);
        }

    }

    // Set the page to the initial state.
    public void resetFilter() {
        dropdownValue = "";
        attributeValue = "";
        resetElementsPage();
        errorMessage = "";
        refreshStores(wasFiltered);
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

    // Refresh the current page of elements, displaying stores on the page
    public void refreshElementsPage() {
        this.refreshStores(wasFiltered);
    }
    // Reset the elements page to the first page and refresh it
    public void resetElementsPage() {
        firstPage();
        this.refreshElementsPage();
    }

    // Calculate the total page count based on the size of all stores and items per page
    @Override
    public Integer getTotalPageCount() {
        return (allStoreSize + itemsPerPage - 1) / itemsPerPage;
    }

    //Getters and Setters
    public List<Store> getStoresList() {
        return storesList;
    }

    public List<String> getFilterItems() {
        return filterItems;
    }

    public void setFilterItems(List<String> filterItems) {
        this.filterItems = filterItems;
    }

    public void setStoresList(List<Store> storesList) {
        this.storesList = storesList;
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

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public void setWasFiltered(boolean wasFiltered) {
        this.wasFiltered = wasFiltered;
    }
}
