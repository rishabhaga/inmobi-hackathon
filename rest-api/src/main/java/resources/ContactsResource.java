package resources;

import core.Contacts;
import core.User;
import dao.ContactsDAO;
import dao.UserDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON)
public class ContactsResource {
    private final UserDAO userDAO;
    private final ContactsDAO contactsDAO;

    public ContactsResource(UserDAO userDAO, ContactsDAO contactsDAO) {
        this.userDAO = userDAO;
        this.contactsDAO = contactsDAO;
    }

    @GET
    @Path("{id}")
    public List<String> getContacts(@PathParam("id") String id) {
        List<String> contactsList = new ArrayList<>();
        try {
            if( !userExist(id) ) {
                System.out.println("User dosent exist with id " + id);
            } else {
                System.out.println("User exist with id " + id);
                List<Contacts> contactses = contactsDAO.findByFromId(id);
                System.out.println("Contact list fetched " + contactses.toString());
                for( Contacts contacts: contactses ) {
                    contactsList.add(contacts.getToId());
                }
            }
        } catch( Exception e ) {
            System.out.println("Exception::ContactsResource::getContacts " + e.getMessage());
        }
        return contactsList;
    }

    @POST
    @Path("/{id}/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addContacts(@PathParam("id") String id, List<String> contactIds) {
        int status = 0;
        try {
            if ( !userExist(id)) {
                System.out.println("User dosent exist with id " + id);
            } else {
                System.out.println("User exist with id " + id);
                if( contactIds != null && contactIds.size() > 0 ) {
                    System.out.println("Adding contacts " + contactIds.toString());
                    for( String contactId: contactIds ) {
                        User contactUserData = userDAO.findById(contactId);
                        if( contactUserData == null ) {
                            System.out.println("No contact exist for id: " + contactId);
                            System.out.println("Creating guest user for id: " + contactId);
                            userDAO.insert(createGuestUserObject(contactId));
                            contactUserData = userDAO.findById(contactId);
                        }

                        System.out.println("Adding in contacts " + id + " -> " + contactUserData.getId() );
                        Contacts contacts = createContactObject( id, contactUserData.getId() );
                        contactsDAO.insert(contacts);
                    }
                    status = 1;
                } else {
                    System.out.println("Contact ids is empty");
                }
            }
            return Response.ok(status).build();
        } catch (Exception e) {
            System.out.println("Exception::ContactsResource::addContacts " + e.getMessage());
            return Response.ok(0).build();
        }
    }

    @POST
    @Path("/{id}/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteContacts(@PathParam("id") String id, List<String> contactIds) {
        int status = 0;
        try {
            if ( !userExist(id) ) {
                System.out.println("User dosent exist with id " + id);
            } else {
                System.out.println("User exist with id " + id);
                if( contactIds != null && contactIds.size() > 0 ) {
                    System.out.println("Deleting contacts " + contactIds.toString());
                    for( String contactId: contactIds ) {
                        User contactUserData = userDAO.findById(contactId);
                        if( contactUserData == null ) {
                            System.out.println("No contact exist for id: " + contactId);
                            continue;
                        }

                        System.out.println("Deleting from contacts " + id + " -> " + contactUserData.getId() );
                        Contacts contacts = createContactObject( id, contactUserData.getId() );
                        contactsDAO.delete(contacts);
                    }
                    status = 1;
                } else {
                    System.out.println("Contact ids is empty");
                }
            }
            return Response.ok(status).build();
        } catch (Exception e) {
            System.out.println("Exception::ContactsResource::deleteContacts " + e.getMessage());
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

    private Contacts createContactObject( String fromId, String toId ) {
        Contacts contacts = new Contacts();
        contacts.setFromId(fromId);
        contacts.setToId(toId);
        return contacts;
    }

    private User createGuestUserObject( String id ) {
        User user = new User();
        user.setId(id);
        user.setUuid("GUEST");
        user.setDeviceId("GUEST");
        return user;
    }

}
