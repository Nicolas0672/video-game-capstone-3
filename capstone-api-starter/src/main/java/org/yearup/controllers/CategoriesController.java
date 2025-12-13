package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController
{
    private final CategoryService categoryService;
    private final ProductDao productDao;

    // create an Autowired controller to inject the categoryDao and ProductDao

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
        return ResponseEntity.ok(category1);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        categoryService.updateCategory(id, category);
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable int id)
    {
        categoryService.deleteCategory(id);
    }
}
