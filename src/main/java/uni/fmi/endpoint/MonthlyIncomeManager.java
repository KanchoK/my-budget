package uni.fmi.endpoint;

import java.math.BigDecimal;
import org.apache.log4j.Logger;
import uni.fmi.annotation.Secured;
import uni.fmi.model.MonthlyIncome;
import uni.fmi.model.StatusMessage;
import uni.fmi.model.StatusMessageBuilder;
import uni.fmi.service.MonthlyIncomeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Secured
@Path("monthly-incomes")
public class MonthlyIncomeManager {

    private static final Logger LOG = Logger.getLogger(MonthlyIncomeManager.class);

    @Inject
    private MonthlyIncomeService monthlyIncomeService;

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMonthlyIncome(MonthlyIncome monthlyIncome) {
        LOG.info("MonthlyIncome creation initiated...");
        if ( monthlyIncome.getValidForMonth() == null || monthlyIncome.getValidForMonth().equals("")
                || monthlyIncome.getMonthlyIncome().compareTo(BigDecimal.ZERO) != 1
                || monthlyIncome.getUser() == null || monthlyIncome.getUser().getId() <= 0) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Monthly income must have MonthlyIncome, Valid Month and User!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }

        MonthlyIncome newMonthlyIncome = monthlyIncomeService.createMonthlyIncome(monthlyIncome);
        if (newMonthlyIncome != null && newMonthlyIncome.getId() != -1) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Monthly income was created successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(newMonthlyIncome).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with monthly income creation!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }
    
    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMonthlyIncome(@PathParam("id") int id,
            MonthlyIncome monthlyIncome) {
        LOG.info("MonthlyIncome creation initiated...");
        if (monthlyIncome.getValidForMonth() == null || monthlyIncome.getValidForMonth().equals("")
                || monthlyIncome.getMonthlyIncome().compareTo(BigDecimal.ZERO) != 1) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Monthly income must have MonthlyIncome and Valid Month!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }
        

        MonthlyIncome newMonthlyIncome = monthlyIncomeService.updateMonthlyIncome(id, monthlyIncome);
        if (newMonthlyIncome != null && newMonthlyIncome.getId() != -1) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Monthly income was updated successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(newMonthlyIncome).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with monthly income update!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlyIncomesForUser(@PathParam("userId") int userId) {
        List<MonthlyIncome> monthlyIncomesForUser = monthlyIncomeService.getMonthlyIncomesForUser(userId);

        LOG.info("Monthly incomes for user's id " + userId + " are successfully retrieved: " + monthlyIncomesForUser);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(monthlyIncomesForUser).build();
    }
    
    @GET
    @Path("{userId}/{validForMonth}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlyIncomesForUserAndMonth(@PathParam("userId") int userId,
            @PathParam("validForMonth") String validForMonth) {
        MonthlyIncome monthlyIncomesForUserForMonth = monthlyIncomeService.getMonthlyIncomeForUserAndMonth(userId, validForMonth);

        LOG.info("Monthly income for user's id = " + userId + " and month = " +
                validForMonth + " is successfully retrieved: " + monthlyIncomesForUserForMonth);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(monthlyIncomesForUserForMonth).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeMonthlyIncome(@PathParam("id") int id) {
        boolean result = monthlyIncomeService.removeMonthlyIncome(id);

        LOG.info("Monthly income successfully deleted: " + result);

        return Response.status(Response.Status.OK.getStatusCode())
                .build();
    }
}
