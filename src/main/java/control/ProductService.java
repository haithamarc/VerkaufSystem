package control;

import entity.Brand;
import entity.Category;
import entity.Product;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class ProductService {


    @PersistenceContext
    EntityManager entityManager;

    /**
     * Persists a new Product with the given parameters in the database.
     * Sets the Brand and Category of the product with the given ids.
     *
     * @param product the Product to be persisted
     */
    @Transactional
    public void persistProduct(Product product) {

        entityManager.persist(product);
    }

    /**
     * This method updates the information of a given product in the database.
     *
     * @param productId The ID of the product to be updated.
     */
    @Transactional
    public void updateProduct(Integer productId, Product product) {

        Product productNew = entityManager.find(Product.class, productId);

        productNew.setListPrice(product.getListPrice());
        productNew.setModelYear(product.getModelYear());
        productNew.setProductName(product.getProductName());
        productNew.setBrand(product.getBrand());
        productNew.setCategory(product.getCategory());
    }

    /**
     * Deletes the product with the specified ID from the database.
     *
     * @param productId the ID of the product to be deleted
     */
    @Transactional
    public void deleteProduct(Integer productId) {

        Product product = entityManager.find(Product.class, productId);
        if(!product.getOrderItems().isEmpty()) return;
        entityManager.remove(product);
    }

    /**
     * Searches for a product with the specified ID in the database.
     *
     * @param productId the ID of the product to search for
     * @return the Product object with the specified ID, or null if no such product exists in the database
     */
    public Product findProductById(Integer productId) {

        return entityManager.find(Product.class, productId);
    }

    /**
     * Retrieves a list of all Products.
     *
     * @return List of all Products in the database.
     **/
    public List<Product> getAllProducts() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p ORDER BY p.productId", Product.class);
        return query.getResultList();
    }

    /**
     * Retrieves all the products that match the specified filter criteria.
     * The method builds a query string using the given filter criteria and retrieves the products from the database.
     * The results are paginated and sorted by product ID in ascending order.
     *
     * @param brandId the ID of the brand to filter by (0 for no filter)
     * @param categoryId the ID of the category to filter by (0 for no filter)
     * @param dropdownValue the value of the dropdown field to filter by ("Model-Year", "Id", "Price", or "Name")
     * @param attributeValue the value of the filter criteria to apply (empty string for no filter)
     * @param page the page number of the results to retrieve (1-based index)
     * @param limit the maximum number of results to retrieve per page
     * @return a list of Product objects that match the specified filter criteria or null if the attributeValue is invalid for the given dropdownValue
     */
    public List<Product> getAllProductsFilter(
            Integer brandId, Integer categoryId, String dropdownValue, String attributeValue, Integer page, Integer limit) {

        //Creates Query-Object and generates the String for the query
        TypedQuery<Product> query;
        String queryString = this.getFilterQuery(brandId, categoryId, dropdownValue, attributeValue);

        //Usage of query-parameter to defend against SQL-attacks
        query = entityManager.createQuery(queryString, Product.class);
        if (brandId != 0) query.setParameter("bId",  brandId);
        if (categoryId != 0) query.setParameter("cId",  categoryId);
        if(!attributeValue.isEmpty()) {
            String extraAtt = "extra";
            switch (dropdownValue) {
                case "Model-Year", "Id" -> {
                    if (this.isDate(attributeValue)) {
                        query.setParameter(extraAtt, Integer.parseInt(attributeValue));
                    } else {
                        return null;
                    }
                }
                case "Price" -> {
                    if (this.isPrice(attributeValue)) {
                        query.setParameter(extraAtt, BigDecimal.valueOf(Double.parseDouble(attributeValue)));
                    } else {
                        return null;
                    }
                }
                case "Name" -> {
                    query.setParameter(extraAtt, attributeValue.concat("%"));
                }
                default -> {
                }
            }
        }
        return query.setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
    }

    /**
     * This method generates a filter query string based on the given brandId, categoryId, extraFilter, and attributeValue parameters.
     * The generated query string is then returned as a string.
     *
     * @param brandId the ID of the brand to filter by, or 0 if no brand filtering is required.
     * @param categoryId the ID of the category to filter by, or 0 if no category filtering is required.
     * @param selectedAttribute the name of the attribute in dropdown-filter to apply, or an empty string if no extra filtering is required.
     * @param attributeValue the value of the selectedAttribute in dropdown-filter to apply, or an empty string if no extra filtering is required.
     * @return a String representing the filter query.
     */
    private String getFilterQuery(Integer brandId, Integer categoryId, String selectedAttribute, String attributeValue) {

        //Initialization of StringBuilder to create the query-String
        StringBuilder queryTag = new StringBuilder();
        queryTag.append("SELECT p FROM Product p ");

        if(brandId != 0) {
            //SELECT-query to get products depending on given Brand-ID
            queryTag.append("WHERE p.brand.brandId = :bId ");
        }
        if(categoryId != 0) {
            //SELECT-query to get products depending on given Category-ID
            String linkExpression = (brandId != 0) ? "AND " : "WHERE ";
            queryTag.append(linkExpression);
            queryTag.append("p.category.categoryId = :cId ");
        }
        if(!attributeValue.isEmpty()) {
            //SELECT-query to get products for given attributeValue
            //The query will be build different, whether for model-year, price or name is filtered
            String linkExpression = (brandId != 0 || categoryId != 0) ? "AND " : "WHERE ";
            switch (selectedAttribute) {
                case "Id" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("p.productId = :extra ");
                }
                case "Model-Year" ->{
                    queryTag.append(linkExpression);
                    queryTag.append("p.modelYear = :extra ");
                }
                case "Price" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("p.listPrice = :extra ");
                }
                case "Name" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("UPPER(p.productName) LIKE UPPER(:extra) ");
                }
                default -> {
                }
            }
        }

        //Finishing with building of the query
        queryTag.append("ORDER BY p.productId");

        return queryTag.toString();
    }

    /**
     * Retrieves a list of all Brands.
     *
     * @return List of all Brands in the database.
     */
    public List<Brand> getAllBrands() {
        TypedQuery<Brand> query = entityManager.createQuery("SELECT b FROM Brand b ORDER BY b.brandId", Brand.class);
        return query.getResultList();
    }

    public Brand getBrandById(Integer brandId) {

        return entityManager.find(Brand.class, brandId);
    }

    /**
     * Retrieves a list of all Categories.
     *
     * @return List of all Categories in the database.
     */
    public List<Category> getAllCategories() {
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c ORDER BY c.categoryId", Category.class);
        return query.getResultList();
    }
    public List<Integer> getModelYears(){
        return entityManager.createNamedQuery("Product.getModelYears", Integer.class).getResultList();
    }

    /**
     * Retrieves the category with the specified ID from the database.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the Category object with the specified ID, or null if no such category exists in the database
     */
    public Category getCategoryById(Integer categoryId) {

        return entityManager.find(Category.class, categoryId);
    }

    /**
     * Checks with the usage of RegEx, if a given String has a valid year-format.
     *
     * @param date Potential date-string
     * @return true, if the String has a valid year-format, otherwise false
     */
    public boolean isDate(String date) {

        Pattern pattern = Pattern.compile("(\\d){1,10}");
        Matcher matcher = pattern.matcher(date);

        return matcher.matches();
    }

    /**
     * Checks with the usage of RegEx, if a given String has a valid price-format.
     *
     * @param price Potential price-string
     * @return true, if the String has a valid price-format, otherwise false
     */
    public boolean isPrice(String price) {

        Pattern pattern = Pattern.compile("(\\d)+(\\.)?(\\d)*");
        Matcher matcher = pattern.matcher(price);

        return matcher.matches();
    }

    /**
     * Counts the number of order-items, that include the given product by the id.
     *
     * @param productId The ID of the product to retrieve all order-items with the ID of the product.
     * @return The number of order-items with the specified ID, or null if not found.
     */
    public Integer getAllProductsOfOrderItem(Integer productId){
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT (o.product.productId) FROM OrderItem o WHERE (o.product.productId = :productId)", Long.class);
        query.setParameter("productId", productId);
        return query.getSingleResult().intValue();
    }
}

