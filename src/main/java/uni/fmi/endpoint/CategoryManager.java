/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.endpoint;

import java.math.BigDecimal;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import uni.fmi.annotation.Secured;
import uni.fmi.model.Category;
import uni.fmi.model.StatusMessage;
import uni.fmi.model.StatusMessageBuilder;
import uni.fmi.service.CategoryService;

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

        if (categoryService.createCategory(category)) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Category was created successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(statusMessage).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with category creation!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }

    @DELETE
    @Path("remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategory(@HeaderParam("id") int id) {
        boolean result = categoryService.removeCategory(id);

        LOG.info("Category successfully deleted: " + result);

        return Response.status(Response.Status.OK.getStatusCode())
                .build();
    }
}
