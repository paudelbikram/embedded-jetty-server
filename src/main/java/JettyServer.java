import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyServer
{
    //https://www.baeldung.com/jetty-embedded
    //https://www.eclipse.org/jetty/documentation/current/embedding-jetty.html
    private Server server;
    private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);


    /*
    Instantiate a new Server object and set it to start on a given port:
    Logback is successor of log4j. So, it is better than log4j.
    Logback is divided into three modules, logback-core, logback-classic and logback-access.
    The core module lays the groundwork for the other two modules. The classic module extends core.
    The classic module is corresponds to a significantly improved version of log4j.
    The third module called access integreates with Servlet containers to provider HTTP-access log
    functionality.
    The way logback tries to configure itself:
    1. Looks for file logback.groovy, logback-test.xml, logback.xml in classpath in order.
    2. if neither file is found, logback configure itself automatically using the BasicConfigurator which will
        cause logging output to be directed to the console.

    Logback delegates the task of writing a looping event to components called appenders. Appenders
    must implement the ch.qos.logback.core.Appender

     */
    void start() throws  Exception
    {
        /*
        When our application is handling requests in an asynchronous way, we should configure
        thread pool explicitly. In next section, we will configure Jetty to use a custom thread pool.
        When we run our web application on production, we might want to tune how
        the Jetty server processes requests. This is done by defining thread pool and applying.
        Three configuration settings that we can set:
        1. maxThreads:
        2. minThreads:
        3. idleTimeout: This value in milliseconds defines how long a thread can be idle before it
                        is stopped and removed from the thread pool. The number of remaining threads in
                        the pool will never go below the minThreads setting.
         */
        logger.info("Starting Jetty Server");
        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;
        logger.info("Setting maxThreads: {}, minThread: {} and idleTimeout: {}",maxThreads, minThreads, idleTimeout);


        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
        server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[]{connector});

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        //Now we need to register the BlockingServlet class in the ServletHandler object
        servletHandler.addServletWithMapping(BlockingServlet.class, "/status");
        servletHandler.addServletWithMapping(AsyncServlet.class, "/heavy/async");

        server.start();

    }

    void stop() throws Exception {
        logger.warn("Stopping Jetty Server");
        server.stop();
    }


}
