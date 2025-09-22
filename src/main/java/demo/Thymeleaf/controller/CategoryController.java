package demo.Thymeleaf.controller;

import demo.Thymeleaf.entity.Category;
import demo.Thymeleaf.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@GetMapping
	public String list(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "0") int page,
			Model model) {
		Page<Category> categories = service.listAll(keyword, page, 5);
		model.addAttribute("categories", categories.getContent()); // danh sách Category
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", categories.getTotalPages());
		model.addAttribute("keyword", keyword);
		return "category/list";
	}

	@GetMapping("/ajax")
	public String ajaxPage() {
		return "category/ajax";
	}

	@GetMapping("/new")
	public String showNewForm(Model model) {
		model.addAttribute("category", new Category());
		return "category/form";
	}

	@PostMapping("/save")
	public String saveCategory(@ModelAttribute Category category) {
		service.save(category);
		return "redirect:/categories";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		model.addAttribute("category", service.get(id));
		return "category/form";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		service.delete(id);
		return "redirect:/categories";
	}
}
