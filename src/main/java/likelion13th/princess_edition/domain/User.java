package likelion13th.princess_edition.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    private boolean deletable = true;

    @Column(nullable = false)
    private int maxMileage = 0;

    @Column(nullable = false)
    private int recentTotal = 0;

    // 주소 정보
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "zipcode", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "address", nullable = false)),
            @AttributeOverride(name = "addressDetail", column = @Column(name = "address_detail", nullable = false))
    })
    private Address address;

    // 주문 정보
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();


    public User() {}

    // 생성자
    public User(String nickname, String phoneNumber, Address address) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getter / Setter
    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public int getMaxMileage() {
        return maxMileage;
    }

    public int getRecentTotal() {
        return recentTotal;
    }

    public Address getAddress() {
        return address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // 주문 추가
    public void addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
    }

    // 마일리지 차감
    public void useMileage(int mileage) {
        if (mileage < 0) return;
        if (this.maxMileage >= mileage) {
            this.maxMileage -= mileage;
        }
    }

    // 마일리지 적립
    public void addMileage(int mileage) {
        if (mileage > 0) {
            this.maxMileage += mileage;
        }
    }

    // 총 결제 금액 추가
    public void updateRecentTotal(int amount) {
        int total = this.recentTotal + amount;
        if (total >= 0) {
            this.recentTotal = total;
        }
    }
}