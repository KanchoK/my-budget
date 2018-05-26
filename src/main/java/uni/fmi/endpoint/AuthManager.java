package uni.fmi.endpoint;

import org.apache.log4j.Logger;
import uni.fmi.model.StatusMessage;
import uni.fmi.model.StatusMessageBuilder;
import uni.fmi.model.User;
import uni.fmi.service.TokenService;
import uni.fmi.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
public class AuthManager {
    private static final Logger LOG = Logger.getLogger(AuthManager.class);

    @Context
    private Configuration configuration;

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        LOG.info("Login initiated...");
        if (user.getUsername() == null || user.getPassword() == null
                || user.getUsername().equals("") || user.getPassword().equals("")) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .message("Username and password must be present!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(statusMessage).build();
        }

        user = userService.validateUser(user.getUsername(), user.getPassword());
        if (user == null) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.FORBIDDEN.getStatusCode())
                    .message("Access denied!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                    .entity(statusMessage).build();
        }

        LOG.info("Logged user info: " + user);

        String token = tokenService.generateTokenForUser(user);

        if (token == null) {
            StatusMessage statusMessage = new StatusMessageBuilder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message("There was some problem with the login service!").build();
            LOG.info("Service status message: " + statusMessage);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .entity(statusMessage).build();
        }

        StatusMessage statusMessage = new StatusMessageBuilder()
                .status(Response.Status.OK.getStatusCode())
                .message(token).build();

        LOG.info("Successful login for: " + user);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(statusMessage).build();
    }
}
