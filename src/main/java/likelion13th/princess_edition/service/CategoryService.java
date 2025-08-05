package likelion13th.princess_edition.service;

import likelion13th.princess_edition.DTO.request.CategoryRequest;
import likelion13th.princess_edition.DTO.response.CategoryResponse;
import likelion13th.princess_edition.DTO.response.ItemResponse;
import likelion13th.princess_edition.domain.Category;
import likelion13th.princess_edition.domain.Item;
import likelion13th.princess_edition.repository.CategoryRepository;
import likelion13th.princess_edition.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public CategoryService(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> result = new ArrayList<>();

        for (Category category : categories) {
            result.add(new CategoryResponse(category.getId(), category.getName()));
        }

        return result;
    }

    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
        return new CategoryResponse(category.getId(), category.getName());
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        Category saved = categoryRepository.save(category);
        return new CategoryResponse(saved.getId(), saved.getName());
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
        category.setName(request.getName());
        Category updated = categoryRepository.save(category);
        return new CategoryResponse(updated.getId(), updated.getName());
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<ItemResponse> getItemsByCategory(Long categoryId) {
        List<Item> items = itemRepository.findAllByCategoryId(categoryId);
        List<ItemResponse> result = new ArrayList<>();

        for (Item item : items) {
            result.add(ItemResponse.from(item));
        }

        return result;
    }
}

