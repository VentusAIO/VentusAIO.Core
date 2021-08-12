import com.ventus.core.models.Proxy;
import com.ventus.core.network.InputStreamTypes;
import com.ventus.core.network.Response;
import com.ventus.core.network.Sender;
import com.ventus.core.proxy.ProxyStatus;
import com.ventus.core.task.RequestModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CookieTest extends RequestModule {

    private static HashMap<String, String> asyncDataHeaders = new HashMap<>();

    @Before
    public void start() {
        Proxy proxy = new Proxy("45.134.31.121", 30001, "savvasiry_gmail_com", "b001060fbf");
        proxy.setStatus(ProxyStatus.VALID);
        sender = new Sender(proxy);

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
    public void test1() throws Exception {
        Assert.assertTrue(this.sender.getCookieManager().getCookieStore().getCookies().isEmpty());

        String link = "https://www.ozon.ru/";

        log.info("goToStartPage - [START]");
        send("GET", link, "", asyncDataHeaders, InputStreamTypes.NONE);
        printCookies();
        send("GET", link, "", asyncDataHeaders, InputStreamTypes.NONE);
        printCookies();
        log.info("Go to start page - [OK]");

        String link2 = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2FozonIdIframe%3FreturnUrl%3D%252F";

        log.info("getAsyncDataURL - [START]");
        send("GET", link2, "", asyncDataHeaders, InputStreamTypes.BR);
        printCookies();
        log.info("getAsyncDataURL - [OK]");





/*        String link3 = "https://javarush.ru/groups/posts/605-junit";

        log.info("javarush - [START]");
        send("GET", link3, "", asyncDataHeaders, InputStreamTypes.NONE);
        printCookies();
        send("GET", link3, "", asyncDataHeaders, InputStreamTypes.NONE);
        printCookies();
        log.info("javarush  - [OK]");*/

        Assert.assertNotNull(this.sender.getCookieManager().getCookieStore().getCookies());
        Assert.assertFalse(this.sender.getCookieManager().getCookieStore().getCookies().isEmpty());
    }

    public void printCookies() {
        sender.getCookieManager().getCookieStore().getCookies().forEach(System.out::println);
    }

    @After
    public void finish() {

    }

    @Override
    protected void postSend(Response response) throws Exception {

    }

    @Override
    public Map<?, ?> call() {
        return null;
    }
}
