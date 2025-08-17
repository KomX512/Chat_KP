import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OnLineClientTest {

    @Mock
    private Socket mockSocket;

    @Mock
    private PrintWriter mockWriter;

    @Test
    public void testConstructorAndSendMessage() throws Exception {
        // Arrange
        MockitoAnnotations.openMocks(this);
        String testName = "TestUser";

        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        // Act
        OnLineClient client = new OnLineClient(mockSocket);
        client.setName(testName);
        client.sendPersonalMessage("Hello, world!");

        // Assert

    }
}