/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.service.impl;

import uni.fmi.model.Category;
import uni.fmi.persistence.dao.CategoryDao;
import uni.fmi.service.CategoryService;

import javax.inject.Inject;
import java.util.List;
import uni.fmi.model.Budget;

public class CategoryServiceImpl implements CategoryService {
    
    @Inject
    private CategoryDao categoryDao;

    @Override
    public Category createCategory(Category category) {
        Category newCategory = categoryDao.createCategory(category);
        return newCategory;
    }

    @Override
    public List<Category> getCategoriesForBudget(int budgetId) {
        return categoryDao.getCategoriesForBudget(budgetId);
    }
    
    @Override
    public List<Category> getCategoriesForUserAndMonth(int userId, String month) {
        return categoryDao.getCategoriesForUserAndMonth(userId, month);
    }
    
    @Override
    public List<Category> copyCategoriesForUserBudgetAndMonth(int userId, int budgetId, String month, Budget budget) {
        return categoryDao.copyCategoriesForUserBudgetAndMonth(userId, budgetId, month, budget);
    }

    @Override
    public boolean removeCategory(int id) {
        return categoryDao.removeCategory(id);
    }
    
    @Override
    public Category getCategoryForId(int id) {
        return categoryDao.getCategoryForId(id);
    }
}
