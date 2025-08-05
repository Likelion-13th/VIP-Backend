package likelion13th.princess_edition.Controller;

import likelion13th.princess_edition.DTO.request.ItemRequest;
import likelion13th.princess_edition.DTO.response.ItemResponse;
import likelion13th.princess_edition.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemResponse> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public ItemResponse getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping
    public ItemResponse createItem(@RequestBody ItemRequest request) {
        return itemService.createItem(request);
    }

    @PutMapping("/{itemId}")
    public ItemResponse updateItem(@PathVariable Long itemId, @RequestBody ItemRequest request) {
        return itemService.updateItem(itemId, request);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }
}


