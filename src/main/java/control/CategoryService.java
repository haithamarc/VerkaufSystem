package control;


import entity.Category;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

/**
 * The class CategoryService is for the link between the Entity Customer and the database table customers
 */
@Stateless
public class CategoryService {

    @PersistenceContext
    EntityManager em;

    public List<Category> getAllCategories(){
        return em.createNamedQuery("Category.getAll",Category.class).getResultList();
    }
}