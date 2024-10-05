package control;


import entity.Brand;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

/**
 * The class BrandService is for the link between the Entity Customer and the database table customers
 */
@Stateless
public class BrandService {

    @PersistenceContext
    EntityManager em;

    public List<Brand> getAllBrands(){
        return em.createNamedQuery("Brand.getAll",Brand.class).getResultList();
    }
}