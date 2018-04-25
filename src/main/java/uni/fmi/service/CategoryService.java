/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.service;

import uni.fmi.model.Category;

import java.util.List;

public interface CategoryService {
    
    boolean createCategory(Category category);

    List<Category> getCategoriesForBudget(int categoryId);

    boolean removeCategory(int id);
}
