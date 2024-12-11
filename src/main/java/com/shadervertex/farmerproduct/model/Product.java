package com.shadervertex.farmerproduct.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadervertex.farmerproduct.dto.ProductDto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "submitted_by")
    @JsonIgnore
    private User submittedBy;  // Added field

    // Getter and Setter for submittedBy
    public User getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(User submittedBy) {
        this.submittedBy = submittedBy;
    }

    // TODO : add mapstruct
    public ProductDto getDto() {
        return ProductDto.builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .byteImg(img)
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build();
    }
}

