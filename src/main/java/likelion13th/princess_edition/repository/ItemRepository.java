package likelion13th.princess_edition.repository;

import likelion13th.princess_edition.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 상품 데이터 저장 및 조회
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 카테고리 ID->상품목록
    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.id = :categoryId")
    List<Item> findAllByCategoryId(Long categoryId);
}