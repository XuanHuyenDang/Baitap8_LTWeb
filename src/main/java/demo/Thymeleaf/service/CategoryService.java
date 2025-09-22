package demo.Thymeleaf.service;

import demo.Thymeleaf.entity.Category;
import demo.Thymeleaf.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    public Page<Category> listAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (keyword != null && !keyword.isEmpty()) {
            return repo.findByNameContaining(keyword, pageable);
        }
        return repo.findAll(pageable);
    }

    public void save(Category category) {
        repo.save(category);
    }

    public Category get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy category"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
