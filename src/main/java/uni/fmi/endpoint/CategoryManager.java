/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.endpoint;

import org.apache.log4j.Logger;
import uni.fmi.annotation.Secured;
import uni.fmi.model.Category;
import uni.fmi.model.StatusMessage;
import uni.fmi.model.StatusMessageBuilder;
import uni.fmi.service.CategoryService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Secured
@Path("categories")
public class CategoryManager {
    
    private static final Logger LOG = Logger.getLogger(CategoryManager.class);

    @Inject
    private CategoryService categoryService;

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(Category category) {
        LOG.info("Category creation initiated...");
        if (category.getName() == null || category.getName().equals("")
                || category.getPlannedAmount().compareTo(BigDecimal.ZERO) != 1
                || category.getBudget() == null || category.getBudget().getId() <= 0) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Category must have Name, Planned Amount and Budget!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }

        Category newCategory = categoryService.createCategory(category);
        if (categoryService.createCategory(category) != null) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Category was created successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(newCategory).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with category creation!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }

    @GET
    @Path("{budgetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriesForBudget(@PathParam("budgetId") int budgetId) {
        List<Category> categoriesForBudget = categoryService.getCategoriesForBudget(budgetId);

        LOG.info("Budgets for categoriy's id successfully retrieved: " + categoriesForBudget);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(categoriesForBudget).build();
    }
    
    @GET
    @Path("{userId}/{validForMonth}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriesForUserAndMonth(@PathParam("userId") int userId,
            @PathParam("validForMonth") String validForMonth) {
        List<Category> categoriesForBudget = categoryService.getCategoriesForUserAndMonth(userId, validForMonth);

        LOG.info("Budgets for user's id and month successfully retrieved: " + categoriesForBudget);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(categoriesForBudget).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCategory(@PathParam("id") int id) {
        boolean result = categoryService.removeCategory(id);

        LOG.info("Category successfully deleted: " + result);

        return Response.status(Response.Status.OK.getStatusCode())
                .build();
    }
}
