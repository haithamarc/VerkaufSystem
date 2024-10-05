package control;

import entity.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CustomerOrderService {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Retrieves all orders by the ID of customer.
     *
     * @param customerId The ID of the customer to retrieve all orders with the ID of the customer.
     * @return The customer with the specified ID, or null if not found.
     */
    public List<Order> getAllOrdersByCustomerId(Integer customerId){
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o WHERE (o.customer.customerId = :customerId) ORDER BY o.orderId", Order.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    /**
     * Counts the number of orders from their id.
     *
     * @param customerId The ID of the customer to retrieve all order with the ID of the customer.
     * @return The number of orders with the specified ID, or null if not found.
     */
    public Integer getAllOrdersByCustomerIdSize(Integer customerId){
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT (o.customer.customerId) FROM Order o WHERE (o.customer.customerId = :customerId)", Long.class);
        query.setParameter("customerId", customerId);
        return query.getSingleResult().intValue();
    }
}
