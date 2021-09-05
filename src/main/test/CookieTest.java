import com.ventus.core.interfaces.IProxy;
import com.ventus.core.models.Proxy;
import com.ventus.core.network.InputStreamTypes;
import com.ventus.core.network.Request;
import com.ventus.core.network.Response;
import com.ventus.core.network.Sender;
import com.ventus.core.proxy.ProxyStatus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CookieTest {

    private static final HashMap<String, String> asyncDataHeaders = new HashMap<>();
    private Sender sender;
    private Request request;
    private final Map<String, String> cookiesMap = new HashMap<>();

    @Before
    @SneakyThrows
    public void start() {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
        IProxy proxy = new Proxy("185.5.250.93", 32799, "MflHedSurF", "zoIFHlnqO5");
        proxy.setStatus(ProxyStatus.VALID);
        sender = new Sender(proxy);

        request = new Request();

        asyncDataHeaders.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        asyncDataHeaders.put("accept-encoding", "br");
        asyncDataHeaders.put("accept-language", "ru-RU,ru;q=0.9");
        asyncDataHeaders.put("cache-control", "no-cache");
        asyncDataHeaders.put("pragma", "no-cache");
        asyncDataHeaders.put("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"");
        asyncDataHeaders.put("sec-ch-ua-mobile", "?0");
        asyncDataHeaders.put("sec-fetch-dest", "document");
        asyncDataHeaders.put("sec-fetch-mode", "navigate");
        asyncDataHeaders.put("sec-fetch-site", "none");
        asyncDataHeaders.put("sec-fetch-user", "?1");
        asyncDataHeaders.put("upgrade-insecure-requests", "1");
        asyncDataHeaders.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

    }

    @Test
    public void shouldSaveCookiesFromAdidas() throws Exception {
        String itemId = "GZ9112";
        Response response;
        StringBuilder cookieStringBuilder = new StringBuilder();

        //get right geo_ip cookie:
        log.info("getting right cookies - [START]");
        request.setLink("https://www.adidas.ru/");
        request.setRequestProperties(asyncDataHeaders);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);
        response = sender.send(request);

        updateCookies(response);
        printCookies(cookiesMap);

        log.info(String.format("getting right cookies - [%d]", response.getResponseCode()));
        Assert.assertEquals(200, response.getResponseCode());
        //end

//        availability1
        String availabilityLink = "https://www.adidas.ru/api/products/" +
                itemId + "/availability";
        request.setLink(availabilityLink);
        request.setRequestProperties(asyncDataHeaders);
        request.addRequestProperties("cookie", collectCookies(cookiesMap).toString());
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("availability1 - [START]");
        response = sender.send(request);

        updateCookies(response);
        printCookies(cookiesMap);

        log.info("availability1 - " + response.getResponseCode());
        Assert.assertEquals(response.getResponseCode(), 200);
//        end

//        404
        String link404 = "https://www.adidas.ru/sbadhjsabdjhasbd";
        request.setLink(link404);
        request.setRequestProperties(asyncDataHeaders);
        request.addRequestProperties("cookie", collectCookies(cookiesMap).toString());
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);
        log.info("link404 - [START]");
        response = sender.send(request);

        updateCookies(response);
        printCookies(cookiesMap);

        log.info("link404 - " + response.getResponseCode());
        Assert.assertEquals(404, response.getResponseCode());
//      end

//      availability2
        request.setLink(availabilityLink);
        request.setRequestProperties(asyncDataHeaders);
        request.addRequestProperties("cookie", collectCookies(cookiesMap).toString());
        request.setDoIn(InputStreamTypes.GZIP);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("availability2 - [START]");
        response = sender.send(request);

        updateCookies(response);
        printCookies(cookiesMap);

        Assert.assertEquals(200, response.getResponseCode());
        log.info("availability2 - " + response.getResponseCode());
    }

    @Test
    @SneakyThrows
    public void test2() {
//        this.sender.changeProxy(proxy);

        String link = "https://www.ozon.ru/";

        request.setLink(link);
        request.setRequestProperties(asyncDataHeaders);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("goToStartPage - [START]");
        sender.send(request);
//        printCookies();
        sender.send(request);
//        printCookies();
        log.info("Go to start page - [OK]");

        String link2 = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2FozonIdIframe%3FreturnUrl%3D%252F";

        request.setLink(link2);
        request.setDoIn(InputStreamTypes.BR);

        log.info("getAsyncDataURL - [START]");
        sender.send(request);
//        printCookies();
        log.info("getAsyncDataURL - [OK]");
    }

    public void printCookies(Map<String , String> cookies) {
        System.out.println("------------------------------------------------------------");
        cookies.forEach((key, value) -> System.out.printf("%s=%s\n", key, value));
//        sender.getCookieManager().getCookieStore().getCookies().forEach(System.out::println);
        System.out.println("============================================================");
    }

    @After
    public void finish() {

    }

    StringBuilder collectCookies(Map<String, String> cookiesMap) {
        StringBuilder cookieStringBuilder = new StringBuilder();
        if (!cookiesMap.isEmpty()) {
            for (Map.Entry<String, String> entry : cookiesMap.entrySet()) {
                cookieStringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
            }
            cookieStringBuilder = new StringBuilder(cookieStringBuilder.substring(0, cookieStringBuilder.length() - 2));
        }
        if (cookieStringBuilder.toString().equals("")) {
            cookieStringBuilder = new StringBuilder("null");
        }
        return cookieStringBuilder;
    }

    void updateCookies(Response response) {
        if (!response.getSetCookies().isEmpty()) {
            for (Map.Entry<String, String> entry : response.getSetCookies().entrySet()) {
                cookiesMap.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    }
}
