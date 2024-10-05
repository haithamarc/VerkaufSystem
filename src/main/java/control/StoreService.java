package control;

import entity.Store;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class StoreService {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Returns the number of stores that match the specified filters.
     *
     * @param filterName A string used to filter by store name. Only stores with names that start with this string will be included.
     *
     * @return The number of stores that match the specified filters.
     */
    public Integer getStoresSize(String filterName){
        return entityManager.createQuery("SELECT COUNT(s.storeId) FROM Store s " +
                        "WHERE UPPER(s.storeName) LIKE UPPER(:filterName) ", Long.class)
                .setParameter("filterName", filterName + "%")
                .getSingleResult()
                .intValue();
    }

    /**
     * Returns a page of stores that match the given filters.
     *
     * @param page The page number.
     * @param limit The maximum number of stores per page.
     * @param filterName The filter for the store name.
     * @return A list of stores that match the given filters for the specified page.
     */
    public List<Store> getStoresPage(Integer page, Integer limit, String filterName){
        return entityManager.createQuery("SELECT s FROM Store s " +
                        "WHERE UPPER(s.storeName) LIKE UPPER(:filterName) " +
                        "ORDER BY s.storeId", Store.class)
                .setParameter("filterName", filterName + "%")
                .setFirstResult((page-1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Retrieves a Store object from the database based on the given storeId.
     *
     * @param storeId the ID of the store to retrieve from the database
     * @return the Store object with the given storeId if it exists in the database, otherwise null.
     */
    public Store getStoreById(Integer storeId){ return entityManager.find(Store.class, storeId);}
}
