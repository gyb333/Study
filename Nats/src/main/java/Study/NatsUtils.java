package Study;


import java.time.Duration;

import io.nats.client.AuthHandler;
import io.nats.client.Connection;
import io.nats.client.ConnectionListener;
import io.nats.client.Nats;
import io.nats.client.Options;

public class NatsUtils {

    public static Options createOptions(String[] servers, boolean allowReconnect) throws Exception {

        Options.Builder builder = new Options.Builder().
                servers(servers).connectionTimeout(Duration.ofSeconds(5)).
                pingInterval(Duration.ofSeconds(10)).
                reconnectWait(Duration.ofSeconds(1)).
                connectionListener(new ConnectionListener(){
                    public void connectionEvent(Connection conn, Events type) {
                        System.out.println("Status change "+type);
                    }
                });

        if (!allowReconnect) {
            builder = builder.noReconnect();
        } else {
            builder = builder.maxReconnects(-1);
        }

        if (System.getenv("NATS_NKEY") != null && System.getenv("NATS_NKEY") != "") {
            AuthHandler handler = new NatsAuthHandler(System.getenv("NATS_NKEY"));
            builder.authHandler(handler);
        } else if (System.getenv("NATS_CREDS") != null && System.getenv("NATS_CREDS") != "") {
            builder.authHandler(Nats.credentials(System.getenv("NATS_CREDS")));
        }

        return builder.build();
    }
}
