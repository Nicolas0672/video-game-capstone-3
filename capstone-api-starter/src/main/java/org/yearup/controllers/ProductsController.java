package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;
import org.yearup.service.ProductService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController
{
    private final ProductService productService;

    @Autowired
    public ProductsController(ProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Product>> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice,
                                                @RequestParam(name="subCategory", required = false) String subCategory
                                )
    {
        List<Product> products = productService.search(categoryId, minPrice, maxPrice, subCategory);
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> getById(@PathVariable int id )
    {
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("categories/{categoryId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Product>> listByCategoryId(@PathVariable int categoryId){
        List<Product> products = productService.listByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody @Valid Product product)
    {
       Product newProduct = productService.create(product);
       return ResponseEntity.status(201).body(newProduct);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody @Valid Product product)
    {
        Product updatedProduct = productService.update(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id)
    {
       productService.delete(id);
       return ResponseEntity.noContent().build();
    }
}
