package benchmark;

import org.openjdk.jmh.annotations.Param;
import com.ventus.core.models.Proxy;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class Proxies {
    public Proxy proxy1 = new Proxy("185.40.7.52", 21321, "YoDKLFO9Ul", "C0Zqo7gnYz");
    public Proxy proxy2 = new Proxy("185.58.207.246", 30876, "rmcufUiwOj", "U1cPRYir45");
    public Proxy proxy3 = new Proxy("185.94.165.165", 17961, "8k9giK4ASr", "FaEWXpvVRz");
    public Proxy proxy4 = new Proxy("185.117.155.178", 31000, "c0GslBQTvq", "HDrhLXFztg");
    public Proxy proxy5 = new Proxy("185.5.249.203", 25143, "TmQB7qneH2", "ieSMpN65mI");
    @Param(value = {"185.58.207.246:30876:rmcufUiwOj:U1cPRYir45",
            "185.94.165.165:17961:8k9giK4ASr:FaEWXpvVRz",
            "185.117.155.178:31000:c0GslBQTvq:HDrhLXFztg",
            "185.94.165.178:19743:va29OVhD8R:xKfqZBQwWu",
            "185.5.249.203:25143:TmQB7qneH2:ieSMpN65mI"
            })
    public String proxy;
}
