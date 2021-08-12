import com.ventus.core.network.Response;
import com.ventus.core.task.RequestModule;
import com.ventus.core.task.TasksFactory;
import com.ventus.core.util.Context;
import org.junit.Test;

import java.util.Map;

public class ContextTest {
    @Test
    public void doSomething() {
        Context.configureContext("com.ventus.core.proxy");
    }
}
