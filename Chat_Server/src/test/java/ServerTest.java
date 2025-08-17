import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerTest {

    private static final Map clients = new HashMap<String, OnLineClient>();

    private static final char PERSONAL_CHAR = '@';

    @Mock
    private ServerSocket mockServerSocket;

    @Mock
    private Socket mockSocket;

    @Mock
    private OnLineClient mockClient;

    @Mock
    private Server mockServer;

    private Server server;

    @Test
    void testServerStart() throws IOException, InterruptedException {
        server = new Server();

        Thread trd = new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        trd.start();
        wait(1000);
        trd.interrupt();

    }

    @Test
    void addClientTest() {

        mockServer.addInClientMap("Петя", mockClient);
        mockServer.addInClientMap("Петя", mockClient);

        Assertions.assertEquals(1, mockServer.getClients().size());
    }

}
