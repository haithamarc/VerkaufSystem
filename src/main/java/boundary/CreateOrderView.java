package boundary;

import boundary.abstracts.AbstractPaginatedView;
import entity.enums.OrderStatusEnum;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


import control.*;
import entity.*;
import control.OrderStage;
import jakarta.security.enterprise.SecurityContext;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Named
@ViewScoped
public class CreateOrderView extends AbstractPaginatedView implements Serializable {

    private Store store;
    private final List<OrderItem> filteredOrderItems = new ArrayList<>();
    private Integer filteredOrderItemsSize;
    private String filterName;
    private Integer filterBrandId;
    private Integer filterCategoryId;
    private Integer filterModelYear;
    private OrderStage orderStage;
    private final HashMap<Integer, Category> categories = new LinkedHashMap<>();
    private final HashMap<Integer, Brand> brands = new LinkedHashMap<>();
    private final HashMap<Integer, Integer> stock = new LinkedHashMap<>();
    private List<Integer> modelYears;
    private Customer customer;
    private Staff staff;
    private OrderStatusEnum OrderStatus;
    private final List<OrderItem> orderItemsInShoppingCart = new ArrayList<>();

    @Inject
    private BrandService brandService;
    @Inject
    private CategoryService categoryService;
    @Inject
    private StaffService staffService;
   @Inject
    private SecurityContext securityContext;
    @Inject
    private OrderService orderService;
    @Inject
    private StockService stockService;
    @Inject
    private ProductService ProductService;
    private boolean addOrderItemActive=false ;
    private OrderItem orderItem;
    // to move us to another Stage
    @PostConstruct
    public void init() {
        orderStage = OrderStage.SELECT_CUSTOMER;
    }
    // to see  the available Products in a certain stock
    public Integer getStockForSelectedProduct() {
        return stock.get(orderItem.getProduct().getProductId());
    }
    // add orderItem to order
    public void addToOrder() {
        orderItemsInShoppingCart.add(orderItem.clone());
        addOrderItemActive= true ;
    }

    // Reset the elements page to the first page and refresh it
    public void resetElementsPage() {
        firstPage();
        refreshElementsPage();
    }
    // to determine  a customer object for order
    public void selectCustomer(Customer customer) {
        FacesContext.getCurrentInstance().validationFailed();
        this.customer = customer;
        proceed();
        staff = staffService.findStaffByEmail(securityContext.getCallerPrincipal().getName());
        store = staff.getStore();
        refreshElementPage();
        modelYears = ProductService.getModelYears();
        categoryService.getAllCategories().forEach(category -> categories.put(category.getCategoryId(), category));
        brandService.getAllBrands().forEach(brand -> brands.put(brand.getBrandId(), brand));
    }
    // to determine  a orderitem object for order
    public void selectOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        addOrderItemActive = false;
        proceed();
    }
    // to create order
    public void createOrder() {
        Order order = new Order(LocalDate.now(), OrderStatus.NO_STATE, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), customer,store,staff );
        for (OrderItem orderItem : orderItemsInShoppingCart) {
            order.addOrderItem(orderItem);
        }
        orderService.createOrder(order);
    }

    /**
     *   to chang the Order Stage  to proceed the Order
     */
    public void proceed() {
        if (!FacesContext.getCurrentInstance().isValidationFailed()) {
            FacesContext.getCurrentInstance().validationFailed();
        }
        orderStage = OrderStage.values()[orderStage.ordinal() + 1];
    }

    /**
     * a method to step back in the Order by changing the Order Stage
     */
    public void back() {
        if (!FacesContext.getCurrentInstance().isValidationFailed()) {
            FacesContext.getCurrentInstance().validationFailed();
        }
        orderStage = OrderStage.values()[orderStage.ordinal() - 1];
    }

    /**
     * a method to change the orderStage to VIEW_CART
     */
    public void toShoppingCart() {
        if (!FacesContext.getCurrentInstance().isValidationFailed()) {
            FacesContext.getCurrentInstance().validationFailed();
        }
        orderStage = OrderStage.VIEW_CART;
    }

    /**
     * a method to change the orderStage to SELECT_PRODUCTS
     */
    public void toSelectProducts() {
        if (!FacesContext.getCurrentInstance().isValidationFailed()) {
            FacesContext.getCurrentInstance().validationFailed();
        }
        orderStage = OrderStage.SELECT_PRODUCTS;
    }
    // to validate the Quantity of avialble products
    public void validateQuantity(FacesContext facesContext, UIComponent uiComponent, String value) throws ValidatorException {
        try {
            int quantity = Integer.parseInt(value);
            int cartQuantity = 0;
            for (OrderItem orderItem : orderItemsInShoppingCart) {
                if (orderItem.getProduct().getProductId() == orderItem.getProduct().getProductId()) {
                    cartQuantity += orderItem.getQuantity();
                }
            }
            if ((quantity + cartQuantity) > getStockForSelectedProduct()) {
                ((UIInput) uiComponent).setValid(false);
                FacesMessage message = new FacesMessage("We do not have these amount in the Depot!");
                facesContext.addMessage(uiComponent.getClientId(facesContext), message);
            }
        } catch (NumberFormatException e) {
            ((UIInput) uiComponent).setValid(false);
            FacesMessage message = new FacesMessage("that is not allowed number!");
            facesContext.addMessage(uiComponent.getClientId(facesContext), message);
        }
    }

    public void refreshElementPage() {
        List<Stock> stockPage = stockService.getFilteredStockPage(currentPage, itemsPerPage, filterModelYear, brands.get(filterBrandId), categories.get(filterCategoryId), filterName == null ? "" : filterName, store);
        filteredOrderItemsSize = stockService.getFilteredStockSize(filterModelYear, brands.get(filterBrandId), categories.get(filterCategoryId), filterName == null ? "" : filterName, store);
        stockPage.forEach(s -> stock.put(s.getProduct().getProductId(), s.getQuantity()));
        filteredOrderItems.clear();
        stockPage.forEach(s -> filteredOrderItems.add(new OrderItem(s.getProduct())));
    }

    // Getter and Setter
    public Integer getFilteredOrderItemsSize() {
        return filteredOrderItemsSize;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Integer getFilterBrandId() {
        return filterBrandId;
    }

    public void setFilterBrandId(Integer filterBrandId) {
        this.filterBrandId = filterBrandId;
    }

    public Integer getFilterCategoryId() {
        return filterCategoryId;
    }

    public void setFilterCategoryId(Integer filterCategoryId) {
        this.filterCategoryId = filterCategoryId;
    }

    public HashMap<Integer, Category> getCategories() {
        return categories;
    }

    public HashMap<Integer, Brand> getBrands() {
        return brands;
    }

    public Integer getFilterModelYear() {
        return filterModelYear;
    }

    public void setFilterModelYear(Integer filterModelYear) {
        this.filterModelYear = filterModelYear;
    }

    public List<Integer> getModelYears() {
        return modelYears;
    }

    public OrderStage getOrderStage() {
        return orderStage;
    }

    public void setOrderStage(OrderStage orderStage) {
        this.orderStage = orderStage;
    }
    // to select customer for order

    public void refreshElementsPage() {
        refreshElementPage();
    }
    public List<OrderItem> getOrderItemsInShoppingCart() {
        return orderItemsInShoppingCart;
    }

    public List<OrderItem> getFilteredOrderItems() {
        return filteredOrderItems;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }
    public boolean isAddOrderItemActive() {
        return addOrderItemActive;
    }

    public void setAddOrderItemActive(boolean addOrderItemActive) {
        this.addOrderItemActive = addOrderItemActive;
    }

    public Integer getTotalPageNumber() {
        return (filteredOrderItemsSize + itemsPerPage - 1) / itemsPerPage;
    }

    @Override
    public Integer getTotalPageCount() {
        return  (filteredOrderItemsSize + itemsPerPage - 1) / itemsPerPage;
    }
    public Store getStore() {
        return store;
    }

}
