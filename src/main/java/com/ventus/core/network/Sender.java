package com.ventus.core.network;

import com.ventus.core.interfaces.IProxy;
import com.ventus.core.interfaces.IProxyManager;
import com.ventus.core.interfaces.ISender;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.brotli.dec.BrotliInputStream;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

/**
 * Класс отвечающий за отправку http запросов
 */
@Slf4j
public class Sender implements ISender {

    static {
        // global system settings to work with http proxy
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
    }

    /**
     * Поле, в котором будет храниться proxy
     */
    @Getter
    @Setter
    private IProxy proxy = null;

    @Setter
    private IProxyManager proxyManager;
    /**
     * Тип получаемых данных, если таковые надо будет получать
     */
    private InputStreamTypes isDoIn = null;

    @Setter
    @Getter
    private HttpClient httpClient;

    private HttpClient proxiedHttpClient;

    private final HttpClient defaultHttpClient = HttpClient
            .newBuilder()
            .cookieHandler(new CookieManager())
            .build();

    private HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();

    @Getter
    private final CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);


    public Sender() {
        httpClient = defaultHttpClient;
    }

    public Sender(IProxy proxy) {
        changeProxy(proxy);
    }

    public void changeProxy(IProxy proxy) {
        this.setProxy(proxy);
        httpClient = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .proxy(ProxySelector.of(new InetSocketAddress(proxy.getHost(), proxy.getPort())))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                proxy.getLogin(),
                                proxy.getPass().toCharArray()
                        );
                    }
                })
                .build();
    }

    public static void decompressGzipNio(Path source, Path target) throws IOException {

        try (GZIPInputStream gis = new GZIPInputStream(
                new FileInputStream(source.toFile()))) {

            Files.copy(gis, target);
        }

    }

    /**
     * Метод для реализации HTTP запросов
     *
     * @param request запрос, который будет отправляться
     * @return Response ответ сервера
     */
    public Response send(Request request) {
        if (proxyManager != null) {
            if (request.getLink().contains("yoomoney.ru")) {
                disableProxy();
            } else {
                enableProxy();
            }
        }

        try {
            httpRequestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(request.getLink()))
                    .version(HttpClient.Version.HTTP_2)
                    .method(request.getMethod(), request.getData() == null
                            ? HttpRequest.BodyPublishers.noBody()
                            : HttpRequest.BodyPublishers.ofString(request.getData()));
//                    .timeout(Duration.ofMillis(15_000));

            request.getRequestProperties().forEach((k, v) -> httpRequestBuilder.header(k, v == null ? "null" : v));

            HttpRequest httpRequest = httpRequestBuilder.build();

            HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofInputStream());

            Response response = new Response();

            response.setResponseCode(httpResponse.statusCode());
            response.setHeaderFields(httpResponse.headers().map());

            if (httpResponse.statusCode() == 302) {
                log.info("Redirected to : " + httpResponse.headers().map().get("Location"));
            }

            isDoIn = request.getDoIn();

            if (!isDoIn.equals(InputStreamTypes.NONE)) {
                try {
                    BufferedReader in;
                    if (isDoIn == InputStreamTypes.BR) {
                        in = new BufferedReader(new InputStreamReader(new BrotliInputStream(httpResponse.body())));
                    } else if (isDoIn == InputStreamTypes.GZIP) {
                        in = new BufferedReader(new InputStreamReader(new GZIPInputStream(httpResponse.body())));
                    } else {
                        in = new BufferedReader(new InputStreamReader(httpResponse.body()));
                    }
                    String inputLine;
                    StringBuilder res = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        res.append(inputLine);
                    }
                    in.close();
                    response.setData(res.toString());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpResponse.body()));
                    String inputLine;
                    StringBuilder res = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        res.append(inputLine);
                    }
                    in.close();
                    log.error(res.toString());
                    response.setData(res.toString());
                }
            }
            return response;
        } catch (ConnectException e) {
            e.printStackTrace();
            proxyManager.replaceProxy(this);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void disableProxy() {
        proxiedHttpClient = httpClient;
        httpClient = defaultHttpClient;
    }

    private void enableProxy() {
        httpClient = proxiedHttpClient;
    }
}
