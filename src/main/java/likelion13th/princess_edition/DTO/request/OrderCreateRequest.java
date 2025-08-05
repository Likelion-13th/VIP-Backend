package likelion13th.princess_edition.DTO.request;

// 주문 생성
public class OrderCreateRequest {

    private Long itemId;
    private int quantity;
    private int mileageToUse;

    public OrderCreateRequest() {
    }

    public Long getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMileageToUse() {
        return mileageToUse;
    }
}