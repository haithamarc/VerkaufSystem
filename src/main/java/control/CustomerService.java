package control;

import entity.Customer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CustomerService {
    @PersistenceContext
    EntityManager entityManager;

    //It is not filtered for the email because the customer's first and last name is already in it.
    /**
     * counts the number of filtered customers
     *
     * @param filterFirstName The first name of the customer to count.
     * @param filterLastName The last name of the customer to count
     * @return The number of customer on the filtered List or 0 if not found.
     */
    public Integer getFilteredCustomersSize(String filterFirstName,
                                            String filterLastName){

        int size;
        TypedQuery<Long> query;
        String queryTag = "SELECT COUNT (c.customerId) FROM " +
                "Customer c WHERE UPPER(c.firstName) LIKE UPPER(:filterFirstName) " +
                "AND UPPER(c.lastName) LIKE UPPER(:filterLastName) ";
        query = entityManager.createQuery(queryTag, Long.class);
        query.setParameter("filterFirstName",filterFirstName + "%")
                .setParameter("filterLastName",filterLastName + "%");
        size = query.getSingleResult().intValue();
        return size;
    }

    /**
     * Retrieves a customer by their first name, lastname, city, street an state.
     *
     * @param page The number from the current page.
     * @param limit The number of all customers on the page.
     * @param filterId The id of the customer to retrieve.
     * @param filterFirstName The first name of the customer to retrieve.
     * @param filterLastName The last name of the customer to retrieve
     * @return The all customer with the specified attribute, or null if not found
     */
    public List<Customer> getFilteredCustomer(Integer page, Integer limit,
                                              Integer filterId,
                                              String filterFirstName,
                                              String filterLastName){
        TypedQuery<Customer> query;
        StringBuilder queryTag = new StringBuilder();
        queryTag.append("SELECT c FROM Customer c ");

        if (filterId != 0){
            queryTag.append("WHERE (c.customerId = :filterId) ");
        }else if (filterFirstName != null && !filterFirstName.isEmpty()){
            queryTag.append("WHERE UPPER(c.firstName) LIKE UPPER(:filterFirstName) ");
        }else if (filterLastName != null && !filterLastName.isEmpty()){
            queryTag.append("WHERE UPPER(c.lastName) LIKE UPPER(:filterLastName) ");
        }
        queryTag.append("ORDER BY c.customerId");
        query = entityManager.createQuery(queryTag.toString(), Customer.class);

        if (filterId != 0) query.setParameter("filterId", filterId);
        else if (filterFirstName != null && !filterFirstName.isEmpty()) query.setParameter("filterFirstName",filterFirstName + "%");
        else if (filterLastName != null && !filterLastName.isEmpty()) query.setParameter("filterLastName",filterLastName + "%");
        return query.setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return The customer with the specified ID, or null if not found.
     */
    public Customer getCustomerById(Integer customerId) {
        return entityManager.find(Customer.class, customerId);
    }

    /**
     * Returns a list of customers with the given email address.
     *
     * @param email the email address to search for
     * @return a list of customers with the given email address
     */
    public List<Customer> getCustomerByEmail(String email){
        return entityManager.createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class).setParameter("email", email).getResultList();
    }
    /**
     * Updates the given customer in the database.
     *
     * @param customer the customer to update.
     */
    public void updateCustomer(Customer customer){entityManager.merge(customer);}

    /**
     * Creates a new customer in the database.
     *
     * @param customer the customer to create.
     */
    public void createCustomer(Customer customer){entityManager.persist(customer);}

    /**
     * Deletes the given customer from the database.
     *
     * @param customerId the ID of customer to delete.
     */
    public void deleteCustomer(Integer customerId){
        entityManager.remove(getCustomerById(customerId));
    }

    /**
     * Gibt eine Liste aller Kunden zurück.
     *
     * @return eine Liste von {@link Customer}-Objekten, die alle Kunden in der Datenbank darstellen.
     */
    public List<Customer> getAllCustomers() {
        TypedQuery<Customer> query = entityManager.createNamedQuery("searchAllCustomer", Customer.class);
        return query.getResultList();
    }

}