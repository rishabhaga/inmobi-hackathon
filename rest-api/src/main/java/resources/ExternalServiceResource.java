package resources;

import core.User;
import dao.UserDAO;
import extra.ExternalServceConfig;
import org.apache.http.client.HttpClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
public class ExternalServiceResource {
    private final HttpClient httpClient;
    private final UserDAO userDAO;
    private final ExternalServceConfig externalServceConfig;

    public ExternalServiceResource(HttpClient httpClient, UserDAO userDAO, ExternalServceConfig externalServceConfig) {
        this.httpClient = httpClient;
        this.userDAO = userDAO;
        this.externalServceConfig = externalServceConfig;
    }

    @POST
    @Path("/{id}/push")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pushData(@PathParam("id") String id) {
        try {
            int status = 0;
            if ( !userExist(id) ) {
                System.out.println("User dosent exist with id " + id);
            } else {
                System.out.println(id);
                System.out.println(externalServceConfig.getHost());
                System.out.println(externalServceConfig.getPort());
                status = 1;
            }
            return Response.ok(status).build();
        } catch( Exception e ) {
            System.out.println("Exception:ExternalServiceResource:pushData " + e.getMessage());
            return Response.ok(0).build();
        }
    }

    private Boolean userExist(String id) {
        Boolean exist = false;
        try {
            User user = userDAO.findById(id);
            if( user != null ) {
                exist = true;
            }
        } catch(Exception e) {
            System.out.println("Exception::ContactsResource::userExist " + e.getMessage());
        }
        return exist;
    }

    private void makeGetCall() {

    }
}
