package demo.Thymeleaf.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductRequest {
	@NotBlank
	@Size(max = 150)
	private String name;

	@NotNull
	@DecimalMin("0.0")
	private BigDecimal price;

	@NotNull
	private Long categoryId;

	// getters/setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
