package control;

import entity.*;
import entity.enums.StaffStatusEnums;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class StaffService {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Retrieves a list of all Products.
     *
     * @return List of all Products in the database.
     **/
    public List<Staff> getAllStaff() {
        TypedQuery<Staff> query = entityManager.createQuery("SELECT s FROM Staff s ORDER BY s.staffId", Staff.class);
        return query.getResultList();
    }
    public Staff findStaffByEmail(String email){
        return entityManager.createNamedQuery("Staff.findByEmail", Staff.class)
                .setParameter("email", email)
                .getSingleResult();
    }
    /**
     *
     * Finds a staff member by their ID.
     *
     * @param staffId the ID of the staff member to find.
     * @return the staff member with the specified ID, or null if not found.
     */
    public Staff findStaffById(Integer staffId) {

        return entityManager.find(Staff.class, staffId);
    }

    /**
     * Persists a new staff member to the database with the specified manager and store IDs.
     *
     * @param staff the staff member to persist.
     */
    @Transactional
    public void persistStaff(Staff staff) {

        entityManager.persist(staff);
    }

    /**
     * Updates an existing staff member in the database with the specified information.
     *
     * @param staffId the ID of the staff member to update.
     */
    @Transactional
    public void updateStaff(
            Integer staffId, Staff staff) {

        Staff staffNew = entityManager.find(Staff.class, staffId);

        staffNew.setActive(staff.getActive());
        staffNew.setEmail(staff.getEmail());
        staffNew.setFirstName(staff.getFirstName());
        staffNew.setLastName(staff.getLastName());
        staffNew.setPhone(staff.getPhone());
        staffNew.setManager(staff.getManager());
        staffNew.setStore(staff.getStore());
    }

    /**
     * Deletes a staff member from the database with the specified ID.
     *
     * @param staffId the ID of the staff member to delete.
     */
    @Transactional
    public void deleteStaff(Integer staffId) {

        Staff staff = entityManager.find(Staff.class, staffId);
        entityManager.remove(staff);
    }

    /**
     * Retrieves a list of all staff members from the database that match the specified filters and pagination parameters.
     *
     * @param storeId the ID of the store where the staff members work, or 0 to retrieve staff members from all stores.
     * @param active the status of the staff members to retrieve (active, inactive, or no status filter).
     * @param extraFilter the attribute to filter on (ID, Manager-Id, First Name, Last Name, Phone, or Email), or an empty string if no extra filter is applied.
     * @param extraValue the value to filter on, or an empty string if no extra filter is applied.
     * @param page the page of results to retrieve.
     * @param limit the maximum number of results per page.
     * @return a list of Staff objects that match the specified filters and pagination parameters.
     */
    public List<Staff> getAllStaffFilter(
            Integer storeId, StaffStatusEnums active, String extraFilter, String extraValue, Integer page, Integer limit) {

        TypedQuery<Staff> query;
        String queryTag = this.createFilterQuery(storeId, active, extraFilter, extraValue);

        query = entityManager.createQuery(queryTag, Staff.class);
        if (storeId != 0) query.setParameter("sId", storeId);
        if (!active.equals(StaffStatusEnums.NO_STATE)) query.setParameter("active", active);
        if(!extraValue.isEmpty()) {
            String extraAtt = "extra";
            switch (extraFilter) {
                case "Id", "Manager-Id" -> {
                    if (this.isNumerical(extraValue)) {
                        query.setParameter(extraAtt, Integer.parseInt(extraValue));
                    } else {
                        return null;
                    }
                }
                case "First Name", "Last Name", "Phone", "Email" -> {
                    query.setParameter(extraAtt, extraValue.concat("%"));
                }
                default -> {
                }
            }
        }
        return query.setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
    }

    /**
     * Generates a query string based on the given parameters to filter the staff.
     *
     * @param storeId an Integer that represents the id of the store to filter on. If it's 0, the filter is ignored.
     * @param active a StaffStatusEnums representing the status to filter on. If StaffStatusEnums.NO_STATE is given, the filter is ignored.
     * @param extraFilter a String that represents the type of the extra filter to apply. If an empty string is given, the filter is ignored.
     * @param extraValue a String that represents the value of the extra filter to apply. If an empty string is given, the filter is ignored.
     * @return a String that represents the generated query string.
     */
    public String createFilterQuery(
            Integer storeId, StaffStatusEnums active, String extraFilter, String extraValue) {

        //Creates Query-Object and StringBuilder to create Query-String
        StringBuilder queryTag = new StringBuilder();
        queryTag.append("SELECT s FROM Staff s ");

        if(storeId != 0) {
            //SELECT-query to get products depending on given Brand-ID
            queryTag.append("WHERE s.store.storeId = :sId ");
        }
        if(!active.equals(StaffStatusEnums.NO_STATE)) {
            //SELECT-query to get products depending on given Brand-ID
            String linkExpression = (storeId != 0) ? "AND " : "WHERE ";
            queryTag.append(linkExpression);
            queryTag.append("s.active = :active ");
        }
        if(!extraValue.isEmpty()) {
            //SELECT-query to get products for given extraAttribute
            //The query will be build different, whether for model-year, price or name is filtered
            String linkExpression = (storeId != 0 || !active.equals(StaffStatusEnums.NO_STATE)) ? "AND " : "WHERE ";
            switch (extraFilter) {
                case "Id" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("s.staffId = :extra ");
                }
                case "Email" ->{
                    queryTag.append(linkExpression);
                    queryTag.append("UPPER(s.email) LIKE UPPER(:extra) ");
                }
                case "First Name" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("UPPER(s.firstName) LIKE UPPER(:extra) ");
                }
                case "Last Name" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("UPPER(s.lastName) LIKE UPPER(:extra) ");
                }
                case "Phone" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("UPPER(s.phone) LIKE UPPER(:extra) ");
                }
                case "Manager-Id" -> {
                    queryTag.append(linkExpression);
                    queryTag.append("s.manager.staffId = :extra ");
                }
                default -> {
                }
            }
        }

        //Finishing with building of the query
        //Usage of query-parameter to defend against SQL-attacks
        queryTag.append("ORDER BY s.staffId");

        return queryTag.toString();
    }

    /**
     * Retrieves a list of all Brands.
     *
     * @return List of all Brands in the database.
     */
    public List<Store> getAllStores() {
        TypedQuery<Store> query = entityManager.createQuery("SELECT s FROM Store s ORDER BY s.storeId", Store.class);
        return query.getResultList();
    }

    public Store findStoreById(Integer storeId) {

        return entityManager.find(Store.class, storeId);
    }

    /**
     * Checks with the usage of RegEx, if a given String has a valid year-format.
     *
     * @param date Potential date-string
     * @return true, if the String has a valid year-format, otherwise false
     */
    public boolean isNumerical(String date) {

        Pattern pattern = Pattern.compile("^(\\d){1,10}$");
        Matcher matcher = pattern.matcher(date);

        return matcher.matches();
    }

    /**
     * Counts the number of orders, that include the given staff by the id.
     *
     * @param staffId The ID of the staff to retrieve all orders with the ID of the staff.
     * @return The number of orders with the specified ID, or null if not found.
     */
    public Integer getAllStaffOfOrder(Integer staffId){
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT (o.staff.staffId) FROM Order o WHERE (o.staff.staffId = :staffId)", Long.class);
        query.setParameter("staffId", staffId);
        return query.getSingleResult().intValue();
    }
}