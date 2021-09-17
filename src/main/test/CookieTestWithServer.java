import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.ventus.core.network.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class CookieTestWithServer {
    private final ExecutorService executorservice = Executors.newSingleThreadExecutor();
    private Sender sender;

    @Before
    public void startServer() {
        Http http = new Http();
        http.run();
    }

    @Before
    public void configureCookieStore() throws URISyntaxException {
        CookieStore cookieStore = new CookieManager().getCookieStore();
        sender = new Sender(cookieStore);
    }

    @After
    public void shutdownServer() {
        executorservice.shutdownNow();
    }

    @Test
    public void test1() throws IOException, URISyntaxException, InterruptedException {
        configureCookieStore();
        checkCookie();
    }

    public void checkCookie() {
        Assert.assertEquals(418, send("http://localhost:32151/test1").getResponseCode());
        log.info("penis 1");
        Assert.assertEquals(200, send("http://localhost:32151/test2").getResponseCode());
        log.info("penis 2");
        Assert.assertEquals(418, send("http://localhost:32151/test3").getResponseCode());
        log.info("penis 3");
        Assert.assertEquals(403, send("http://localhost:32151/test4").getResponseCode());
        log.info("penis 4");
    }

    private Response send(String link){
        Request request = new Request();
        request.setLink(link);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);
        return sender.send(request);
    }
}
