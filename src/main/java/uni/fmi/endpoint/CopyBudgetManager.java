package uni.fmi.endpoint;

import java.util.List;
import org.apache.log4j.Logger;
import uni.fmi.annotation.Secured;
import uni.fmi.model.Budget;
import uni.fmi.model.StatusMessage;
import uni.fmi.model.StatusMessageBuilder;
import uni.fmi.service.BudgetService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uni.fmi.model.Category;
import uni.fmi.service.CategoryService;

@Secured
@Path("copyBudget")
public class CopyBudgetManager {

    private static final Logger LOG = Logger.getLogger(CopyBudgetManager.class);

    @Inject
    private BudgetService budgetService;
    @Inject
    private CategoryService categoryService;
    
    @GET 
    @Path("{userId}/{validForMonth}/{budgetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response copyBudgetUserBudgetAndMonth(@PathParam("validForMonth") String month,
        @PathParam("userId") int userId, @PathParam("budgetId") int budgetId){       
        Budget copiedBudgetUserBudgetAndMonth = budgetService.copyBudgetForUserBudgetAndMonth(userId, budgetId, month);
        LOG.info("Copied budget for " + month + " successfully retrieved: " + copiedBudgetUserBudgetAndMonth);
                
        Budget budgetById = budgetService.getBudgetForId(budgetId);
        
        List<Category> copiedCategoriesUserBudgetAndMonth = categoryService.copyCategoriesForUserBudgetAndMonth(userId,
                budgetId, budgetById.getValidForMonth(), copiedBudgetUserBudgetAndMonth);
        LOG.info("Copied categories for " + month + " successfully retrieved: " + copiedCategoriesUserBudgetAndMonth);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(copiedBudgetUserBudgetAndMonth).build();
    }
}
