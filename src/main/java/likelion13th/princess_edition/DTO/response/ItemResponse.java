package likelion13th.princess_edition.DTO.response;

import likelion13th.princess_edition.domain.Item;

// 상품 정보 응답할 때
public class ItemResponse {

    private Long id;
    private String itemName;
    private int price;
    private String imagePath;
    private String brand;
    private boolean isNew;

    public ItemResponse(Long id, String itemName, int price, String imagePath, String brand, boolean isNew) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.imagePath = imagePath;
        this.brand = brand;
        this.isNew = isNew;
    }

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

    public boolean isNew() {
        return isNew;
    }

    // Item -> DTO로 변환
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getItemName(),  // getter 명칭을 초보자 스타일로 바꿨던 구조 반영
                item.getPrice(),
                item.getImagePath(),
                item.getBrand(),
                item.getIsNew()
        );
    }
}
