package likelion13th.princess_edition.Controller;

import likelion13th.princess_edition.DTO.request.CategoryRequest;
import likelion13th.princess_edition.DTO.response.CategoryResponse;
import likelion13th.princess_edition.DTO.response.ItemResponse;
import likelion13th.princess_edition.service.CategoryService;
import likelion13th.princess_edition.global.api.ApiResponse;
import likelion13th.princess_edition.global.api.SuccessCode;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 전체 카테고리 보기
    @GetMapping("/list")
    public ApiResponse<List<CategoryResponse>> listCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ApiResponse.onSuccess(SuccessCode.OK, categories);
    }

    // 카테고리 하나 보기
    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategory(id);
        return ApiResponse.onSuccess(SuccessCode.OK, category);
    }

    // 새 카테고리 등록
    @PostMapping("/add")
    public ApiResponse<CategoryResponse> addCategory(@RequestBody CategoryRequest request) {
        CategoryResponse saved = categoryService.createCategory(request);
        return ApiResponse.onSuccess(SuccessCode.CREATED, saved);
    }

    // 카테고리 이름 수정
    @PutMapping("/update/{id}")
    public ApiResponse<CategoryResponse> update(@PathVariable Long id, @RequestBody CategoryRequest request) {
        CategoryResponse updated = categoryService.updateCategory(id, request);
        return ApiResponse.onSuccess(SuccessCode.OK, updated);
    }

    // 카테고리 삭제
    @DeleteMapping("/remove/{id}")
    public ApiResponse<String> remove(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.onSuccess(SuccessCode.OK, "삭제 완료");
    }

    // 특정 카테고리에 포함된 상품 보기
    @GetMapping("/{id}/items")
    public ApiResponse<List<ItemResponse>> itemList(@PathVariable Long id) {
        List<ItemResponse> items = categoryService.getItemsByCategory(id);
        if (items.isEmpty()) {
            return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_EMPTY, items);
        }
        return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, items);
    }
}