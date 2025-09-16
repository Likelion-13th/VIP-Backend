package likelion13th.princess_edition.repository;

import likelion13th.princess_edition.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
