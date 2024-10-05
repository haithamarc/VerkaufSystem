package boundary;

import boundary.abstracts.AbstractPaginatedView;
import control.StaffService;
import entity.Staff;
import entity.Store;
import entity.enums.StaffStatusEnums;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named
@ViewScoped
public class StaffView extends AbstractPaginatedView implements Serializable {

    //region ATTRIBUTES
    @Inject
    StaffService staffService;
    private List<Staff> staffList;
    private Staff staff;
    private StaffStatusEnums active = StaffStatusEnums.NO_STATE;
    private Integer managerId = 0;
    private Integer storeId = 0;
    private List<String> attributeList = new ArrayList<>();
    private String dropdownValue = null;
    private String attributeValue = "";
    private String errorMessage = "";
    private Integer allStaffSize;
    private String creationStateMessage;
    private Boolean createView = false;
    private Boolean updateView = false;
    private Boolean deleteView = false;
    private Boolean createConfirmation = false;
    private Boolean updateConfirmation = false;
    private Boolean showSuccessMessage = false;
    private Boolean showDeleteMessage = false;

    //endregion

    //region METHODS
    /**
     * Initializes the staff list by adding default attributes to the attribute list,
     * retrieving all staff from the database, and filtering the staff list based on the current page,
     * items per page, and search criteria.
     * This method is automatically called after the bean has been constructed.
     */
    @PostConstruct
    public void listAllStaff() {
        if(this.attributeList.isEmpty()) {
            this.attributeList.add("Id");
            this.attributeList.add("First Name");
            this.attributeList.add("Last Name");
        }
        findStaff();
        this.allStaffSize = staffService.getAllStaff().size();
        this.staffList = staffService.getAllStaffFilter(0, this.active, "", "", currentPage, itemsPerPage);
    }

    /**
     * Retrieves the staff member from the database with the ID retrieved by the retrieveStaffId() method.
     */
    public void findStaff() {

        if(retrieveStaffId() != null) {
            this.staff = staffService.findStaffById(retrieveStaffId());

            if(this.staff != null) {
                this.managerId = this.staff.getManager() != null
                        ? this.staff.getManager().getStaffId()
                        : 0;
                this.storeId = this.staff.getStore() != null
                        ? this.staff.getStore().getStoreId()
                        : 0;
                return;
            }
        }
        this.staff = new Staff();
    }

    // Calculate the total page count based on the size of all products and items per page
    public Integer getTotalPageCount() {
        return (this.allStaffSize + itemsPerPage - 1) / itemsPerPage;
    }

    // Refresh the current page of elements, displaying products on the page
    public void refreshElementsPage() {
        this.listStaffFiltered(false);
    }

    // Reset the elements page to the first page and refresh it
    public void resetElementsPage() {
        firstPage();
        this.refreshElementsPage();
    }

    /**
     * Resets the filter for the staff list by setting all filter criteria to their default values,
     * and calls the listAllStaff() method to retrieve all staff members from the database and apply the new filter.
     */
    public void resetFilter() {
        this.storeId = 0;
        this.active = StaffStatusEnums.NO_STATE;
        this.dropdownValue = "";
        this.attributeValue = "";
        this.errorMessage = "";
        this.creationStateMessage = "";
        this.createView = false;
        this.deleteView = false;
        this.updateView = false;
        currentPage = 1;
        itemsPerPage = 10;
        this.listAllStaff();

    }

    /**
     * Retrieves the ID of the staff member from the request parameter map.
     *
     * @return the ID of the staff member as an Integer, or null if no ID was found in the request parameter map
     */
    public Integer retrieveStaffId() {

        FacesContext context = FacesContext.getCurrentInstance();
        if(context.getExternalContext().getRequestParameterMap().get("staffId") != null) {
            return Integer.valueOf(context.getExternalContext().getRequestParameterMap().get("staffId"));
        } else {
            return null;
        }
    }

    /**
     * Creates a new staff member with the properties specified by the values of the active, email, firstName, lastName,
     * and phone instance variables, and persists it to the database using the persistStaff() method of the staffService object.
     * After the staff member has been successfully persisted, the instance variables are reset, and the list of staff members is updated.
     * The method also refreshes the current page to display the updated staff list.
     */
    public void createStaff() {

        this.staff.setManager(this.managerId != null ?
                staffService.findStaffById(this.managerId) :
                null);
        this.staff.setStore(staffService.findStoreById(this.storeId));
        this.staff.setPhone(transformPhoneNum());
        staffService.persistStaff(this.staff);
        this.creationStateMessage =
                "Staff " + staff.getStaffId() + " with name " + staff.getFirstName() + " " + staff.getLastName() + " successfully created!";
        this.staffList = staffService.getAllStaff();
        confirmCreate();
        this.showSuccessMessage = true;
    }

    /**
     * Updates the properties of an existing staff member in the database with the values of the active, email, firstName,
     * lastName, and phone instance variables, using the updateStaff() method of the staffService object.
     * After the staff member has been successfully updated, the creationStateMessage instance variable is set to a success message,
     * and the method calls the displayUpdateForm() method to display the updated staff member.
     */
    public void updateStaff() {

        this.staff.setManager(this.managerId != null ?
                staffService.findStaffById(this.managerId) :
                null);
        this.staff.setStore(staffService.findStoreById(this.storeId));

        staffService.updateStaff(
                retrieveStaffId(),
                this.staff
        );

        this.creationStateMessage =
                "Staff " + retrieveStaffId() + " with name " + staff.getFirstName() + " " + staff.getLastName() + " successfully updated!";
        this.updateConfirmation = false;
        this.showSuccessMessage = true;
    }

    /**
     * Deletes the currently selected staff member from the database using the deleteStaff() method of the staffService object.
     * After the staff member has been successfully deleted, the list of staff members is updated, and the method calls the
     * displayDeleteForm() method to hide to delete confirmation dialog and reset the current staff member.
     * The method also refreshes the current page to display the updated staff list.
     */
    public void deleteStaff() {

        this.creationStateMessage =
                "Staff " + retrieveStaffId() + " with name " + this.staff.getFirstName() + " " + this.staff.getLastName() + " successfully deleted!";
        staffService.deleteStaff(retrieveStaffId());
        this.deleteView = false;
        this.showDeleteMessage = true;
    }

    /**
     * Checks if the customer can be safely deleted from the system by determining if the customer has any associated orders.
     *
     * @return true if the customer has no associated orders, false otherwise.
     */
    public boolean staffIsDeletable() {
        return staffService.getAllStaffOfOrder(retrieveStaffId()).equals(0);
    }

    /**
     * Retrieves a filtered list of staff members based on the storeId, active, dropdownValue, and attributeValue instance variables.
     * The getAllStaffFilter() method of the staffService object is used to retrieve the filtered list. The method retrieves the first
     * 10000 staff members from the filtered list and sets the allStaffSize instance variable to the size of the filtered list.
     * The method then retrieves the staff members for the current page using the currentPage and itemsPerPage instance variables,
     * and sets the staffList instance variable to the resulting list. If wasFiltered is true, the currentPage is set to 1 to display
     * the first page of the filtered list. If the attributeValue is not a valid numerical value, the method sets the errorMessage
     * instance variable to "Error! Field needs a valid numerical value."
     *
     * @param wasFiltered a boolean value indicating whether the list was filtered
     */
    public void listStaffFiltered(boolean wasFiltered) {
        List<Staff> tempList = staffService.getAllStaffFilter(
                this.storeId, this.active, this.dropdownValue, this.attributeValue, 1, 10000
        );
        this.allStaffSize = (tempList != null) ? tempList.size() : this.allStaffSize;

        tempList = staffService.getAllStaffFilter(
                this.storeId, this.active, this.dropdownValue, this.attributeValue, currentPage, itemsPerPage
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

    /**
     * Determines whether the search field should be disabled based on the selected dropdown value.
     *
     * @return true if the dropdown value is null or empty, false otherwise
     */
    public boolean searchFieldDisabled() {

        if(this.dropdownValue == null) {
            this.attributeValue = "";
            return true;
        } else {
            return this.dropdownValue.isEmpty();
        }
    }

    /**
     * Method to disable or enable the filter-button.
     * If at least one attribute is 'non-empty', the button will be enabled.
     *
     * @return true, if the button shall be disabled or false, if the button shall be enabled
     */
    public boolean filterButtonDisabled(boolean isDetailPage) {

        boolean b1 = this.active.equals(StaffStatusEnums.NO_STATE);
        boolean b2 = this.storeId == 0;
        boolean b3 = (this.dropdownValue == null || this.attributeValue.isEmpty());

        return isDetailPage ? (b1 && b2 && b3) : b3;

    }

    /**
     * Toggles the create view form for staff creation.
     * The create view form is displayed when createView is false and hidden when createView is true.
     */
    public void displayCreateForm() {

        this.creationStateMessage = "";
        this.createView = !this.createView;
    }

    /**
     * Activates and deactivates the display confirmation form for the creation process
     * by toggling the createdConfirmation flag.
     */
    public void confirmCreate() {

        this.createConfirmation = !this.createConfirmation;
    }

    public void displaySuccessMessage() {

        displayCreateForm();
        displayUpdateForm();
        this.showSuccessMessage = !this.showSuccessMessage;
    }

    /**
     * Activates and deactivates the display of the update form
     * by toggling the updateView flag.
     */
    public void displayUpdateForm() {

        this.creationStateMessage = "";
        this.updateView = !this.updateView;
    }

    /**
     * Activates and deactivates the display confirmation form for the update process
     * by toggling the updateConfirmation flag.
     */
    public void confirmUpdate() {

        this.updateConfirmation = !this.updateConfirmation;
    }

    /**
     * Toggles the view of the delete form for a given staff member.
     * Sets the current staff member to be deleted to the given staff object.
     */
    public void displayDeleteForm() {

        this.staff = staffService.findStaffById(retrieveStaffId());
        this.creationStateMessage = "";
        this.deleteView = !this.deleteView;
    }

    /**
     * Returns a list of all the stores available in the system.
     *
     * @return List of Store objects representing the stores.
     */
    public List<Store> listStores() {
        return staffService.getAllStores();
    }

    /**
     * Returns a list of all the staff available in the system,
     * used for displaying stuff-entities in the manager dropdown.
     *
     * @return List of Staff objects representing the stores.
     */
    public List<Staff> listStaff() {
        return staffService.getAllStaff();
    }

    /**
     * Transforms the phone number of the staff member into a more readable format.
     * The method assumes that the phone number is a 10-digit number without any separators.
     * The transformed format is "(XXX) YYY-ZZZZ", where X is the area code, Y is the exchange code, and Z is the subscriber number.
     *
     * @return the transformed phone number as a string in the format "(XXX) YYY-ZZZZ"
     */
    private String transformPhoneNum() {

        String phone = this.staff.getPhone();

        return "(" +
                phone.substring(0, 3) +
                ") " +
                phone.substring(3, 6) +
                "-" +
                phone.substring(6, 10);
    }

    //endregion

    //------------------------GETTERS AND SETTERS----------------------

    //region GETTERS
    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public List<String> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<String> attributeList) {
        this.attributeList = attributeList;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getAllStaffSize() {
        return allStaffSize;
    }

    public Boolean getShowSuccessMessage() {
        return showSuccessMessage;
    }

    public Boolean getShowDeleteMessage() {
        return showDeleteMessage;
    }

    //endregion

    //region SETTERS
    public void setAllStaffSize(Integer allStaffSize) {
        this.allStaffSize = allStaffSize;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getCreationStateMessage() {
        return creationStateMessage;
    }

    public void setCreationStateMessage(String creationStateMessage) {
        this.creationStateMessage = creationStateMessage;
    }

    public Boolean getCreateView() {
        return createView;
    }

    public void setCreateView(Boolean createView) {
        this.createView = createView;
    }

    public Boolean getUpdateView() {
        return updateView;
    }

    public void setUpdateView(Boolean updateView) {
        this.updateView = updateView;
    }

    public Boolean getDeleteView() {
        return deleteView;
    }

    public void setDeleteView(Boolean deleteView) {
        this.deleteView = deleteView;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Boolean getCreateConfirmation() {
        return createConfirmation;
    }

    public void setCreateConfirmation(Boolean createConfirmation) {
        this.createConfirmation = createConfirmation;
    }

    public Boolean getUpdateConfirmation() {
        return updateConfirmation;
    }

    public void setUpdateConfirmation(Boolean updateConfirmation) {
        this.updateConfirmation = updateConfirmation;
    }

    public void setShowSuccessMessage(Boolean showSuccessMessage) {
        this.showSuccessMessage = showSuccessMessage;
    }

    public void setShowDeleteMessage(Boolean showDeleteMessage) {
        this.showDeleteMessage = showDeleteMessage;
    }

    //endregion
}
