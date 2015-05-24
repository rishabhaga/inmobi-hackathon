import dao.ContactsDAO;
import dao.UserDAO;
import extra.ExternalServceConfig;
import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.http.client.HttpClient;
import org.skife.jdbi.v2.DBI;
import resources.ContactsResource;
import resources.ExternalServiceResource;
import resources.TestResource;
import resources.UserResource;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
public class MainApplication extends Application<MainConfiguration> {
    public static void main(String[] args) throws Exception {
        new MainApplication().run(args);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void initialize(Bootstrap<MainConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(MainConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register( new TestResource() );

        /*
        Adding DAO Resources
         */
        final DBIFactory dbiFactory = new DBIFactory();
        final DBI dbi = dbiFactory.build(environment, configuration.getDataSourceFactory(), "jdbc");

        final UserDAO userDAO = dbi.onDemand(UserDAO.class);
        environment.jersey().register(new UserResource(userDAO));

        final ContactsDAO contactsDAO = dbi.onDemand(ContactsDAO.class);
        environment.jersey().register(new ContactsResource(userDAO, contactsDAO));

        /*
        Adding Client Resources
         */
        HttpClient httpClient = new HttpClientBuilder(environment).using(configuration.getHttpClientConfiguration()).build("myHttpClient");
        ExternalServceConfig externalServceConfig = configuration.getExternalServceConfig();
        environment.jersey().register(new ExternalServiceResource(httpClient,userDAO,externalServceConfig));
    }
}
