package likelion13th.princess_edition.DTO.request;

import java.util.List;

// 상품 등록/수정 요청을 받을 때
public class ItemRequest {

    private String itemName;
    private int price;
    private String imagePath;
    private String brand;
    private boolean isNew;
    private List<Long> categoryIds; // 카테고리 ID 목록

    public ItemRequest() {
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

    public boolean isNew() {
        return isNew;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }
}
