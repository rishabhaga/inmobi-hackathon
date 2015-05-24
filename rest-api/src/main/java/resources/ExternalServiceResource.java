package resources;

import com.mysql.jdbc.StringUtils;
import core.User;
import dao.UserDAO;
import extra.ExternalServceConfig;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
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

        burst = convertCsvToBitmap(burst);

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

    private String convertCsvToBitmap( String data ) {
        String baseBitMap = "000000000000000000000000000000000000";
        if( data == null || data.length() == 0 ) {
            return baseBitMap;
        }

        String[] baseBitMapArray = {
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
        };
        String[] strArray = data.split(",");
        int[] intArray = new int[strArray.length];
        for(int i = 0; i < strArray.length; i++) {
            int loc = Integer.parseInt(strArray[i]);
            baseBitMapArray[loc-1] = "1";
        }

        return org.apache.commons.lang3.StringUtils.join(baseBitMapArray);
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

    @GET
    @Path("/{id}/pull")
    public Map<String,Integer> pullData(@PathParam("id") String id ) {
        Map<String,Integer> response = new HashMap<>();
        try {
            //response = getSlottedDate(id);
            String output = makeGetCall(id);
            if( output != null ) {
                output = output.substring(1, output.length()-1);           //remove curly brackets

                int commaIndex = output.indexOf(',');
                String dataValue = output.substring(commaIndex+1);

                int colonIndex = dataValue.indexOf(':');
                String value = dataValue.substring(colonIndex+1);

                value = value.substring(1, value.length()-1);

                String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
                Map<Integer,Integer> map = new HashMap<>();

                for(String pair : keyValuePairs)                        //iterate over the pais
                {
                    String[] entry = pair.split(":");                   //split the pairs to get key and value
                    String key = entry[0].trim();
                    key = key.substring(1,key.length()-1);
                    map.put( Integer.parseInt(key), Integer.parseInt(entry[1].trim()));          //add them to the hashmap
                }
                System.out.println("Data POJO " + map.toString());

                response = getSlottedData( map );
            }
            System.out.println(response);
        } catch( Exception e ) {
            System.out.println("Exception:ExternalServiceResource:pushData " + e.getMessage());
        }
        return response;
    }

    private String makeGetCall( String id ) {
        System.out.println("Making get call for id " + id );
        String output = null;

        try {
            String url = externalServceConfig.getUrl() + "/predict/" + id;
            System.out.println("URL:: " + url);

            HttpGet get = new HttpGet(url);

            // add header
            get.setHeader("Content-Type", "application/json");

            System.out.println("\nSending 'GET' request to URL : " + url);

            HttpResponse response = httpClient.execute(get);

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
            }
        } catch(Exception e) {
            System.out.println("Exception:ExternalServiceResource:makePostCall " + e.getMessage());
        }
        return output;
    }

    private Map<String,Integer> getSlottedData( Map<Integer,Integer> input ) {
        Map<String,Integer> response = new HashMap<>();
        String burstData = "";
        Integer interval;
        Integer slot = 1;
        for( int i = 0; i < 288; i ++ ) {
            if( input.containsKey(i) ) {
                burstData += input.get(i).toString();
            }

            if( i%36 == 35 ) {
                if( burstData.length() == 36 ) {
                    interval = getAlarmingIntervalForSlot(burstData);
                    response.put( slot.toString(), interval );
                }
                slot += 1;
                burstData = "";
            }
        }
        return response;
    }

    private Integer getAlarmingIntervalForSlot(String data) {
        int maxInterval = getMaximumConsecutiveZeros(data);
        int alarmingInterval = maxInterval * 5;
        if( alarmingInterval < 15 ) {
            alarmingInterval = 15;
        }
        return alarmingInterval;
    }

    private Integer getMaximumConsecutiveZeros( String data ) {
        int count = 0;
        int max = 0;
        char ch;
        for( int a = 0; a < data.length(); a ++ ) {
            ch = data.charAt(a);
            if( ch == '1' ) {
                count = 0;
            }
            if( ch == '0' ) {
                count ++;
            }
            if( count > max ) {
                max = count;
            }
        }
        return max;
    }


}
