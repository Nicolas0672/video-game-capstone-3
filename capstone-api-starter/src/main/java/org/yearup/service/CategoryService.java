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

    public List<Category> getAllCategories(){
        return categoryDao.getAllCategories();
    }

    public Category getCategoryByID(int id){
        Category category = categoryDao.getById(id);
        if(category == null){
            throw new CategoryNotFoundException("Category not found: " + id);
        }
        return category;
    }

    public Category createCategory(Category category){
        return categoryDao.create(category);
    }

    public Category updateCategory(int id, Category category){
        getCategoryByID(id);
        categoryDao.update(id, category);
        return getCategoryByID(id);
    }

    public void deleteCategory(int id){
        getCategoryByID(id);
        categoryDao.delete(id);
    }


}
