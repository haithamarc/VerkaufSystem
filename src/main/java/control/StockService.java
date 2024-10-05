package control;

import entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * The class StoreService is for the link between the Entity Store and the database table stores
 */
@Stateless
public class StockService {

    @PersistenceContext
    EntityManager em;

    public Stock getStockById(Store store, Product product){
        return em.find(Stock.class, new StockIdClass(product, store));
    }

    public void mergeStock(Stock stock){
        em.merge(stock);
    }

    /**
     * a method to get only the stocks that are on the current page
     *
     * @param page  Integer the current page
     * @param limit Integer the total amount of stocks on the current page
     * @return List<Stock>
     */
    public List<Stock> getFilteredStockPage(Integer page, Integer limit, Integer filterModelYear, Brand filterBrand, Category filterCategory, String filterName, Store store) {
        return em.createNamedQuery("Stock.getFilteredStockPage", Stock.class)
                .setParameter("modelYear", filterModelYear)
                .setParameter("brand", filterBrand)
                .setParameter("category", filterCategory)
                .setParameter("name", "%"+filterName+"%")
                .setParameter("store", store)
                .setFirstResult((page-1)*limit)
                .setMaxResults(limit)
                .getResultList();
    }

    public Integer getFilteredStockSize(Integer filterModelYear, Brand filterBrand, Category filterCategory, String filterName, Store store){
        return em.createNamedQuery("Stock.getFilteredStockSize", Long.class)
                .setParameter("modelYear", filterModelYear)
                .setParameter("brand", filterBrand)
                .setParameter("category", filterCategory)
                .setParameter("name", "%"+filterName+"%")
                .setParameter("store", store)
                .getSingleResult().intValue();
    }
}
