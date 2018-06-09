/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.endpoint;

import org.apache.log4j.Logger;
import uni.fmi.annotation.Secured;
import uni.fmi.model.Payment;
import uni.fmi.model.StatusMessage;
import uni.fmi.model.StatusMessageBuilder;
import uni.fmi.service.PaymentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Secured
@Path("payments")
public class PaymentManager {

    private static final Logger LOG = Logger.getLogger(PaymentManager.class);

    @Inject
    private PaymentService paymentService;

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPayment(Payment payment) {
        LOG.info("Payment creation initiated...");
        if (payment.getTitle() == null || payment.getTitle().equals("")
                || payment.getAmount().compareTo(BigDecimal.ZERO) != 1
                || payment.getCategory() == null || payment.getCategory().getId() <= 0) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Payment must have Name,Amount and Category!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }

        Payment newPayment = paymentService.createPayment(payment);
        if (newPayment != null && newPayment.getId() != -1) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Payment was created successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(newPayment).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with payment creation!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }

    @GET
    @Path("{categoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentsForCategory(@PathParam("categoryId") int categoryId) {
        List<Payment> paymentsForCategory = paymentService.getPaymentsForCategory(categoryId);

        LOG.info("Payments fro category's id = " + categoryId
                + " are successfully retrieved: " + paymentsForCategory);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(paymentsForCategory).build();
    }

    @GET
    @Path("{userId}/{validForMonth}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentsForUserAndMonth(@PathParam("userId") int userId,
            @PathParam("validForMonth") String month) {
        List<Payment> paymentsForCategory = paymentService.getPaymentsForUserAndMonth(userId, month);

        LOG.info("Payments for user's id = " + userId + " and month = " + month
                + " is successfully retrieved: " + paymentsForCategory);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(paymentsForCategory).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePayment(@PathParam("id") int id) {
        boolean result = paymentService.removePayment(id);

        LOG.info("Payment for id = " + id + " is successfully deleted: " + result);

        return Response.status(Response.Status.OK.getStatusCode())
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoryForId(Payment payment) {
        Payment paymentById = paymentService.getPaymentForId(payment.getId());
        
        if (paymentById != null && paymentById.getId() != -1) {
            LOG.info("Payment for id = " + paymentById.getId() + " is successfully retrieved: " + paymentById);

            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(paymentById).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There is no payment for id = " + payment.getId() + " !").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }
    
    @PUT
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePaymentById(@PathParam("id") int id,
            Payment payment){
          LOG.info("Payment update initiated...");
        if (payment.getAmount().compareTo(BigDecimal.ZERO) != 1){
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Payment must have Amount!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }

        Payment updatedPayment = paymentService.updatePaymentById(id, payment);
        if (updatedPayment != null && updatedPayment.getId() != -1) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("Payment was updated successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(updatedPayment).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with payment update!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }
}
