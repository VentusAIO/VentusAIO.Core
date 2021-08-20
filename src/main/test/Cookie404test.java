import com.ventus.core.interfaces.IProxy;
import com.ventus.core.models.Proxy;
import com.ventus.core.network.*;
import com.ventus.core.proxy.ProxyStatus;
import com.ventus.core.util.JsonParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.CookieStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@Slf4j
public class Cookie404test {

    private static final HashMap<String, String> asyncDataHeaders = new HashMap<>();
    private static final HashMap<String, String> ozonHeaders = new HashMap<>();
    private Sender sender;
    private Request request;
    private CookieStore adidasCookieStore;
    private CookieStore ozonCookieStore;

    @Before
    @SneakyThrows
    public void start() {
//        adidasCookieStore = new PersistentCookieStore("login", "path.txt", new URI("https://www.adidas.ru"));
        ozonCookieStore = new PersistentCookieStore("", "cookies.txt", URI.create("https://www.ozon.ru"));
//        cookieStore = new CookieManager().getCookieStore();
        request = new Request();

        //Настройка прокси
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");

//        adidas properties
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
        
        //ozon properties
        ozonHeaders.put("pragma", "no-cache");
        ozonHeaders.put("cache-control", "no-cache");
        ozonHeaders.put("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"");
        ozonHeaders.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        ozonHeaders.put("accept", "application/json");
        ozonHeaders.put("x-o3-app-name", "dweb_client");
        ozonHeaders.put("x-o3-app-version", "release_30-6'-'2021_64baf5a0");
        ozonHeaders.put("sec-ch-ua-mobile", "?0");
        ozonHeaders.put("content-type", "application/json");
        ozonHeaders.put("origin", "https://www.ozon.ru");
        ozonHeaders.put("sec-fetch-site", "same-origin");
        ozonHeaders.put("sec-fetch-mode", "cors");
        ozonHeaders.put("sec-fetch-dest", "empty");
        ozonHeaders.put("referer", "https://www.ozon.ru/");
        ozonHeaders.put("accept-encoding", "gzip");
        ozonHeaders.put("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
    }

    @Test
    public void cookie404TestLocalHost() throws URISyntaxException {
        sender = new Sender(adidasCookieStore);
        cookie404Test(sender);
    }

//    @Test
    public void cookie404TestProxy() throws URISyntaxException {
        IProxy proxy = new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf");
        proxy.setStatus(ProxyStatus.VALID);
        sender = new Sender(adidasCookieStore);
        sender.changeProxy(proxy);
        cookie404Test(sender);
    }

    public void cookie404Test(Sender sender) throws URISyntaxException {
        String itemId = "GZ9112";
        URI uri = new URI("https://www.adidas.ru/");

        //get right geo_ip cookie:
        log.info("getting right cookies - [START]");
        request.setLink("https://www.adidas.ru/");
        request.setRequestProperties(asyncDataHeaders);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);
        Response response = sender.send(request);
        printCookies(uri);
        log.info("getting right cookies - [{}]", response.getResponseCode());
//        Assert.assertEquals(200, response.getResponseCode());
        //end

//        availability1
        String availabilityLink = "https://www.adidas.ru/api/products/" +
                itemId + "/availability";
        request.setLink(availabilityLink);
        request.setRequestProperties(asyncDataHeaders);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("availability1 - [START]");
        Response response1 = sender.send(request);

        printCookies(uri);

        log.info("availability1 - " + response1.getResponseCode());
//        Assert.assertEquals(response1.getResponseCode(), 200);
//        end

//        404
        String link404 = "https://www.adidas.ru/sbadhjsabdjhasbd";
        request.setLink(link404);
        request.setRequestProperties(asyncDataHeaders);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);
        log.info("link404 - [START]");
        Response response2 = sender.send(request);
        printCookies(uri);
        log.info("link404 - [{}]", response2.getResponseCode());
//        Assert.assertEquals(404, response2.getResponseCode());
//      end

//      availability2
        request.setLink(availabilityLink);
        request.setRequestProperties(asyncDataHeaders);
        request.setDoIn(InputStreamTypes.GZIP);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("availability2 - [START]");
        Response response3 = sender.send(request);
        printCookies(uri);
//        Assert.assertEquals(200, response3.getResponseCode());
        log.info("availability2 - [{}]", response3.getResponseCode());
//      end

        //ozon check after adidas
//        log.info("ozon check - [START]");
//        URI uri2 = URI.create("https://www.ozon.ru");
//        request.setLink("https://www.ozon.ru");
//        Response response4 = sender.send(request);
//        printCookies(uri2);
//        log.info("ozon check - [{}]", response4.getResponseCode());
        //end
    }

    @Test
    public void shouldTestCookiesForOzonLocalhost() {
        URI uri = URI.create("https://www.ozon.ru");
        Request request = new Request();
        Response response;
        sender = new Sender(ozonCookieStore);

        //get cookies
        log.info("get cookies - [START]");
        request.setLink("https://www.ozon.ru");
        request.setRequestProperties(ozonHeaders);
        request.setDoIn(InputStreamTypes.NONE);
        request.setMethod("GET");
        response = sender.send(request);
        printCookies(uri);
        log.info("get cookies - [{}]", response.getResponseCode());
        log.info("response headers: {}", response.getHeaderFields());
        Assert.assertEquals(200, response.getResponseCode());
        //end

        //get summary
        log.info("get summary - [START]");
        request.setLink("https://www.ozon.ru/api/composer-api.bx/_action/summary");
        response = sender.send(request);
        printCookies(uri);
        log.info("get summary - [{}]", response.getResponseCode());
        log.info("response headers: {}", response.getHeaderFields());
        Assert.assertEquals(200, response.getResponseCode());
        //end

        //get asyncData url
        log.info("get asyncData url - [START]");
        request.setLink("https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2FozonIdIframe%3FreturnUrl%3D%252F");
        request.setDoIn(InputStreamTypes.GZIP);
        response = sender.send(request);
        printCookies(uri);
        log.info("get asyncData url - [{}]", response.getResponseCode());
        log.info("response headers: {}", response.getHeaderFields());
        Assert.assertEquals(200, response.getResponseCode());
        //end

        //get asyncData
        log.info("get asyncData - [START]");
        String link = "https://www.ozon.ru" + JsonParser.getValue(response.getData(), "url");
        link = link.substring(0, link.length() - 1);
        request.setLink(link);
        request.setDoIn(InputStreamTypes.GZIP);
        response = sender.send(request);
        printCookies(uri);
        log.info("get asyncData - [{}]", response.getResponseCode());
        log.info("response headers: {}", response.getHeaderFields());
        String asyncData;
        Assert.assertEquals(200, response.getResponseCode());
        //end

        asyncData = JsonParser.getValue(response.getData(), "asyncData");
        asyncData = asyncData.replace("\\\\u002F", "/");
        log.info("asyncData - {}", asyncData);
    }

    public void printCookies(URI uri) {
        System.out.println("------------------------------------------------------------");
        sender.getCookieManager().getCookieStore().get(uri).forEach(x -> System.out.printf("%s: %s=%s, (Version=%d, MaxAge=%d, Path=%s)\n", x.getDomain(), x.getName(), x.getValue(), x.getVersion(), x.getMaxAge(), x.getPath()));
        System.out.println("============================================================");
    }

    @After
    public void finish() {
    }
}
