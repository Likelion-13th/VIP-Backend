package likelion13th.princess_edition.domain;

import jakarta.persistence.*;
import likelion13th.princess_edition.global.constant.OrderStatus;

@Entity
@Table(name = "orders") // 예약어 회피
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int finalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // 다대일 관계
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    public Order() {}

    // 초기화
    public Order(Item item, int quantity, int totalPrice, int finalPrice) {
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
        this.status = OrderStatus.PROCESSING;
    }

    // 상태 업데이트
    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    // Getter / Setter
    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Item getItem() {
        return item;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
