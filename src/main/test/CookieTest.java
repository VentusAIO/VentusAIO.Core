import com.ventus.core.models.Proxy;
import com.ventus.core.models.Account;
import com.ventus.core.models.AccountManager;
import com.ventus.core.models.Profile;
import com.ventus.core.models.ProfileManager;
import com.ventus.core.network.*;
import com.ventus.core.proxy.ProxyStatus;
import com.ventus.core.task.RequestModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.CookieStore;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CookieTest {

    private static final HashMap<String, String> asyncDataHeaders = new HashMap<>();
    private Sender sender;
    private Request request;

    @Before
    @SneakyThrows
    public void start() {
        Proxy proxy = new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf");
//        PersistentCookieStore cookieStore = new PersistentCookieStore("79526453542", "cookies.txt", new URI("https://www.ozon.ru"));
        Profile profile = Profile.builder().build();
//        Account account = Account
//                .builder()
//                .login("79526453542")
//                .pass("penis")
//                .uri(URI.create("https://www.ozon.ru"))
//                .path("cookies.txt")
//                .build();
        proxy.setStatus(ProxyStatus.VALID);
//        sender = new Sender(cookieStore);
        sender = new Sender();
//        sender.changeProxy(proxy);

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
//        AccountManager accountManager = new AccountManager(Collections.singletonList(account));
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
        printCookies(sender.getCookieManager().getCookieStore());
        sender.send(request);
        printCookies(sender.getCookieManager().getCookieStore());
        log.info("Go to start page - [OK]");

        String link2 = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2FozonIdIframe%3FreturnUrl%3D%252F";

        request.setLink(link2);
        request.setDoIn(InputStreamTypes.BR);

        log.info("getAsyncDataURL - [START]");
        sender.send(request);
        printCookies(sender.getCookieManager().getCookieStore());
        log.info("getAsyncDataURL - [OK]");

        Assert.assertNotNull(this.sender.getCookieManager().getCookieStore().getCookies());
        Assert.assertFalse(this.sender.getCookieManager().getCookieStore().getCookies().isEmpty());
    }

    @Test
    @SneakyThrows
    public void test2() {
        PersistentCookieStore cookieStore = new PersistentCookieStore("79526494542", "cookies.txt", new URI("https://www.ozon.ru"));
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
        Response response = sender.send(request);
//        System.out.println("Actual Cookies: \n" + response.getSetCookies() + "\nActual Cookies.");
        printCookies(cookieStore);
        request.setLink("https://www.adidas.ru");
        response = sender.send(request);
//        System.out.println("Actual Cookies: \n" + response.getSetCookies() + "\nActual Cookies.");
        printCookies(cookieStore);
        log.info("Go to start page - [OK]");

        String link2 = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2FozonIdIframe%3FreturnUrl%3D%252F";

        request.setLink(link2);
//        request.setDoIn(InputStreamTypes.BR);

        log.info("getAsyncDataURL - [START]");
        response = sender.send(request);
//        System.out.println("Actual Cookies: \n" + response.getSetCookies() + "\nActual Cookies.");
        printCookies(cookieStore);
        log.info("getAsyncDataURL - [OK]");
    }

    public void printCookies(CookieStore cookieStore) {
        System.out.println("------------------------------- Saved Cookies -------------------------------");
        cookieStore.getCookies().forEach(x -> System.out.printf("%s: %s\n", x.getDomain(), x));
        System.out.println("=============================== Saved Cookies ===============================");
    }

    @After
    public void finish() {

    }
}
