package boundary;


import boundary.abstracts.AbstractPaginatedView;
import control.OrderService;
import control.StockService;
import entity.Order;
import entity.OrderItem;
import entity.Stock;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import entity.enums.OrderStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


@Named
@ViewScoped
public class CreateOrderDetailView extends AbstractPaginatedView implements Serializable {
    private Order order;
    private List<OrderItem> orderItems;
    private Integer orderItemsSize;
    private final LinkedHashMap<OrderStatusEnum, Boolean> orderStatus = new LinkedHashMap<>();
    private boolean edit = false;
    private boolean submit;
    @Inject
    OrderService orderService;
    @Inject
    StockService stockService;

    /**
     * a method to save often used data to minimize database accesses
     */
    public void initOrder(Integer orderId) {
        order = orderService.getOrderById(orderId);
        orderItems = order.getOrderItems().stream().sorted(Comparator.comparing(oi -> oi.getProduct().getProductId())).toList();
        orderItemsSize = orderItems.size();
        submit = false;
        updateOrderStatus();
    }

    // to listen to change in OrderStatus
    public void updateOrderStatus(){
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if(!status.equals(OrderStatusEnum.NO_STATE)) {
                orderStatus.put(status, checkStatus(status));
            }
        }
    }
    // to pass  status to order
    public boolean checkStatus(OrderStatusEnum status){
        return switch (status) {
            case NO_STATE -> false;
            case RECEIVED -> order.getOrderStatus().equals(OrderStatusEnum.RECEIVED);
            case IN_PROGRESS-> order.getOrderStatus().equals(OrderStatusEnum.IN_PROGRESS) ;
            case CANCELED -> order.getOrderStatus().equals(OrderStatusEnum.CANCELED) ;
            case COMPLETED -> order.getOrderStatus().equals(OrderStatusEnum.COMPLETED) ;
        };
    }
    // to know if Item is available
    public boolean isInStock(){
        for (OrderItem orderItem: order.getOrderItems()) {
            if(stockService.getStockById(order.getStore(), orderItem.getProduct()).getQuantity() < orderItem.getQuantity()){
                return false;
            }
        }
        return true;
    }
    // used in Shopping cart to get TotalPrice
    public String getTotalPriceByOrderId() {
        BigDecimal sum = BigDecimal.valueOf(0);
        for (OrderItem orderItems : orderItems) {
            sum = sum.add(orderItems.getTotalPrice());
        }
        return new DecimalFormat("0.00").format(sum);
    }
    // Getters and setters
    public String getTotalPriceByOrderItem(OrderItem orderItem) {
        return new DecimalFormat("0.00").format(orderItem.getTotalPrice());
    }

    public String getDiscountAsPercentByOrderItem(OrderItem orderItem) {
        return new DecimalFormat("0").format(orderItem.getDiscount().multiply(BigDecimal.valueOf(100)));
    }

    public Order getOrder() {
        return order;
    }


    public void mergeOrder() {
        orderService.updateOrder(order);
        submit = true;
    }

    public HashMap<OrderStatusEnum, Boolean> getOrderStatus() {
        return orderStatus;
    }

    @Override
    public Integer getTotalPageCount() {
        Integer totalCustomersNumber = orderItems.size();
        return (totalCustomersNumber + itemsPerPage - 1) / itemsPerPage;
    }
    public List<OrderItem> getOrderItemPage() {
        int from = (itemsPerPage - 1) * itemsPerPage;
        int to = Math.min(from + itemsPerPage, orderItemsSize);
        return orderItems.subList(from, to);
    }
    public Boolean getEdit() {
        return this.edit;
    }

    public boolean isSubmit() {
        return submit;
    }
}


