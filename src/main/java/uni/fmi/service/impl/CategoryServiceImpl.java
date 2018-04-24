/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.service.impl;

import javax.inject.Inject;
import uni.fmi.model.Category;
import uni.fmi.persistence.dao.CategoryDao;
import uni.fmi.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    
    @Inject
    private CategoryDao categoryDao;

    @Override
    public boolean createCategory(Category category) {
        int categoryId = categoryDao.createCategory(category);
        return categoryId > 0;
    }

    @Override
    public boolean removeCategory(int id) {
        return categoryDao.removeCategory(id);
    }
}