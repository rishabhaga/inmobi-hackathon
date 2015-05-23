package resources;

import com.fasterxml.jackson.databind.util.JSONPObject;
import core.User;
import dao.UserDAO;
import extra.ExternalServceConfig;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Response pushData(@PathParam("id") String id, HashMap<String,Object> payload) {
        try {
            int status = 0;
            if ( !userExist(id) ) {
                System.out.println("User dosent exist with id " + id);
            } else {
                String response = makePostCall(id,payload);
                if( response != null ) {
                    System.out.println(response);
                    status = 1;
                }
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

    private String makePostCall( String id, HashMap<String,Object> paylod) {
        System.out.println("Making post call for id " + id + " and payload " + paylod.toString());
        String output = null;

        if (!paylod.containsKey("startTime") || !paylod.containsKey("burst")) {
            System.out.println("Insufficient info to make post call");
            return output;
        }

        String startTime = paylod.get("startTime").toString();
        String burst = paylod.get("burst").toString();

        try {
            String url = externalServceConfig.getUrl() + "/learn";
            System.out.println("URL:: " + url);

            HttpPost post = new HttpPost(url);

            // add header
            post.setHeader("Content-Type", "application/json");

            // add payload
            String requestPayload = buildJsonPayload(id, startTime, burst);
            System.out.println("JSON payload: " + requestPayload);
            post.setEntity( new StringEntity( requestPayload , ContentType.create("application/json") ));

            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + post.getEntity());

            HttpResponse response = httpClient.execute(post);

            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result.toString());

            if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
                output = result.toString();
                if( output == null ) {
                    output = "OK";
                }
            }
        } catch(Exception e) {
            System.out.println("Exception:ExternalServiceResource:makePostCall " + e.getMessage());
        }
        return output;
    }

    private String buildJsonPayload( String id, String startTime, String burst ) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"user_id\":\"" + id + "\"" + ",");
        stringBuilder.append("\"start_time\":\"" + startTime + "\"" + ",");
        stringBuilder.append("\"burst\":\"" + burst + "\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

}
