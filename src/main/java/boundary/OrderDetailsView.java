package boundary;

import control.CustomerService;
import control.OrderItemService;
import control.OrderService;
import entity.Customer;
import entity.Order;
import entity.OrderItem;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Named
@ViewScoped
public class OrderDetailsView implements Serializable {
    @Inject
    OrderService orderService;
    @Inject
    OrderItemService orderItemService;
    private String orderIdentifier;

    // Injecting services for accessing data and business logic
    @Inject
    CustomerService customerService;
    private Integer orderId;
    private Order order;
    private Integer selectedCustomerId;
    private boolean activeEditForm = false;
    private String orderDate;
    private String requiredDate;
    private String shippedDate;
    // get the Order from the database and pass its parameters to this class
    public void init(){
        order = orderService.getOrderById(orderId);
        orderDate=order.getOrderDate().toString();
        requiredDate=order.getRequiredDate().toString();
        shippedDate=order.getShippedDate().toString();
        selectedCustomerId=order.getCustomer().getCustomerId();
    }
    // to add an order to the database
    public void addOrder(){
        orderService.createOrder(order);
    }
    // to update an order in the database
    public void updateOrder(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localOrderDate = LocalDate.parse(orderDate, dateTimeFormatter);
        LocalDate localRequiredDate = LocalDate.parse(requiredDate, dateTimeFormatter);
        LocalDate localShippedDate = LocalDate.parse(shippedDate, dateTimeFormatter);
        order.setShippedDate(localShippedDate);
        order.setOrderDate(localOrderDate);
        order.setRequiredDate(localRequiredDate);
        // Associate the new order with the selected customer
        order.setCustomer(customerService.getCustomerById(selectedCustomerId));
        orderService.updateOrder(order);
        cancel();
    }
    // to deactivate a button
    public void cancel(){
        activeEditForm = false;
    }
    // when click on the delete  button
    public String deleteOrder(){

        List<OrderItem> orderItems =orderItemService.getAllOrderItemsByOrderId(orderId);
        for(OrderItem orderItem : orderItems){
            orderItemService.deleteOrderItem(orderItem);
        }

        orderService.deleteOrder(order);
        return back();
    }
    public String back(){
        return ("orders.xhtml");
    }
    public void  activeEdit(){
         activeEditForm = true;
    }
    //Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isActiveEditForm() {
        return activeEditForm;
    }

    public void setActiveEditForm(boolean activeEditForm) {
        this.activeEditForm = activeEditForm;
    }
    public Integer getSelectedCustomerId() {
        return selectedCustomerId;
    }

    public void setSelectedCustomerId(Integer selectedCustomerId) {
        this.selectedCustomerId = selectedCustomerId;
    }

    public List<Customer> getCustomerList(){
        return this.customerService.getAllCustomers();
    }
    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }
    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }
    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    /**
     * Calculates the total price of all OrderItems for a specific order and formats the result as a String.
     * The output format will be "##,###.##" (e.g., "9,442.50"), with the grouping separator as '.' and the decimal separator as ','.
     *
     * @return A formatted String representing the total price of all OrderItems for the specified order.
     */
    public String sum() {
        List<OrderItem> orderItems = orderItemService.getAllOrderItemsByOrderId(Integer.parseInt(orderIdentifier));
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal one = BigDecimal.ONE;
        for (OrderItem orderItem : orderItems) {
            BigDecimal itemPrice = orderItem.getListPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            BigDecimal discountAmount = itemPrice.multiply(orderItem.getDiscount());
            BigDecimal discountedPrice = itemPrice.subtract(discountAmount);
            totalPrice = totalPrice.add(discountedPrice);
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(','); // If you need a decimal separator
        DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols); // Replace .## with desired number of decimal places
        formatter.setGroupingSize(3);
        formatter.setGroupingUsed(true);
        String formattedTotalPrice = formatter.format(totalPrice);

        return formattedTotalPrice;
    }
    public List<OrderItem> getOrderItemList() {
        return orderItemService.getAllOrderItemsByOrderId(Integer.parseInt(orderIdentifier));
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

}

