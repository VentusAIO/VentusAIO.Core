import com.ventus.core.network.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.CookieStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class CookieTestWithServer {
    private final ExecutorService executorservice = Executors.newSingleThreadExecutor();
    private Sender sender;

    @Before
    public void startServer() {
        CookieTester http = new CookieTester();
        http.run();
    }

    @Before
    public void configureCookieStore() {
        CookieStore cookieStore = new PersistentCookieStore();
        sender = new Sender(cookieStore);
    }

    @After
    public void shutdownServer() {
        executorservice.shutdownNow();
    }

    @Test
    public void test1() {
        configureCookieStore();
        checkCookie();
    }

    public void checkCookie() {
        log.info("test 1");
        Assert.assertEquals(418, send("http://localhost:32151/test1").getResponseCode());
        log.info("test 2");
        Assert.assertEquals(200, send("http://localhost:32151/test2").getResponseCode());
        log.info("test 3");
        Assert.assertEquals(307, send("http://localhost:32151/test3").getResponseCode());
        log.info("test 4");
        Assert.assertEquals(200, send("http://localhost:32151/test4").getResponseCode());
        log.info("test 5");
        Assert.assertEquals(418, send("http://localhost:32151/test5").getResponseCode());
        log.info("test 6");
        Assert.assertEquals(200, send("http://localhost:32151/test6").getResponseCode());
        log.info("test 7");
        Assert.assertEquals(418, send("http://localhost:32151/test7").getResponseCode());
        log.info("test 8");
        Assert.assertEquals(200, send("http://localhost:32151/test8").getResponseCode());
        log.info("test 9");
        Assert.assertEquals(418, send("http://localhost:32151/test9").getResponseCode());
        log.info("test 10");
        Assert.assertEquals(403, send("http://localhost:32151/test10").getResponseCode());
        log.info("test 11");
        Assert.assertEquals(418, send("http://localhost:32151/test1").getResponseCode());
        log.info("test 12");
        Assert.assertEquals(200, send("http://localhost:32151/test2").getResponseCode());
        log.info("test 13");
        Assert.assertEquals(418, send("http://localhost:32151/test5").getResponseCode());
        log.info("test 14");
        Assert.assertEquals(403, send("http://localhost:32151/test2").getResponseCode());
    }

    private Response send(String link) {
        Request request = new Request();
        request.setLink(link);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);
        return sender.send(request);
    }
}
