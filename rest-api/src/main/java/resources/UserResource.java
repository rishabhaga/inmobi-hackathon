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
            User oldUser = userDAO.findById(user.getId());
            int status = 0;
            if( oldUser == null ) {
                System.out.println("User dosent exist. Creating user !!!");
                status = userDAO.insert(user);
            } else {
                String newUserUUID = user.getUuid();
                String oldUserUUID = oldUser.getUuid();
                if( oldUserUUID.compareTo(newUserUUID) == 0 ) {
                    System.out.println("User token is same");
                    status = 1;
                } else {
                    System.out.println("User token is different");
                    status = 0;
                }
            }
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
