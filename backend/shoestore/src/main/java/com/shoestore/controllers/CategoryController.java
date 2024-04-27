package com.shoestore.controllers;

import com.shoestore.dtos.CategoryDTO;
import com.shoestore.models.Category;
import com.shoestore.response.CreateCategoryResponse;
import com.shoestore.response.UpdateCategoryResponse;
import com.shoestore.services.Implement.CategoryServiceImpl;
import com.shoestore.components.LocalizationUtils;
import com.shoestore.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
//Dependency Injection
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
    public ResponseEntity<CreateCategoryResponse> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(CreateCategoryResponse.builder()
                            .message(localizationUtils.getLocalizedMessaged(MessageKeys.CREATE_CATEGORY_FAILED))
                    .build());
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(CreateCategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessaged(MessageKeys.CREATE_CATEGORY_SUCCESSFULLY))
                .build());
    }

    //Hiện tất cả các categories
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page")     int page,
            @RequestParam("limit")    int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(UpdateCategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessaged(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY))
                .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessaged(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
    }
}
