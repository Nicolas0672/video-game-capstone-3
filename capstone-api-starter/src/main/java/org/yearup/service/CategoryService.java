package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.CategoryDao;
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
        return categoryDao.getById(id);
    }

    public Category createCategory(Category category){
        return categoryDao.create(category);
    }

    public void updateCategory(int id, Category category){
        Category categoryToUpdate = categoryDao.getById(id);
        if(category.getName() != null){
            categoryToUpdate.setName(category.getName());
        }

        if(category.getDescription() != null){
            categoryToUpdate.setDescription(category.getDescription());
        }

        categoryDao.update(id, categoryToUpdate);
    }

    public void deleteCategory(int id){
        categoryDao.delete(id);
    }


}
