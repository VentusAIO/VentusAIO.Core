import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CookieTestWithServer {

    public static void main(String[] args) {
        configureHttp();
    }

    @Before
    public static void configureHttp(){
        Http http = new Http();
        http.run();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkCookie() throws IOException, InterruptedException {
        Assert.assertEquals(send("/test1"), 418);
        Assert.assertEquals(send("/test2"), 200);
    }

    private static int send(String link) throws IOException {
        URL url = new URL("http://127.0.0.1:32151" + link);
//        ip=" + URLEncoder.encode(my_ip, UTF_8)
        System.out.println(url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getResponseCode();
    }

    private static class Http implements Runnable {
        private static final ExecutorService executorservice = Executors.newSingleThreadExecutor();

        public Http() {
        }

        @Override
        public void run() {
            executorservice.execute(() -> {
                try {
                    HttpServer server = HttpServer.create(new InetSocketAddress(32151), 0);
                    server.createContext("/test1", new FirstTest());
                    server.createContext("/test2", new SecondTest());
//                    server.createContext("/test2", new SecondTest());
//                    server.createContext("/test3", new ThirdTest());
                    server.setExecutor(null); // creates a default executor
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
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

        private static class FirstTest implements HttpHandler {
            @Override
            public void handle(HttpExchange t) throws IOException {
                String response = "ok";
                t.getResponseHeaders().add("set-cookie", "akavpfq_y2_ru=1630915178~0~69b3d5a28ae9e6045244dcbb230011b9al6d5226dF207b9b7364F#6c318e2008; Expires-Mon, 6 Sep 2021 08:01:38 GMT; Path=/; domain=localhost");
                t.sendResponseHeaders(418, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private static class SecondTest implements HttpHandler {
            @Override
            public void handle(HttpExchange t) throws IOException {
                String response = "ok";
                System.out.println(t.getRequestHeaders().get("cookie"));
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
