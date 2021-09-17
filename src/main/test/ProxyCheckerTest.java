import com.ventus.core.interfaces.IProxy;
import com.ventus.core.models.Proxy;
import com.ventus.core.proxy.ProxyChecker;
import org.junit.Before;
import org.junit.Test;


public class ProxyCheckerTest {

    @Before
    public void configureProxy(){
        //Настройка прокси
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
    }

//    @Test
//    public void doCheckProxyForAdidas() {
//        //before
//        List<IProxy> proxies = new LinkedList<>();
//        proxies.add(new Proxy("176.53.176.42", 1337, "savvasiry_gmail_com", "b001060fbf"));
//        proxies.add(new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf"));
//        //then
//        ProxyChecker.check(proxies, "https://www.adidas.ru");
//        //penis
//        proxies.add(new Proxy("176.53.176.42", 1337, "savvasiry_gmail_com", "b001060fbf"));
//    }
//
//    @Test
//    public void doCheckProxyForOzon() {
//        //before
//        List<IProxy> proxies = new LinkedList<>();
//        proxies.add(new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf"));
//        proxies.add(new Proxy("176.53.166.42", 30001, "savvasiry_gmail_com", "b001060fbf"));
//        //then
//        ProxyChecker.check(proxies, "https://www.ozon.ru");
//        //penis

//    }
    @Test
    public void shouldGetProxyStatus() {
        IProxy proxy = new Proxy("185.5.250.93", 32799, "MflHedSurF", "zoIFHlnqO5");
        System.out.println(ProxyChecker.check(proxy, "https://www.adidas.ru"));
    }

    @Test
    public void shouldGetProxySpeed() {
//        185.5.250.93:32799:MflHedSurF:zoIFHlnqO5
        IProxy proxy = new Proxy("185.5.250.93", 32799, "MflHedSurF", "zoIFHlnqO5");
        System.out.println(ProxyChecker.checkRequestTime(proxy, "https://www.adidas.ru"));
    }

    @Test
    public void shouldGetProxySpeedAndProxyStatus() {
//        45.142.252.130:1050:KaHNc9:K5bfOPvyXP
        IProxy proxy = new Proxy("45.142.252.130", 1050, "KaHNc9", "K5bfOPvyXP");
        System.out.println(ProxyChecker.checkProxy(proxy, "https://www.adidas.ru/yeezy"));
    }
}
