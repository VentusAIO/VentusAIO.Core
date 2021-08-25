package benchmark;

import com.ventus.core.exceptions.Not200CodeException;
import com.ventus.core.models.Proxy;
import com.ventus.core.network.*;
import com.ventus.core.util.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.openjdk.jmh.annotations.*;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
@State(Scope.Benchmark)
@Fork(value = 1, warmups = 0)
public class OzonBenchmark {

//    static PersistentCookieStore ozonCookieStore = new PersistentCookieStore("", "cookies.txt", URI.create("https://www.ozon.ru"));
    static CookieStore cookieStore = new CookieManager().getCookieStore();
    private Sender sender;
//    static Sender sender = new Sender(ozonCookieStore);
    static HashMap<String, String> ozonHeaders = new HashMap<>();

    static {
        HttpCookie httpCookie = new HttpCookie("__Secure-refresh-token", "3.47000308.vwLR97KQTgGexcZn99_XEQ.0.l8cMBQAAAABhJM5LIDffGqN3ZWKrNzk1MjY0NTM1NDIAgJCg.20200528174219.20210825093411.KeeR52pTqkvOw-EUosPpojDdYJOfvv2hggfNAsu9iRM");
        httpCookie.setDomain("ozon.ru");
        httpCookie.setPath("/");
        cookieStore.add(URI.create("https://www.ozon.ru"), httpCookie);
        //Настройка прокси
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");

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

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 0)
    @Measurement(iterations = 2, time = 5)
    @Timeout(time = 20)
    public void test(Proxies proxies) {
        String itemId = "173782895";
        Request request;
        String[] proxy = proxies.proxy.split(":");
        Proxy realProxy = new Proxy(proxy[0], Integer.parseInt(proxy[1]), proxy[2], proxy[3]);

//        cookieStore = new CookieManager().getCookieStore();

        URI uri = URI.create("https://www.ozon.ru");
        request = new Request();
        Response response;
        sender = new Sender(cookieStore);
        sender.changeProxy(realProxy);

        //1
        String link = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2Fcart%3Fdelete%3Dselectedtab0";
//        log.info("cleanBasket - [START]");
        request.setLink(link);
        request.setMethod("POST");
        request.setDoIn(InputStreamTypes.NONE);
        request.setRequestProperties(ozonHeaders);
        response = sender.send(request);
//        printCookies(uri);
//        log.info("cleanBasket - [OK]");
        //1.end

        //2
        link = "https://www.ozon.ru/api/composer-api.bx/_action/addToCart";
//        log.info("addToCart - [START]");
        request.setLink(link);
        request.setData("[{\"id\":" + itemId + ",\"quantity\":" + 1 + "}]");
        response = sender.send(request);
//        printCookies(uri);
//        log.info("addToCart - [OK]");
        //2.end

        //3
        link = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2Fgocheckout%3Fstart%3D0%26page_changed%3Dtrue";
//        log.info("preCheckout - [START]");
        request.setLink(link);
        request.setData("");
        request.setMethod("GET");
        response = sender.send(request);
//        printCookies(uri);
//        log.info("preCheckout - [OK]");
        //3.end

        //4
        link = "https://www.ozon.ru/api/composer-api.bx/page/json/v2?page_changed=true&url=%2Fgocheckout";
//        log.info("goCheckout - [START]");
        request.setLink(link);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);
        response = sender.send(request);
        int responseCod = response.getResponseCode();
        if (responseCod == 302) {
            String str = String.valueOf(response.getHeaderFields().get("Location"));
            String url = "https://www.ozon.ru" + str.substring(1, str.length() - 1);
            request.setLink(url);
            response = sender.send(request);
        }
//        printCookies(uri);
//        log.info("goCheckout - [OK]");
        //4.end

        //5
        link = "https://www.ozon.ru/api/composer-api.bx/_action/createOrder";
//        log.info("createOrder - [START]");
        request.setLink(link);
        request.setDoIn(InputStreamTypes.DEFAULT);
        request.setMethod("GET");
        response = sender.send(request);
//        printCookies(uri);

//        log.info("createOrder - [OK]");
        //5.end


//        Request request;
//
////        cookieStore = new CookieManager().getCookieStore();
//
//        URI uri = URI.create("https://www.ozon.ru");
//        request = new Request();
//        Response response;
//        sender = new Sender(ozonCookieStore);
//
//        //get cookies
//        log.info("get cookies - [START]");
//        request.setLink("https://www.ozon.ru");
//        request.setRequestProperties(ozonHeaders);
//        request.setDoIn(InputStreamTypes.NONE);
//        request.setMethod("GET");
//        Response response1 = sender.send(request);
////        printCookies(uri);
//        log.info("get cookies - [{}]", response1.getResponseCode());
//        log.info("response1 headers: {}", response1.getHeaderFields());
//        Assert.assertEquals(200, response1.getResponseCode());
//        //end
//
//        //get 404
//        log.info("get 404 - [START]");
//        request.setLink("https://www.ozon.ru/sahdofihsadoif");
//        request.setRequestProperties(ozonHeaders);
//        request.setDoIn(InputStreamTypes.NONE);
//        request.setMethod("GET");
//        Response response2 = sender.send(request);
////        printCookies(uri);
//        log.info("get 404 - [{}]", response2.getResponseCode());
//        log.info("response headers: {}", response2.getHeaderFields());
////        Assert.assertEquals(404, response.getResponseCode());
//        //end
//
//        //get summary
//        log.info("get summary - [START]");
//        request.setLink("https://www.ozon.ru/api/composer-api.bx/_action/summary");
//        Response response3 = sender.send(request);
////        printCookies(uri);
//        log.info("get summary - [{}]", response3.getResponseCode());
//        log.info("response headers: {}", response3.getHeaderFields());
//        Assert.assertEquals(200, response3.getResponseCode());
//        //end
//
//        //get asyncData url
//        log.info("get asyncData url - [START]");
//        request.setLink("https://www.ozon.ru/api/composer-api.bx/page/json/v2?url=%2FozonIdIframe%3FreturnUrl%3D%252F");
//        request.setDoIn(InputStreamTypes.GZIP);
//        Response response4 = sender.send(request);
////        printCookies(uri);
//        log.info("get asyncData url - [{}]", response4.getResponseCode());
//        log.info("response headers: {}", response4.getHeaderFields());
//        Assert.assertEquals(200, response4.getResponseCode());
//        //end
//
//        //get asyncData
//        log.info("get asyncData - [START]");
//        String link = "https://www.ozon.ru" + JsonParser.getValue(response4.getData(), "url");
//        link = link.substring(0, link.length() - 1);
//        request.setLink(link);
//        request.setDoIn(InputStreamTypes.GZIP);
//        Response response5 = sender.send(request);
////        printCookies(uri);
//        log.info("get asyncData - [{}]", response5.getResponseCode());
//        log.info("response headers: {}", response5.getHeaderFields());
//        String asyncData;
//        Assert.assertEquals(200, response5.getResponseCode());
//        //end
//
//        asyncData = JsonParser.getValue(response5.getData(), "asyncData");
//        asyncData = asyncData.replace("\\\\u002F", "/");
//        log.info("asyncData - {}", asyncData);
//
//        //get CSRF token
//        String json = "{\"asyncData\":\"" + asyncData + "\",\"componentName\":\"loginOrRegistration\"}";
//        request.setLink("https://www.ozon.ru/api/composer-api.bx/widget/json/v2");
//        request.setData(json);
//        request.setDoIn(InputStreamTypes.GZIP);
//        request.setMethod("POST");
//        log.info("getCsrfToken - [START]");
//        Response response6 = sender.send(request);
//        String authRequestToken = JsonParser.getValue(response6.getData(), "authRequestToken");
//        String csrfToken = JsonParser.getValue(response6.getData(), "csrfToken");
////        printCookies(uri);
//        log.info("get CSRF - [{}]", response6.getResponseCode());
//        log.info("response headers: {}", response6.getHeaderFields());
//        //end
//
//        //login
//        request.setLink("https://www.ozon.ru/api/composer-api.bx/_action/fastEntryV3");
//        json = "{\"authRequestToken\":\"" + authRequestToken + "\",\"isAlphaNumericOtp\":false,\"hideHints\":false,\"csrfToken\":\"" + csrfToken + "\",\"isOtpExpired\":false,\"isAdsAllowed\":false,\"phone\":\"" + "79526453542" + "\"}";
//        request.setData(json);
//        request.setDoIn(InputStreamTypes.GZIP);
//        request.setMethod("POST");
//        log.info("login - [START]");
//        Response response7 = sender.send(request);
//
//        System.out.println(response7.getData());
//
//        int otpId = JsonParser.getIntValue(response7.getData(), "otpId");
////        printCookies(uri);
//        log.info("login - [{}]", response7.getResponseCode());
//        log.info("response headers: {}", response7.getHeaderFields());
//        //end
//
//        //enter login code
//        request.setLink("https://www.ozon.ru/api/composer-api.bx/_action/fastEntryV3");
//
//        System.out.print("Code for " + "79526453542" + ": ");
//        Scanner scanner = new Scanner(System.in);
//        String smsCode = scanner.nextLine();
//
//        json = "{\"phone\":\"" + "79526453542" + "\",\"authRequestToken\":\"" + authRequestToken + "\",\"otpId\":" + otpId + ",\"isAlphaNumericOtp\":false,\"hideHints\":false,\"csrfToken\":\"" + csrfToken + "\",\"isOtpExpired\":false,\"isAdsAllowed\":true,\"otp\":\"" + smsCode + "\"}";
//        log.info("sendCode - [START]");
//        Response response8 = sender.send(request);
////        printCookies(uri);
//        log.info("sendCode - [{}]", response8.getResponseCode());
//        log.info("response headers: {}", response8.getHeaderFields());
//        log.info("response data: {}", response8.getData());
    }

    public void printCookies(URI uri) {
        System.out.println("------------------------------------------------------------");
        sender.getCookieManager().getCookieStore().get(uri).forEach(x -> System.out.printf("%s: %s=%s, (Version=%d, MaxAge=%d, Path=%s)\n", x.getDomain(), x.getName(), x.getValue(), x.getVersion(), x.getMaxAge(), x.getPath()));
        System.out.println("============================================================");
    }
}
