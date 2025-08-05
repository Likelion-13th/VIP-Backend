package likelion13th.princess_edition.DTO.response;

import likelion13th.princess_edition.domain.Order;
import likelion13th.princess_edition.global.constant.OrderStatus;

import java.time.LocalDateTime;

// 주문 정보 응답할 때
public class OrderResponse {

    private Long orderId;
    private String itemName;
    private int quantity;
    private int totalPrice;
    private int finalPrice;
    private int mileageToUse;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderResponse(Long orderId, String itemName, int quantity, int totalPrice,
                         int finalPrice, int mileageToUse, OrderStatus status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
        this.mileageToUse = mileageToUse;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
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

    public int getMileageToUse() {
        return mileageToUse;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Order-> DTO로 변환
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getItem().getItemName(), // 필드명 변경 반영
                order.getQuantity(),
                order.getTotalPrice(),
                order.getFinalPrice(),
                order.getTotalPrice() - order.getFinalPrice(), // 사용한 마일리지 계산
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
