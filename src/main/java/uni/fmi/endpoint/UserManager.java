package uni.fmi.endpoint;

import org.apache.log4j.Logger;
import uni.fmi.model.StatusMessage;
import uni.fmi.model.StatusMessageBuilder;
import uni.fmi.model.User;
import uni.fmi.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
}
