import com.ventus.core.network.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.HashMap;

@Slf4j
public class Cookie404test {

    private static final HashMap<String, String> asyncDataHeaders = new HashMap<>();
    private Sender sender;
    private Request request;

    @Before
    @SneakyThrows
    public void start() {
        PersistentCookieStore cookieStore = new PersistentCookieStore("login", "path", new URI(""));
        sender = new Sender(cookieStore);
        request = new Request();

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
    }

    @Test
    public void cookie404test() {
        String itemId = "GZ9112";
//        IProxy proxy = new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf");
//        proxy.setStatus(ProxyStatus.VALID);

        //      availability1
        String availabilityLink = "https://www.adidas.ru/api/products/" +
                itemId + "/availability";
        request.setLink(availabilityLink);
        request.setRequestProperties(asyncDataHeaders);
        request.setDoIn(InputStreamTypes.GZIP);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("availability1 - [START]");
        Response responce1 = sender.send(request);
        printCookies();
        log.info("availability1 - " + responce1.getResponseCode());
        Assert.assertEquals(responce1.getResponseCode(), 200);
//        end

//        404
        String link404 = "https://www.adidas.ru/sbadhjsabdjhasbd";
        request.setLink(link404);
        request.setRequestProperties(asyncDataHeaders);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);
        log.info("link404 - [START]");
        Response send = sender.send(request);
        printCookies();
        log.info("link404 - " + send.getResponseCode());
        Assert.assertEquals(responce1.getResponseCode(), 404);
//      end

//      availability2
        request.setLink(availabilityLink);
        request.setRequestProperties(asyncDataHeaders);
        request.setDoIn(InputStreamTypes.GZIP);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("availability2 - [START]");
        Response send2 = sender.send(request);
        printCookies();
        Assert.assertEquals(200, send2.getResponseCode());
        log.info("availability2 - " + send2.getResponseCode());
//      end
    }

    public void printCookies() {
        System.out.println("------------------------------------------------------------");
        sender.getCookieManager().getCookieStore().getCookies().forEach(System.out::println);
        System.out.println("============================================================");
    }

    @After
    public void finish() {
    }
}
