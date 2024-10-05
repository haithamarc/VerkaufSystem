package entity;

import jakarta.persistence.*;

@NamedQuery(name = "Category.getAll", query = "SELECT c from Category c ORDER BY c.categoryId")
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_Id")
    private Integer categoryId;

    @Column(name = "category_name")
    private String categoryName;

    public Category() {
    }


    //Getters and Setters
    public Integer getCategoryId() { return categoryId; }

    public String getCategoryName() {
        return categoryName;
    }

}
