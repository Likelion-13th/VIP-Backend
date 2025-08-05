package likelion13th.princess_edition.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private boolean isNew = false;

    // 카테고리와 다대다 관계 (연결 테이블은 Category에서 설정함)
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 주문과 일대다 관계
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    // 기본 생성자
    public Item() {}

    // 주요 필드를 받는 생성자
    public Item(String itemName, int price, String imagePath, String brand, boolean isNew) {
        this.itemName = itemName;
        this.price = price;
        this.imagePath = imagePath;
        this.brand = brand;
        this.isNew = isNew;
    }

    // Getter/Setter
    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getBrand() {
        return brand;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}