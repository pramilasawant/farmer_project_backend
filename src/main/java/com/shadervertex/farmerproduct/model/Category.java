package com.shadervertex.farmerproduct.model;

import com.shadervertex.farmerproduct.dto.CategoryDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Builder
// TODO : Change table name
@Table(name="category")
@Data
@NoArgsConstructor
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Lob
	private String description;

	public CategoryDto getDto(){
		return CategoryDto.builder()
				.id(id)
				.description(description)
				.name(name)
				.build();
	}
}