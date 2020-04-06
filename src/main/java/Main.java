import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String [] args)
    {

        logger.info("Starting application");
        try {
            JettyServer application = new JettyServer();
            application.start();
        } catch (Exception e) {
            logger.error("Error occurred {}", e);
        }
    }

}
