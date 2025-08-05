package likelion13th.princess_edition.repository;

import likelion13th.princess_edition.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// 카테고리 데이터 저장 및 조회
public interface CategoryRepository extends JpaRepository<Category, Long> {

}