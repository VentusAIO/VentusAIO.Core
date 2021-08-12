package com.ventus.core.proxy;

import com.ventus.core.interfaces.IProxy;
import com.ventus.core.network.InputStreamTypes;
import com.ventus.core.network.Request;
import com.ventus.core.network.Response;
import com.ventus.core.network.Sender;
import lombok.SneakyThrows;


public class ProxyCheckerTask implements Runnable {
    private final IProxy proxyPair;
    private final Sender sender;
    private final String url;

    public ProxyCheckerTask(IProxy proxyPair, String url) {
        this.url = url;
        this.proxyPair = proxyPair;
        this.sender = new Sender(proxyPair);
    }

    @SneakyThrows
    @Override
    public void run() {
        String result;

        Request request = new Request();
//        request.setRequestProperties(AdidasConfig.getAdidas());
        request.setLink(url);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        Response response = sender.send(request);

        proxyPair.setStatus(((response.getResponseCode() == 200 || response.getResponseCode() == 302) ? ProxyStatus.VALID : ProxyStatus.INVALID));
        result = String.format("Proxy: {%s} -- %s[%d], for host: %s", proxyPair.getHost(), url, response.getResponseCode(), url);
        ProxyChecker.callback(result, proxyPair);
    }
}
