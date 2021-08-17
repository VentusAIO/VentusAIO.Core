import com.ventus.core.interfaces.IProxy;
import com.ventus.core.models.Proxy;
import com.ventus.core.proxy.ProxyChecker;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ProxyCheckerTest {
    @Test
    public void doCheckProxyForAdidas() {
        //before
        List<IProxy> proxies = new LinkedList<>();
        proxies.add(new Proxy("176.53.176.42", 1337, "savvasiry_gmail_com", "b001060fbf"));
        proxies.add(new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf"));
        //then
        ProxyChecker.check(proxies, "https://www.adidas.ru");
        //penis
        proxies.add(new Proxy("176.53.176.42", 1337, "savvasiry_gmail_com", "b001060fbf"));
    }

    @Test
    public void doCheckProxyForOzon() {
        //before
        List<IProxy> proxies = new LinkedList<>();
        proxies.add(new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf"));
        proxies.add(new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf"));
        //then
        ProxyChecker.check(proxies, "https://www.ozon.ru");
        //penis
    }
}
