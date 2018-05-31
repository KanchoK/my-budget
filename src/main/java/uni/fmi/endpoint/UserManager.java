package uni.fmi.endpoint;

import java.math.BigDecimal;
import java.util.List;
import org.apache.log4j.Logger;
import uni.fmi.model.StatusMessage;
import uni.fmi.model.StatusMessageBuilder;
import uni.fmi.model.User;
import uni.fmi.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uni.fmi.annotation.Secured;
import uni.fmi.model.Budget;

@Path("users")
public class UserManager {
    private static final Logger LOG = Logger.getLogger(UserManager.class);

    @Context
    private Configuration configuration;

    @Inject
    private UserService userService;

    @POST
    @Path("registration")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        LOG.info("Registration initiated...");
        if (user.getUsername() == null || user.getPassword() == null
                || user.getUsername().equals("") || user.getPassword().equals("")) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Username, password and user role must be present!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }

        if (userService.createUser(user)) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("User was created successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(statusMessage).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with the registration service!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }
    
    @Secured
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserById(@PathParam("id") int id, User user) {
        if (user.getUsername() == null || user.getUsername().equals("") 
                || user.getEmail() == null || user.getEmail().equals("") 
                || user.getOverallExpensesAlertLimit().compareTo(BigDecimal.ZERO) != 1
                || user.getCategoryExpensesAlertLimit().compareTo(BigDecimal.ZERO) != 1){
//                || (!"true".equals(user.getOverallExpensesAlert()) && !"false".equals(user.getOverallExpensesAlert()))
//                || (!"true".equals(user.getCategoryExpensesAlert()) && !"false".equals(user.getCategoryExpensesAlert()))){
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Username, email and overall and category expenses " +
                            "alert limits and overall and category expenses alerts must be present!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }
        
        User updatedUser = userService.updateUserForId(id, user);
        if (updatedUser != null &&  updatedUser.getId() != -1) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.OK.getStatusCode())
                    .message("User was updated successfully.").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(updatedUser).build();
        } else {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with the user update!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }
    }
    
    @Secured
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserForId (@PathParam("id") int id) {
        User user = userService.getUserForId(id);
        LOG.info("User successfully retrieved: " + user);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(user).build();
    }
}
