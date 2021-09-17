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

    private class Http implements Runnable {
        public Http() {}
        @Override
        public void run() {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(32151), 0);
                server.createContext("/test1", new FirstTest());
                server.createContext("/test2", new SecondTest());
                server.createContext("/test3", new ThirdTest());
                server.createContext("/test4", new FourTest());
                server.setExecutor(executorservice);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Map<String, String> handleGetRequests(HttpExchange httpExchange) {
            Map<String, String> map = new HashMap<>();
            for (String s : httpExchange.
                    getRequestURI()
                    .toString()
                    .split("\\?")[1]
                    .split("&")) {
                String[] ss = s.split("=");
                map.put(ss[0], URLDecoder.decode(ss[1], StandardCharsets.UTF_8));
            }
            return map;
        }

        private String handleGetRequest(HttpExchange httpExchange) {
            return httpExchange.
                    getRequestURI()
                    .toString()
                    .split("\\?")[1]
                    .split("=")[1];
        }

        private class FirstTest implements HttpHandler {
            @Override
            public void handle(HttpExchange t) throws IOException {
                String response = "ok";
                t.getResponseHeaders().add("set-cookie", "test1=1630915178~0~69b3d5a28ae9e6045244dcbb230011b9al6d5226dF207b9b7364F#6c318e2008; Expires=Mon, 20 Sep 2021 08:01:38 GMT; Path=/; domain=localhost");
                t.sendResponseHeaders(418, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private class SecondTest implements HttpHandler {
            @Override
            public void handle(HttpExchange t) throws IOException {
                String response = "ok";
                Map<String, String> map = new HashMap<>();
                List.of(t.getRequestHeaders().get("cookie").get(0).split("; ")).forEach(i -> {
                    String[] s = i.split("=", 2);
                    map.put(s[0], s[1]);
                });
                if(map.get("test1").equals("1630915178~0~69b3d5a28ae9e6045244dcbb230011b9al6d5226dF207b9b7364F#6c318e2008")){
                    t.sendResponseHeaders(200, response.length());
                }
                else{
                    t.sendResponseHeaders(403, response.length());
                }
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private class ThirdTest implements HttpHandler {
            @Override
            public void handle(HttpExchange t) throws IOException {
                String response = "ok";
                String cookie = "test1=qw21321eqe12e12; Expires=Mon, 10 Sep 2021 08:01:38 GMT; Path=/; domain=localhost";
                t.getResponseHeaders().add("set-cookie", cookie);
                t.sendResponseHeaders(418, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private class FourTest implements HttpHandler {
            @Override
            public void handle(HttpExchange t) throws IOException {
                String response = "ok";
                Map<String, String> map = new HashMap<>();
                List.of(t.getRequestHeaders().get("cookie").get(0).split("; ")).forEach(i -> {
                    String[] s = i.split("=", 2);
                    map.put(s[0], s[1]);
                });
                if(map.get("test1").equals("qw21321eqe12e12")){
                    t.sendResponseHeaders(200, response.length());
                }
                else{
                    t.sendResponseHeaders(403, response.length());
                }
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
