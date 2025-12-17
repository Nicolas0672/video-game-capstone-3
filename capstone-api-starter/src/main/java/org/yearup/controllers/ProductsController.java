package org.yearup.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products", description = "API for managing products")
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
    @Operation(summary = "Search products", description = "Search products with custom filters")
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
    @Operation(summary = "Get product by productID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getById(@PathVariable int id )
    {
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("categories/{categoryId}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get products by category ID")
    public ResponseEntity<List<Product>> listByCategoryId(@PathVariable int categoryId){
        List<Product> products = productService.listByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Add product (ADMIN_ONLY)")
    public ResponseEntity<Product> addProduct(@RequestBody @Valid Product product)
    {
       Product newProduct = productService.create(product);
       return ResponseEntity.status(201).body(newProduct);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update product (ADMIN_ONLY)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found to update")
    })
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody @Valid Product product)
    {
        Product updatedProduct = productService.update(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete product (ADMIN_ONLY)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found to delete")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable int id)
    {
       productService.delete(id);
       return ResponseEntity.noContent().build();
    }
}
