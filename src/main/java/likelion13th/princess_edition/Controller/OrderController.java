package likelion13th.princess_edition.Controller;

import likelion13th.princess_edition.DTO.request.OrderCreateRequest;
import likelion13th.princess_edition.DTO.response.OrderResponse;
import likelion13th.princess_edition.login.auth.jwt.CustomUserDetails;
import likelion13th.princess_edition.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 생성
    @PostMapping
    public OrderResponse createOrder(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody OrderCreateRequest request
    ) {
        return orderService.createOrder(request, customUserDetails.getUser());
    }

    // 전체 주문 조회
    @GetMapping
    public List<OrderResponse> getAllOrders(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return orderService.getAllOrders(customUserDetails.getUser());
    }

    // 주문 취소
    @PutMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }
}