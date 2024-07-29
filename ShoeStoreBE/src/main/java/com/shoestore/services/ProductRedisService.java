package com.shoestore.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shoestore.response.ProductResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductRedisService {

    void clear();

    List<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException;

    void saveAllProducts(List<ProductResponse> productResponses,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest) throws  JsonProcessingException;
}
