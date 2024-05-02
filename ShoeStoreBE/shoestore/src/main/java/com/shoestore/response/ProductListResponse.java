package com.shoestore.response;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
