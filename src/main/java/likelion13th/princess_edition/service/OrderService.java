package likelion13th.princess_edition.service;

import jakarta.transaction.Transactional;
import likelion13th.princess_edition.DTO.request.OrderCreateRequest;
import likelion13th.princess_edition.DTO.response.OrderResponse;
import likelion13th.princess_edition.domain.Item;
import likelion13th.princess_edition.domain.Order;
import likelion13th.princess_edition.global.constant.OrderStatus;
import likelion13th.princess_edition.repository.ItemRepository;
import likelion13th.princess_edition.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        int totalPrice = item.getPrice() * request.getQuantity();
        int mileageToUse = request.getMileageToUse();

        // 마일리지는 결제 금액보다 클 수 없음
        int availableMileage = Math.min(mileageToUse, totalPrice);
        int finalPrice = totalPrice - availableMileage;

        // 주문 객체 생성
        Order order = new Order();
        order.setItem(item);
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setFinalPrice(finalPrice);
        order.setStatus(OrderStatus.PROCESSING);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    @Transactional
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(OrderResponse.from(order));
        }

        return result;
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (order.getStatus() == OrderStatus.COMPLETE || order.getStatus() == OrderStatus.CANCEL) {
            throw new IllegalArgumentException("이미 완료되었거나 취소된 주문은 변경할 수 없습니다.");
        }

        order.updateStatus(OrderStatus.CANCEL);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateOrderStatus() {
        List<Order> orders = orderRepository.findByStatusAndCreatedAtBefore(
                OrderStatus.PROCESSING,
                LocalDateTime.now().minusMinutes(1)
        );

        for (Order order : orders) {
            order.updateStatus(OrderStatus.COMPLETE);
        }
    }
}