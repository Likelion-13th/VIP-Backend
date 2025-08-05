package likelion13th.princess_edition.service;

import jakarta.transaction.Transactional;
import likelion13th.princess_edition.DTO.request.ItemRequest;
import likelion13th.princess_edition.DTO.response.ItemResponse;
import likelion13th.princess_edition.domain.Category;
import likelion13th.princess_edition.domain.Item;
import likelion13th.princess_edition.repository.CategoryRepository;
import likelion13th.princess_edition.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ItemResponse> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemResponse> result = new ArrayList<>();

        for (Item item : items) {
            result.add(ItemResponse.from(item));
        }

        return result;
    }

    public ItemResponse getItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        return ItemResponse.from(item);
    }

    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        Item item = new Item();
        item.setItemName(request.getItemName());
        item.setPrice(request.getPrice());
        item.setImagePath(request.getImagePath());
        item.setBrand(request.getBrand());
        item.setIsNew(request.isNew());

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        item.setCategories(categories);

        Item saved = itemRepository.save(item);
        return ItemResponse.from(saved);
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        item.setItemName(request.getItemName());
        item.setPrice(request.getPrice());
        item.setImagePath(request.getImagePath());
        item.setBrand(request.getBrand());
        item.setIsNew(request.isNew());

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        item.setCategories(categories);

        return ItemResponse.from(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
