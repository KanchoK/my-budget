package uni.fmi.endpoint;

import java.math.BigDecimal;
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
                || budget.getUser() == null || budget.getUser().getId() <= 0
                || budget.getPlannedAmount().compareTo(BigDecimal.ZERO) != 1) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Budget must have Name, Valid Month, Planned Amount and User!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }

        Budget newBudget = budgetService.createBudget(budget);
        if (newBudget != null && newBudget.getId() != -1) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Budget was created successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(newBudget).build();
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
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetsForUser(@PathParam("userId") int userId) {
        List<Budget> budgetsForUser = budgetService.getBudgetsForUser(userId);

        LOG.info("Budgets for user's id = " + userId + " are successfully retrieved: " + budgetsForUser);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(budgetsForUser).build();
    }

//    @GET
//    @Path("{budgetId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getBudgetForId(@PathParam("budgetId") int budgetId) {
//        Budget budgetForId = budgetService.getBudgetForId(budgetId);
//
//        LOG.info("Budget for id = " + budgetId + " is successfully retrieved: " + budgetForId);
//
//        return Response.status(Response.Status.OK.getStatusCode())
//                .entity(budgetForId).build();
//    }
    @GET
    @Path("{userId}/{validForMonth}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetsForUserAndMonth(@PathParam("validForMonth") String month,
            @PathParam("userId") int userId) {
        List<Budget> budgetsForUserAndMonth = budgetService.getBudgetsForUserAndMonth(userId, month);

        LOG.info("Budgets for user id = " + userId + " are successfully retrieved: " + budgetsForUserAndMonth);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(budgetsForUserAndMonth).build();
    }

//    @GET 
//    @Path("copy/{userId}/{validForMonth}/{budgetId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response copyBudgetUserBudgetAndMonth(@PathParam("validForMonth") String month,
//        @PathParam("userId") int userId, @PathParam("budgetId") int budgetId){
//        Budget copiedBudgetUserBudgetAndMonth = budgetService.copyBudgetUserBudgetAndMonth(userId, budgetId, month);
//
//        LOG.info("Copied budget for " + month + " successfully retrieved: " + copiedBudgetUserBudgetAndMonth);
//
//        return Response.status(Response.Status.OK.getStatusCode())
//                .entity(copiedBudgetUserBudgetAndMonth).build();
//    }
    @GET
    @Path("plannedAmount/{userId}/{validForMonth}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetsPlannedAmountForUserAndMonth(@PathParam("validForMonth") String month,
            @PathParam("userId") int userId) {
        BigDecimal budgetsPlannedAmount = budgetService.getBudgetsPlannedAmountForUserAndMonth(userId, month);
        LOG.info(budgetsPlannedAmount);

        LOG.info("Overall planned amount for user's id = " + userId
                + " and month = " + month + " is successfully retrieved: "
                + budgetsPlannedAmount);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(budgetsPlannedAmount).build();
    }

    @GET
    @Path("spentAmount/{userId}/{validForMonth}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetsSpentAmountForUserAndMonth(@PathParam("validForMonth") String month,
            @PathParam("userId") int userId) {
        BigDecimal budgetsSpentAmount = budgetService.getBudgetsSpentAmountForUserAndMonth(userId, month);

        LOG.info("Overall spent amount for user's id = " + userId
                + " and month = " + month + " is successfully retrieved: "
                + budgetsSpentAmount);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(budgetsSpentAmount).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeBudget(@PathParam("id") int id) {
        boolean result = budgetService.removeBudget(id);

        LOG.info("Budget for id = " + id + " is successfully deleted: " + result);

        return Response.status(Response.Status.OK.getStatusCode())
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetForId(Budget budget) {
        Budget budgetById = budgetService.getBudgetForId(budget.getId());

        if (budgetById != null && budgetById.getId() != -1) {
            LOG.info("Budget for id = " + budget.getId() + " is successfully retrieved: " + budgetById);

            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(budgetById).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There is no budget for id = " + budget.getId() + " !").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }
}
