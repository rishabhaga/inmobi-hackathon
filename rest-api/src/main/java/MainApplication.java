import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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

    }
}
