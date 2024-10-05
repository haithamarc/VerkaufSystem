package boundary;

import boundary.abstracts.AbstractPaginatedView;
import control.ProductService;
import entity.Brand;
import entity.Category;
import entity.Product;
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
public class ProductView extends AbstractPaginatedView implements Serializable {

    //region ATTRIBUTES
    @Inject
    ProductService ProductService;
    private List<Product> productList;
    private Product product;
    private Integer brandId = 0;
    private Integer categoryId = 0;
    private List<String> attributeList = new ArrayList<>();
    private String dropdownValue = null;
    private String attributeValue = "";
    private String errorMessage = "";
    private Integer allProductSize;
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
     * Initializes the product list by calling the ProductService to get all the products and sets up the pagination values.
     * The method is annotated with @PostConstruct, so it will be automatically called after the bean has been constructed and all
     * dependency injection has taken place.
     * If the ProductService is not initialized properly, a NullPointerException will be thrown.
     * The method also initializes the attributeList by adding the "id" and "Name" attributes, and sets the allProductSize field to the
     * total number of products in the database.
     * @throws NullPointerException if the ProductService is not initialized properly
     */
    @PostConstruct
    public void listAllProducts() {
        try {
            if (this.attributeList.isEmpty()) {
                this.attributeList.add("Id");
                this.attributeList.add("Name");
            }
            findProduct();
            this.creationStateMessage = "";
            this.allProductSize = ProductService.getAllProducts().size();
            this.productList = ProductService.getAllProductsFilter(0, 0, "", "", currentPage, itemsPerPage);
        } catch (NullPointerException e) {
            throw new NullPointerException("ProductService is not initialized properly.");
        }
    }

    /**
     * This method retrieves the product with the specified ID by calling the findProductById method of productService.
     */
    public void findProduct() {

        if(retrieveProductId() != null) {
            this.product = ProductService.findProductById(retrieveProductId());

            if(this.product != null) {
                this.categoryId = this.product.getCategory() != null
                        ? this.product.getCategory().getCategoryId()
                        : 0;
                this.brandId = this.product.getBrand() != null
                        ? this.product.getBrand().getBrandId()
                        : 0;
                return;
            }
        }
        this.product = new Product();
    }

    // Calculate the total page count based on the size of all products and items per page
    public Integer getTotalPageCount() {
        return (this.allProductSize + itemsPerPage - 1) / itemsPerPage;
    }

    // Refresh the current page of elements, displaying products on the page
    public void refreshElementsPage() {
        this.listProductsFiltered(false);
    }

    // Reset the elements page to the first page and refresh it
    public void resetElementsPage() {
        firstPage();
        this.refreshElementsPage();
    }

    /**
     * Retrieves the ID of a product from the request parameters of the current FacesContext.
     *
     * @return the ID of the product retrieved from the request parameters of the current FacesContext
     */
    public Integer retrieveProductId() {

        FacesContext context = FacesContext.getCurrentInstance();
        if(context.getExternalContext().getRequestParameterMap().get("productId") != null) {
            return Integer.valueOf(context.getExternalContext().getRequestParameterMap().get("productId"));
        } else {
            return null;
        }
    }

    /**
     * Method used to reset all filters to the default values.
     */
    public void resetFilter() {
        this.brandId = 0;
        this.categoryId = 0;
        this.dropdownValue = null;
        this.attributeValue = "";
        this.errorMessage = "";
        this.creationStateMessage = "";
        this.createView = false;
        this.deleteView = false;
        this.updateView = false;
        currentPage = 1;
        itemsPerPage = 10;
        this.listAllProducts();
    }

    /**
     * Creates a new product with the data provided by the user, and persists it through the ProductService.
     */
    public void createProduct() {

        this.product.setBrand(ProductService.getBrandById(this.brandId));
        this.product.setCategory(ProductService.getCategoryById(this.categoryId));
        ProductService.persistProduct(this.product);
        this.creationStateMessage =
                "Product " + product.getProductId() + " with name " + product.getProductName() + " successfully created!";
        this.productList = ProductService.getAllProducts();
        confirmCreate();
        this.showSuccessMessage = true;
    }

    /**
     * Updates a product with the data submitted in the form.
     * Uses the values in the class's product object to update it in the ProductService.
     * After the update, the method sets the creationStateMessage field to a success message.
     */
    public void updateProduct() {

        this.product.setBrand(ProductService.getBrandById(this.brandId));
        this.product.setCategory(ProductService.getCategoryById(this.categoryId));

        ProductService.updateProduct(
                retrieveProductId(),
                this.product
        );

        this.creationStateMessage =
                "Product " + retrieveProductId() + " with name " + this.product.getProductName() + " successfully updated!";
        this.updateView = false;
        this.updateConfirmation = false;
        this.showSuccessMessage = true;
    }

    /**
     * Deletes a product from the system and sends the User back to the master-version of
     * the products page.
     *
     * @return the path to the product-master-page.
     */
    public void deleteProduct() {

        this.creationStateMessage =
                "Product " + retrieveProductId() + " with name " + this.product.getProductName() + " successfully deleted!";
        ProductService.deleteProduct(retrieveProductId());
        this.deleteView = false;
        this.showDeleteMessage = true;
    }

    /**
     * Checks if the customer can be safely deleted from the system by determining if the customer has any associated orders.
     *
     * @return true if the customer has no associated orders, false otherwise.
     */
    public boolean productIsDeletable() {
        return ProductService.getAllProductsOfOrderItem(retrieveProductId()).equals(0);
    }

    /**
     * Refreshes the list with a new list of products, optionally with selected filters.
     *
     * @param wasFiltered to determine whether it was a filtered search or not.
     */
    public void listProductsFiltered(boolean wasFiltered) {
        List<Product> tempList = ProductService.getAllProductsFilter(
                this.brandId, this.categoryId, this.dropdownValue, this.attributeValue, 1, 10000
        );
        this.allProductSize = (tempList != null) ? tempList.size() : this.allProductSize;

        tempList = ProductService.getAllProductsFilter(
                this.brandId, this.categoryId, this.dropdownValue, this.attributeValue, currentPage, itemsPerPage
        );

        // Checks, whether a list contains any products or not
        if(tempList != null) {
            if(wasFiltered) currentPage = 1;
            this.productList = tempList;
            this.errorMessage = "";
        } else {
            this.errorMessage = "Error! Please enter a valid number.";
            this.attributeValue = "";
        }
    }

    /**
     * Method to disable or enable the filter-button.
     * If at least one attribute is 'non-empty', the button will be enabled.
     *
     * @return true, if the button shall be disabled or false, if the button shall be enabled
     */
    public boolean filterButtonDisabled(boolean isDetailPage) {

        boolean b1 = this.brandId.equals(0);
        boolean b2 = this.categoryId.equals(0);
        boolean b3 = (this.dropdownValue == null || this.attributeValue.isEmpty());

        return isDetailPage ? (b1 && b2 && b3) : b3;
    }

    /**
     * Method to disable or enable the search-text-field.
     * If an attribute is selected (dropdownValue), then the text-field is enabled.
     *
     * @return true, if the text-field shall be disabled or false, if the text-field shall be enabled
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
     * Displays the create form view by setting the createView flag to true.
     */
    public void displayCreateForm() {

        this.creationStateMessage = "";
        this.createView = !this.createView;
    }

    /**
     * Activates and deactivates the confirmation form for the creation process
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
     * Activates and deactivates the update form by toggling the editView flag and
     * pre-populating the form fields with the values of the product to be updated.
     */
    public void displayUpdateForm() {

        this.creationStateMessage = "";
        this.updateView = !this.updateView;
    }

    /**
     * Activates and deactivates the display of the confirmation form for the update process
     * by toggling the updatedConfirmation flag.
     */
    public void confirmUpdate() {

        this.updateConfirmation = !this.updateConfirmation;
    }

    /**
     * Activates and deactivates the display of the delete form by toggling the deleteView flag
     * and setting the currentProduct field to the product to be deleted.
     */
    public void displayDeleteForm() {

        this.product = ProductService.findProductById(retrieveProductId());
        this.creationStateMessage = "";
        this.deleteView = !this.deleteView;
    }

    /**
     * Method to return all brands from the database.
     *
     * @return List of brands from database
     */
    public List<Brand> listBrands() {
        return ProductService.getAllBrands();
    }

    /**
     * Method to return all categories from the database.
     *
     * @return List of categories from database
     */
    public List<Category> listCategories() {
        return ProductService.getAllCategories();
    }

    //endregion

    //------------------------GETTERS AND SETTERS----------------------

    //region GETTERS
    public List<Product> getProductList() {
        return this.productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getAttributeList() {
        return attributeList;
    }

    public Boolean getCreateConfirmation() {
        return createConfirmation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getAllProductSize() {
        return allProductSize;
    }

    public String getDropdownValue() {
        return dropdownValue;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public boolean isCreateView() {
        return createView;
    }

    public String getCreationStateMessage() {
        return creationStateMessage;
    }

    public Boolean getUpdateView() {
        return updateView;
    }

    public Boolean getCreateView() {
        return createView;
    }

    public Boolean getDeleteView() {
        return deleteView;
    }

    public Boolean getShowSuccessMessage() {
        return showSuccessMessage;
    }

    public Boolean getShowDeleteMessage() {
        return showDeleteMessage;
    }

    //endregion

    //region SETTERS
    public void setAttributeList(List<String> attributeList) {
        this.attributeList = attributeList;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setAllProductSize(Integer allProductSize) {
        this.allProductSize = allProductSize;
    }

    public void setDropdownValue(String dropdownValue) {
        this.dropdownValue = dropdownValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public void setCreateView(Boolean createView) {
        this.createView = createView;
    }

    public void setCreationStateMessage(String creationStateMessage) {
        this.creationStateMessage = creationStateMessage;
    }

    public void setUpdateView(Boolean updateView) {
        this.updateView = updateView;
    }

    public void setDeleteView(Boolean deleteView) {
        this.deleteView = deleteView;
    }

    public Boolean getUpdateConfirmation() {
        return updateConfirmation;
    }

    public void setUpdateConfirmation(Boolean updateConfirmation) {
        this.updateConfirmation = updateConfirmation;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCreateConfirmation(Boolean createConfirmation) {
        this.createConfirmation = createConfirmation;
    }

    public void setShowSuccessMessage(Boolean showSuccessMessage) {
        this.showSuccessMessage = showSuccessMessage;
    }

    public void setShowDeleteMessage(Boolean showDeleteMessage) {
        this.showDeleteMessage = showDeleteMessage;
    }

    //endregion
}
