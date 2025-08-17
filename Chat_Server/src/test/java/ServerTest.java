import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

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
    private final String testUserName = "ТЕСТ";
    private OnLineClient testClient;

    @Test
    void testServerStart()  {
        server = new Server();

        Thread trd = new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        trd.start();

        trd.interrupt();
    }

    @Test
    void addClientTest() throws IOException {

        MockitoAnnotations.openMocks(this);
        String testName = "TestUser";

        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        // Act
        OnLineClient client = new OnLineClient(mockSocket);

        for (int i = 0; i < 5; i++) {
            mockServer.addInClientMap(testName, client);
        }

        Assertions.assertEquals(1, mockServer.getClients().size());
    }

}
