import com.ventus.core.models.Account;
import com.ventus.core.models.AccountManager;
import com.ventus.core.models.Profile;
import com.ventus.core.models.ProfileManager;
import com.ventus.core.network.InputStreamTypes;
import com.ventus.core.network.PersistentCookieStore;
import com.ventus.core.network.Request;
import com.ventus.core.network.Sender;
import com.ventus.core.proxy.Proxy;
import com.ventus.core.proxy.ProxyStatus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;

@Slf4j
public class CookieTest {

    private static final HashMap<String, String> asyncDataHeaders = new HashMap<>();
    private Sender sender;
    private Request request;

    @Before
    @SneakyThrows
    public void start() {
        Proxy proxy = new Proxy("45.134.31.121", 30001, "savvasiry_gmail_com", "b001060fbf");
        PersistentCookieStore cookieStore = new PersistentCookieStore("79526453542", "cookies.txt", new URI("https://www.ozon.ru"));
        Profile profile = Profile.builder().build();
        Account account = Account
                .builder()
                .login("79526453542")
                .pass("penis")
                .uri(URI.create("https://www.ozon.ru"))
                .path("cookies.txt")
                .build();
        proxy.setStatus(ProxyStatus.VALID);
        sender = new Sender(cookieStore);

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

        ProfileManager profileManager = new ProfileManager(Collections.singletonList(profile));
        AccountManager accountManager = new AccountManager(Collections.singletonList(account));
    }

    @Test
    public void test1() throws Exception {
        System.out.println(this.sender.getCookieManager().getCookieStore().getCookies());

        String link = "https://www.ozon.ru/";

        request.setLink(link);
        request.setRequestProperties(asyncDataHeaders);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("goToStartPage - [START]");
        sender.send(request);
        printCookies();
        sender.send(request);
        printCookies();
        log.info("Go to start page - [OK]");

        String link2 = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2FozonIdIframe%3FreturnUrl%3D%252F";

        request.setLink(link2);
        request.setDoIn(InputStreamTypes.BR);

        log.info("getAsyncDataURL - [START]");
        sender.send(request);
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

    @Test
    @SneakyThrows
    public void test2() {
        PersistentCookieStore cookieStore = new PersistentCookieStore("79526453542", "cookies.txt", new URI("https://www.ozon.ru"));
        Proxy proxy = new Proxy("45.134.31.121", 30001, "savvasiry_gmail_com", "b001060fbf");
        proxy.setStatus(ProxyStatus.VALID);
        this.sender = new Sender(cookieStore);
//        this.sender.changeProxy(proxy);

        String link = "https://www.ozon.ru/";

        request.setLink(link);
        request.setRequestProperties(asyncDataHeaders);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        log.info("goToStartPage - [START]");
        sender.send(request);
        printCookies();
        sender.send(request);
        printCookies();
        log.info("Go to start page - [OK]");

        String link2 = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2FozonIdIframe%3FreturnUrl%3D%252F";

        request.setLink(link2);
        request.setDoIn(InputStreamTypes.BR);

        log.info("getAsyncDataURL - [START]");
        sender.send(request);
        printCookies();
        log.info("getAsyncDataURL - [OK]");
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
