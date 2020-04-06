import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
Let's say we want to create an endpoint that will respond with the HTTP Status code of
200 if everything goes well and a sample JSON payload.
We will create a class that extends the HttpServlet class to handle such request; this class
will be single threaded and block until completion
 */
public class BlockingServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\"}");
    }
}
