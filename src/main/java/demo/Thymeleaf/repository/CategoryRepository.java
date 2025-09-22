package demo.Thymeleaf.repository;

import demo.Thymeleaf.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Tìm kiếm theo tên có chứa từ khóa (kết hợp phân trang)
    Page<Category> findByNameContaining(String keyword, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
}
