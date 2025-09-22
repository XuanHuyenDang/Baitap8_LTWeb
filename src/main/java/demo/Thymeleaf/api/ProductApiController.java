package demo.Thymeleaf.api;

import demo.Thymeleaf.dto.ProductRequest;
import demo.Thymeleaf.entity.Category;
import demo.Thymeleaf.entity.Product;
import demo.Thymeleaf.repository.CategoryRepository;
import demo.Thymeleaf.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

	private final ProductRepository productRepo;
	private final CategoryRepository categoryRepo;

	public ProductApiController(ProductRepository productRepo, CategoryRepository categoryRepo) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
	}

	@GetMapping
	public Page<Product> list(@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id,desc") String sort) {
		String[] sp = sort.split(",");
		Sort s = (sp.length == 2 && "asc".equalsIgnoreCase(sp[1])) ? Sort.by(sp[0]).ascending()
				: Sort.by(sp[0]).descending();
		Pageable pageable = PageRequest.of(page, size, s);
		return (keyword == null || keyword.isBlank()) ? productRepo.findAll(pageable)
				: productRepo.findByNameContainingIgnoreCase(keyword, pageable);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> get(@PathVariable Long id) {
		return productRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody ProductRequest req) {
		Category cat = categoryRepo.findById(req.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("Category không tồn tại"));
		Product p = new Product();
		p.setName(req.getName());
		p.setPrice(req.getPrice());
		p.setCategory(cat);
		Product saved = productRepo.save(p);
		return ResponseEntity.created(URI.create("/api/products/" + saved.getId())).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
		return productRepo.findById(id).map(ex -> {
			Category cat = categoryRepo.findById(req.getCategoryId())
					.orElseThrow(() -> new IllegalArgumentException("Category không tồn tại"));
			ex.setName(req.getName());
			ex.setPrice(req.getPrice());
			ex.setCategory(cat);
			// return ResponseEntity.ok(productRepo.save(ex));
			  productRepo.save(ex); 
			return ResponseEntity.noContent().build(); // 204, không serialize Product
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (!productRepo.existsById(id))
			return ResponseEntity.notFound().build();
		productRepo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
