package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.ProductDao;
import org.yearup.exception.InvalidSearchCriteraException;
import org.yearup.exception.ProductNotFoundException;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    // Search for products using optional filters
    // Uncomment the validation if you want to enforce at least one filter
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String subCategory){
//        if(categoryId == null && minPrice == null && maxPrice == null && subCategory == null){
//            throw new InvalidSearchCriteraException("Need one or more search filter");
//        } else {
        return productDao.search(categoryId, minPrice, maxPrice, subCategory);
//        }
    }

    // List products by a specific category
    public List<Product> listByCategoryId(int categoryId){
        return productDao.listByCategoryId(categoryId);
    }

    // Retrieve a product by its ID
    // Throws exception if product not found
    public Product getById(int productId){
        Product product = productDao.getById(productId);
        if(product == null){
            throw new ProductNotFoundException("Product not found with " + productId);
        }
        return product;
    }

    // Update a product
    // Ensures the product exists before updating
    public Product update(int productId, Product productToUpdate){
        getById(productId); // validate existence
        productDao.update(productId, productToUpdate);
        return productDao.getById(productId);
    }

    // Create a new product
    public Product create(Product product){
        return productDao.create(product);
    }

    // Delete a product by ID
    // Throws exception if product not found
    public void delete(int productId){
        Product product = productDao.getById(productId);
        if(product == null){
            throw new ProductNotFoundException("Product not found with " + productId);
        }
        productDao.delete(productId);
    }
}

