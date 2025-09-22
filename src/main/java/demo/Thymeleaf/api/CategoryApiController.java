package demo.Thymeleaf.api;

import demo.Thymeleaf.entity.Category;
import demo.Thymeleaf.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {
    private final CategoryRepository repo;
    public CategoryApiController(CategoryRepository repo){ this.repo = repo; }

    @GetMapping
    public Page<Category> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ){
        String[] sp = sort.split(",");
        Sort s = (sp.length==2 && "asc".equalsIgnoreCase(sp[1])) ?
                Sort.by(sp[0]).ascending() : Sort.by(sp[0]).descending();
        Pageable pageable = PageRequest.of(page, size, s);
        return (keyword == null || keyword.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNameContaining(keyword, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> get(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Category body){
        body.setId(null);
        // Nếu muốn chống trùng tên nhanh:
        // if (repo.existsByNameIgnoreCase(body.getName())) return ResponseEntity.status(409).body("Name duplicated");
        Category saved = repo.save(body);
        return ResponseEntity.created(URI.create("/api/categories/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Category body){
        return repo.findById(id).map(ex -> {
            ex.setName(body.getName());
            ex.setDescription(body.getDescription());
            return ResponseEntity.ok(repo.save(ex));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
