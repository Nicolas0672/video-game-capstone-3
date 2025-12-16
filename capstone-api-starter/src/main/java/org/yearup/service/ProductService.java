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

    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String subCategory){
//        if(categoryId == null && minPrice == null && maxPrice == null && subCategory == null){
//            throw new InvalidSearchCriteraException("Need one or more search filter");
//        } else {
            return productDao.search(categoryId, minPrice, maxPrice, subCategory);
//        }
    }

    public List<Product> listByCategoryId(int categoryId){
        return productDao.listByCategoryId(categoryId);
    }

    public Product getById(int productId){
        Product product = productDao.getById(productId);
        if(product == null){
            throw new ProductNotFoundException("Product not found with " + productId);
        }
        return product;
    }

    public Product update(int productId, Product productToUpdate){
        // Check to ensure Product exist before updating
        getById(productId);
        productDao.update(productId, productToUpdate);
        return productDao.getById(productId);
    }

    public Product create(Product product){
        return productDao.create(product);
    }

    public void delete(int productId){
        Product product = productDao.getById(productId);
        if(product == null){
            throw new ProductNotFoundException("Product not found with " + productId);
        }
        productDao.delete(productId);
    }
}
