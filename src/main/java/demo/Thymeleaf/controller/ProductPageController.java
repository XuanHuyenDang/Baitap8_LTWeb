package demo.Thymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductPageController {

	@GetMapping("/products/ajax")
	public String ajaxPage() {
		return "product/ajax"; // templates/product/ajax.html
	}
}
