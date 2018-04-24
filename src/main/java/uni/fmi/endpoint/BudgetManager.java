package uni.fmi.endpoint;

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
import java.util.List;

@Secured
@Path("budgets")
public class BudgetManager {

    private static final Logger LOG = Logger.getLogger(BudgetManager.class);

    @Inject
    private BudgetService budgetService;

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBudget(Budget budget) {
        LOG.info("Budget creation initiated...");
        if (budget.getName() == null || budget.getValidForMonth() == null
                || budget.getName().equals("") || budget.getValidForMonth().equals("")
                || budget.getUser() == null || budget.getUser().getId() <= 0) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Budget must have Name, Valid Month and User!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }

        if (budgetService.createBudget(budget)) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Budget was created successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(statusMessage).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with budget creation!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetsForUser(@HeaderParam("userId") int userId) {
        List<Budget> budgetsForUser = budgetService.getBudgetsForUser(userId);

        LOG.info("Budgets successfully retrieved: " + budgetsForUser);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(budgetsForUser).build();
    }

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudget(@HeaderParam("validForMonth") String month,
                              @HeaderParam("userId") int userId) {
        Budget result = budgetService.getBudgetForUserAndMonth(month, userId);

        LOG.info("Budget successfully retrieved: " + result);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(result).build();
    }

    @DELETE
    @Path("remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudget(@HeaderParam("id") int id) {
        boolean result = budgetService.removeBudget(id);

        LOG.info("Budget successfully deleted: " + result);

        return Response.status(Response.Status.OK.getStatusCode())
                .build();
    }
}
