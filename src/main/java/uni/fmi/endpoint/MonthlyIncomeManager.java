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

        if (monthlyIncomeService.createMonthlyIncome(monthlyIncome)) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Monthly income was created successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(statusMessage).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with monthly income creation!").build();
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

        LOG.info("Monthly incomes successfully retrieved: " + monthlyIncomesForUser);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(monthlyIncomesForUser).build();
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
