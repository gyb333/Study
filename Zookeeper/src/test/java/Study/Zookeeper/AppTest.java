package Study.Zookeeper;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class AppTest {
    @Test
    public void Test(){
        try {
            String m = DigestAuthenticationProvider.generateDigest("super:super");
            System.out.println(m);
            Assert.assertTrue(true);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
