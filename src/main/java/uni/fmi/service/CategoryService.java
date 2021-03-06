/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.service;

import uni.fmi.model.Category;

import java.util.List;
import uni.fmi.model.Budget;

public interface CategoryService {
    
    Category createCategory(Category category);

    List<Category> getCategoriesForBudget(int budgetId);
    
    List<Category> getCategoriesForUserAndMonth(int userId, String month);
    
    List<Category> copyCategoriesForUserBudgetAndMonth(int userId, int budgetId, String month, Budget budget);

    boolean removeCategory(int id);
    
    Category getCategoryForId(int id);
    
    
}
