import org.junit.Assert;
import org.junit.Test;

import static com.ventus.core.util.Random.getRandom;


public class RandomTest {
    @Test
    public void shouldSaveCookiesFromAdidas() {
        for(int i = 0; i < 100; i++){
            Assert.assertEquals(getRandom(i, i), i);
        }
    }
}
