package control;

import entity.Customer;
import entity.Order;

import entity.OrderItem;
import entity.enums.OrderStatusEnum;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static entity.enums.OrderStatusEnum.NO_STATE;

@Stateless
public class OrderService {
    @PersistenceContext
    EntityManager em;
    // to get All the Orders from DB
    public List<Order> getAllOrders() {
        TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o ORDER BY o.orderId", Order.class);
        return query.getResultList();
    }
    // to filter according to the param.
    public List<Order> getAllOrdersFilter(
            String orderId,OrderStatusEnum orderStatus,String extraFilter, String extraValue, Integer page, Integer limit) {
        StringBuilder queryTag = new StringBuilder();
        queryTag.append("SELECT o FROM Order o ");
        if(!orderId.isEmpty()) {
            if(this.isPositiveInteger(orderId)) {
                //SELECT-query to get products depending on given Brand-ID
                queryTag.append("WHERE o.orderId = :oId ");
            }else {
                return null;
            }
        }
        if(orderStatus != NO_STATE  ) {
            String linkExpression = (!orderId.isEmpty()  ) ? "AND " : "WHERE ";
            queryTag.append(linkExpression);
            queryTag.append(" o.orderStatus = :orderStatus ");
        }
        if(!extraValue.isEmpty()) {
            String linkExpression = (!orderId.isEmpty()|| orderStatus != NO_STATE   ) ? "AND " : "WHERE ";
            switch (extraFilter) {
                case "OrderDate" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("o.orderDate = :extra ");
                }
                case "RequiredDate" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("o.requiredDate = :extra ");
                }
                case "ShippedDate" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("o.shippedDate = :extra ");
                }
                default -> {
                }
            }
        }
        queryTag.append("ORDER BY o.orderId");
       return getAllOrdersFilterQuery(queryTag,orderId,extraValue,orderStatus,page,limit);
    }
    // assign parameters to Jpa Query with paging
    public List<Order> getAllOrdersFilterQuery(StringBuilder  queryTag, String orderId,
                                               String extraValue,OrderStatusEnum orderStatus,
                                               Integer page, Integer limit) {
        TypedQuery<Order> query = em.createQuery(queryTag.toString(), Order.class);
        if (!orderId.isEmpty()) query.setParameter("oId", Integer.parseInt(orderId));
        if (orderStatus != NO_STATE ) query.setParameter("orderStatus",  orderStatus);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(!extraValue.isEmpty()) {
            LocalDate date = LocalDate.parse(extraValue, dateTimeFormatter);
            String extraAtt = "extra";
            query.setParameter(extraAtt,date);

        }
        return query.setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
    }
    // to assert that the input is valid positive number
    public  boolean isPositiveInteger(String str) {
        try {
            Integer.parseInt(str);
            int number = Integer.parseInt(str);
            if (number < 1) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Persists a new Order object in the database.
     *
     * @param order The Order object to be persisted.
     */
    public void saveOrder(Order order) {
        em.persist(order);
    }
    /**
     * Retrieves a customer by their ID.
     *
     * @param orderId ID of the customer to retrieve.
     * @return The customer with the specified ID, or null if not found.
     */
    public Order getOrderById(Integer orderId) {
        return em.find(Order.class, orderId);
    }
    // update the attributes of order in DB
    public void updateOrder(Order order){em.merge(order);}
    // it creates an order in DB
    public void createOrder(Order order){em.persist(order);}
    public void deleteOrder(Order order ){
        em.remove(em.merge(order));
    }

}
