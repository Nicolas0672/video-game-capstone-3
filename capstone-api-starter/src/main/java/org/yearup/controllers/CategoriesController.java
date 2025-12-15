package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController
{
    private final CategoryService categoryService;
    private final ProductDao productDao;

    public CategoriesController(CategoryService categoryService, ProductDao productDao) {
        this.categoryService = categoryService;
        this.productDao = productDao;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll()
    {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> getById(@PathVariable int id)
    {
        Category category = categoryService.getCategoryByID(id);
        if(category == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return null;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        Category category1 = categoryService.createCategory(category);
        if(category1 == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(category1);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        Category categoryToUpdate = categoryService.getCategoryByID(id);
        if(categoryToUpdate == null){
            return ResponseEntity.notFound().build();
        }
        categoryService.updateCategory(id, category);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        Category category = categoryService.getCategoryByID(id);
        if(category == null){
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
