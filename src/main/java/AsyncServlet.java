import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;

import java.nio.charset.StandardCharsets;
/*
Let's say we have enormous resource that is I/O intense taking a long
time to load blocking the executing thread for a substantial amount of time.
It is better if that thread can be liberated to handle other requests in meantime,
instead of waiting for some I/O resource.
To provide such logic with Jetty, we can create a servlet that will use the
AsyncContext class by calling the startAsync() method on the HttpServletRequest.
This code will not block executing thread but will perform the I/O operation in separate
thread returning the result when ready using the AsyncContext.completed() method.


 */

public class AsyncServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AsyncServlet.class);
    private static final String HEAVY_RESOURCE = "This is some heavy resource that will be served in an async way";

    protected void doGet(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        logger.info("Trying to get heavy resources {}", HEAVY_RESOURCE);
        final ByteBuffer content = ByteBuffer.wrap(HEAVY_RESOURCE.getBytes(StandardCharsets.UTF_8));

        final AsyncContext async = request.startAsync();
        final ServletOutputStream out = response.getOutputStream();
        out.setWriteListener(new WriteListener() {
            @Override
            public void onWritePossible() throws IOException {
                while (out.isReady()) {
                    if (!content.hasRemaining()) {
                        response.setStatus(200);
                        async.complete(); //signaling that result is ready to return to the client by invoking the complete() method.
                        return;
                    }
                    out.write(content.get());
                }
            }

            @Override
            public void onError(Throwable t) {
                getServletContext().log("Async Error", t);
                async.complete();
            }
        });
    }
}