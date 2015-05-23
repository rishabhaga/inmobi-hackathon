package resources;

import core.User;
import dao.UserDAO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @Path("/register")
    public Response register(@Valid User user) {
        try {
            int status = userDAO.insert(user);
            return Response.ok(status).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.ok(0).build();
        }
    }

    @GET
    @Path("/info/{id}")
    public User getInfo(@PathParam("id") String id) {
        return userDAO.findById(id);
    }
}
