package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.CategoryDao;
import org.yearup.exception.CategoryNotFoundException;
import org.yearup.models.Category;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    // Retrieve all categories
    public List<Category> getAllCategories(){
        return categoryDao.getAllCategories();
    }

    // Retrieve a category by ID
    // Throws exception if not found
    public Category getCategoryByID(int id){
        Category category = categoryDao.getById(id);
        if(category == null){
            throw new CategoryNotFoundException("Category not found: " + id);
        }
        return category;
    }

    // Create a new category
    public Category createCategory(Category category){
        return categoryDao.create(category);
    }

    // Update an existing category
    // Ensures category exists before updating
    public Category updateCategory(int id, Category category){
        getCategoryByID(id); // validate existence
        categoryDao.update(id, category);
        return getCategoryByID(id); // return updated category
    }

    // Delete a category
    // Ensures category exists before deletion
    public void deleteCategory(int id){
        getCategoryByID(id); // validate existence
        categoryDao.delete(id);
    }
}




