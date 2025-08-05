package likelion13th.princess_edition.repository;

import likelion13th.princess_edition.domain.Order;
import likelion13th.princess_edition.global.constant.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime dateTime);
}