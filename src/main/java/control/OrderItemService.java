package control;

import entity.Order;
import entity.OrderItem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;


@Stateless
public class OrderItemService {
    @PersistenceContext
    EntityManager em;

    /**
     * Retrieves a list of OrderItem objects associated with a specific order ID.
     *
     * @param orderId The order ID for which the OrderItems should be fetched.
     * @return A List of OrderItem objects associated with the provided order ID.
     */
    public List<OrderItem> getAllOrderItemsByOrderId(int orderId) {
        TypedQuery<OrderItem> query = em.createQuery("SELECT oi FROM OrderItem oi WHERE oi.order.orderId =:orderId", OrderItem.class)
                .setParameter("orderId", orderId);
        return query.getResultList();
    }
    public OrderItem getOrderItemsByOrderIdItemId(int orderId,int itemId) {
        TypedQuery<OrderItem> query = em.createQuery("SELECT oi FROM OrderItem oi WHERE oi.order.orderId =:orderId AND oi.itemId=:itemId", OrderItem.class);
        query.setParameter("orderId", orderId);
        query.setParameter("itemId", itemId);
        return query.getSingleResult();
    }
    // update the attributes of order in DB
    public void updateOrderItem(OrderItem orderItem){em.merge(orderItem);}
    public void deleteOrderItem(OrderItem orderItem){
        em.remove(em.merge(orderItem));
    }
}
