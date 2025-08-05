package likelion13th.princess_edition.DTO.response;

import likelion13th.princess_edition.domain.Category;

// 카테고리 정보 응답
public class CategoryResponse {

    private Long id;
    private String name;

    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Category -> DTO로 변환
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}