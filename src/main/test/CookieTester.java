import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CookieTester implements Runnable {
    private final ExecutorService executorservice = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        CookieTester http = new CookieTester();
        http.run();
    }

    public CookieTester() {
    }

    @Override
    public void run() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(32151), 0);
            server.createContext("/test1", new SetCookie(418, "test1=1630915178~0~69b3d5a28ae9e6045244dcbb230011b9al6d5226dF207b9b7364F#6c318e2008; Expires=Mon, 20 Sep 2021 08:01:38 GMT; Path=/; domain=localhost"));
            server.createContext("/test2", new CheckCookie("test1", "1630915178~0~69b3d5a28ae9e6045244dcbb230011b9al6d5226dF207b9b7364F#6c318e2008"));
            server.createContext("/test3", new ThirdTest());
            server.createContext("/test4", new CheckCookie("test1", "qw21321eqe12e12"));
            server.createContext("/test5", new SetCookie(418, "test1=newcookie; Expires=Mon, 27 Sep 2021 08:01:38 GMT; Path=/; domain=localhost"));
            server.createContext("/test6", new CheckCookie("test1", "newcookie"));
            server.createContext("/test7", new SetCookie(418, "test1=newcookie2; Expires=Mon, 27 Sep 2021 08:01:38 GMT; Path=/; domain=localhost"));
            server.createContext("/test8", new CheckCookie("test1", "newcookie2"));
            server.createContext("/test9", new SetCookie(418, "test1=pls_remove_me; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/; domain=localhost"));
            server.createContext("/test10", new CheckCookie("test1", "pls_remove_me"));
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

    private class SetCookie implements HttpHandler {
        private final int code;
        private final String cookie;

        SetCookie(int code, String cookie) {
            this.code = code;
            this.cookie = cookie;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "ok";
            t.getResponseHeaders().add("set-cookie", cookie);
            t.sendResponseHeaders(code, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static class ThirdTest implements HttpHandler {
        private boolean isRedirect = false;
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "ok";
            t.getResponseHeaders().add("set-cookie", "test1=qw21321eqe12e12; Expires=Tue, 21 Sep 2021 12:01:38 GMT; Path=/; domain=localhost");
            t.getResponseHeaders().add("location", "http://localhost:32151/test3");
            if (isRedirect) {
                t.sendResponseHeaders(418, response.length());
            } else {
                t.sendResponseHeaders(307, response.length());
            }
            isRedirect = !isRedirect;
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private class CheckCookie implements HttpHandler {
        private final String key;
        private final String value;

        CheckCookie(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            try {
                String response = "ok";
                Map<String, String> map = new HashMap<>();
                if (t.getRequestHeaders().containsKey("cookie")) {
                    List.of(t.getRequestHeaders().get("cookie").get(0).split("; ")).forEach(i -> {
                        String[] s = i.split("=", 2);
                        map.put(s[0], s[1]);
                    });
                }
                if (map.containsKey(key) && map.get(key).equals(value)) {
                    t.sendResponseHeaders(200, response.length());
                } else {
                    t.sendResponseHeaders(403, response.length());
                }
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
