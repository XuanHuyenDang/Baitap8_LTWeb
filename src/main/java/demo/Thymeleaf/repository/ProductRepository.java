package demo.Thymeleaf.repository;

import demo.Thymeleaf.entity.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Override
	@EntityGraph(attributePaths = "category")
	Page<Product> findAll(Pageable pageable);

	@EntityGraph(attributePaths = "category")
	Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

	@Override
	@EntityGraph(attributePaths = "category")
	Optional<Product> findById(Long id);
}
