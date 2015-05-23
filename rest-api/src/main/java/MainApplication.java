import dao.UserDAO;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
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
        addResources( configuration, environment );
    }

    private void addResources(MainConfiguration configuration, Environment environment) {
        final DBIFactory dbiFactory = new DBIFactory();
        final DBI dbi = dbiFactory.build(environment, configuration.getDataSourceFactory(), "jdbc");

        final UserDAO userDAO = dbi.onDemand(UserDAO.class);
        environment.jersey().register(new UserResource(userDAO));

    }
}
