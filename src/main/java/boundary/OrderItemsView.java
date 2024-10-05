package boundary;

import control.OrderItemService;
import entity.OrderItem;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.model.DataModel;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@RequestScoped
public class OrderItemsView {
    private String inputQuantity;
    private String inputPrice;
    private String inputDiscount;
    @Inject
    OrderItemService orderItemService;
    private String orderIdentifier;
    private OrderItem selectedOrderItem ;
    private int selectedOrderItemId ;
    private String errorMessage = "";
    private String infoMessage = "";

    // invoke the objects OrderItems from DatenBank
   public void initOrderItem(){
        selectedOrderItem =orderItemService.getOrderItemsByOrderIdItemId(Integer.parseInt(orderIdentifier),selectedOrderItemId) ;
        inputPrice=selectedOrderItem.getListPrice().toString();
        inputDiscount=selectedOrderItem.getDiscount().toString();
        inputQuantity=selectedOrderItem.getQuantity().toString();
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
    // to cancel and go back
    public String cancel(){

        return "/protected/master_pages/orders.xhtml" ;
    }
    // to update a selected OrderItem in the page
    public String updateOrderItem(){
        selectedOrderItem =orderItemService.getOrderItemsByOrderIdItemId(Integer.parseInt(orderIdentifier),selectedOrderItemId) ;
        selectedOrderItem.setQuantity(Integer.parseInt(inputQuantity));
        selectedOrderItem.setListPrice(new BigDecimal(inputPrice));
        selectedOrderItem.setDiscount(new BigDecimal(inputDiscount));
        orderItemService.updateOrderItem(selectedOrderItem);
        infoMessage = "Order Item is successfully updated ";
        //return cancel();
        return "/protected/master_detail_pages/orderItems.xhtml?orderId=" +orderIdentifier;
    }
    // to delete a selected  OrderItem from the  page
    public String deleteOrderItem(){
        selectedOrderItem =orderItemService.getOrderItemsByOrderIdItemId(Integer.parseInt(orderIdentifier),selectedOrderItemId) ;
        orderItemService.deleteOrderItem(selectedOrderItem);
        infoMessage = "Order Item deleted successfully";
        return "/protected/master_detail_pages/orderItems.xhtml?orderId=" +orderIdentifier;
    }
    // Setters and Getters
    public String getInputPrice() {
        return inputPrice;
    }

    public String getInputDiscount() {
        return inputDiscount;
    }

    public void setInputPrice(String inputPrice) {
        this.inputPrice = inputPrice;
    }
    public String getInputQuantity() {
        return inputQuantity;
    }

    public void setInputQuantity(String inputQuantity) {
        this.inputQuantity = inputQuantity;
    }
    public void setInputDiscount(String inputDiscount) {
        this.inputDiscount = inputDiscount;
    }
    public int getSelectedOrderItemId() {
        return selectedOrderItemId;
    }
    public OrderItem getSelectedOrderItem() {
        return selectedOrderItem;
    }
    public void setSelectedOrderItem(OrderItem selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }
    public OrderItemService getOrderItemService() {
        return orderItemService;
    }
    public void setOrderItemService(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }
    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemService.getAllOrderItemsByOrderId(Integer.parseInt(orderIdentifier));
    }
    public boolean isPrice(String price) {
        Pattern pattern = Pattern.compile("(\\d)+(\\.)?(\\d)*");
        Matcher matcher = pattern.matcher(price);

        return matcher.matches();
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public void setSelectedOrderItemId(int selectedOrderItemId) {
        this.selectedOrderItemId = selectedOrderItemId;
    }
    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }
}