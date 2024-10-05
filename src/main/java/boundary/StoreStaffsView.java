package boundary;

import boundary.abstracts.AbstractPaginatedView;
import control.StaffService;
import control.StoreService;
import entity.Staff;
import entity.enums.StaffStatusEnums;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class StoreStaffsView extends AbstractPaginatedView implements Serializable {

    @Inject
    StaffService staffService;
    @Inject
    StoreService storeService;
    private List<Staff> staffList;
    private List<String> filterItems = new ArrayList<>();
    private String dropdownValue = "";
    private String errorMessage = "";
    private String attributeValue = "";
    private String storeName;
    private Integer storeId = 0;
    private Integer allStaffsSize;
    private final boolean wasFiltered=false;

    public StoreStaffsView(){
    }

    /**
     * Initializes the staff list by setting the store ID, store name, and staff list variables.
     * The method also adds filter items to the filterItems list, retrieves all staffs from the staff service,
     * and sets the allStaffsSize and staffList variables.
     *
     * @param storeId an integer representing the ID of the store to retrieve staffs from
     */
    public void init(Integer storeId){
        setStoreId(storeId);
        if (filterItems.isEmpty()) {
            List<String> toAdd = Arrays.asList("Id", "First Name", "Last Name");
            filterItems.addAll(toAdd);
        }
        this.storeName = storeService.getStoreById(storeId).getStoreName();
        this.staffList = staffService.getAllStaffFilter(storeId, StaffStatusEnums.NO_STATE,"","",1, 10000);
        this.allStaffsSize= staffList.size();
        this.staffList = staffService.getAllStaffFilter(storeId, StaffStatusEnums.NO_STATE,"","",currentPage, itemsPerPage);
    }

    /**
     * updates the number of staffs.
     *
     * @return The number all staffs.
     */
    public Integer refreshSize(){return allStaffsSize;}

    /**
     * Refreshes the staffs list by retrieving staff objects from the staff service and updating the staff list
     * and related variables. The method takes a boolean value indicating whether the staffs list was previously filtered.
     *
     * @param wasFiltered a boolean value indicating whether the staffs list was previously filtered
     */
    public void refreshStaffs(boolean wasFiltered){
        List<Staff> tempList = staffService.getAllStaffFilter(
                this.storeId, StaffStatusEnums.NO_STATE, this.dropdownValue, this.attributeValue, 1, 10000
        );
        this.allStaffsSize = (tempList != null) ? tempList.size() : this.allStaffsSize;

        tempList = staffService.getAllStaffFilter(
                this.storeId, StaffStatusEnums.NO_STATE, this.dropdownValue, this.attributeValue, currentPage, itemsPerPage
        );

        if(tempList != null) {
            if(wasFiltered) currentPage = 1;
            this.staffList = tempList;
            this.errorMessage = "";
        } else {
            this.errorMessage = "Error! Field needs a valid numerical value.";
            this.attributeValue = "";
        }
    }

    // Set the page to the initial state.
    public void resetFilter() {
        dropdownValue = "";
        attributeValue = "";
        resetElementsPage();
        errorMessage = "";
        refreshStaffs(wasFiltered);
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


    // Refresh the current page of elements, displaying staffs on the page
    public void refreshElementsPage() {
        this.refreshStaffs(wasFiltered);
    }

    // Reset the elements page to the first page and refresh it
    public void resetElementsPage() {
        firstPage();
        this.refreshElementsPage();
    }

    // Calculate the total page count based on the size of all staffs and items per page
    @Override
    public Integer getTotalPageCount() {
        return (allStaffsSize + itemsPerPage - 1) / itemsPerPage;
    }

    // Getters and Setters
    public List<String> getFilterItems() {
        return filterItems;
    }
    public void setFilterItems(List<String> filterItems) {
        this.filterItems = filterItems;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public String getDropdownValue() {
        return dropdownValue;
    }

    public void setDropdownValue(String dropdownValue) {
        this.dropdownValue = dropdownValue;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }
}
