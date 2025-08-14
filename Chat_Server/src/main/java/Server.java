
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final String INI_FILE = "settings.ini";
    private final String PORT_STRING = "port:";
    private final Map clients = new HashMap<String, OnLineClient>();

    public Server() {

    }

    private int getPort() {
        int port = 0;
        String settingString = Useful.getSettingFromIni(PORT_STRING, INI_FILE);
        if (settingString == null) {
            return 0;
        }
        port = Integer.parseInt(settingString);
        return port;
    }

    public static void sendAllCome(String msg, OnLineClient sender) {

    }

    public void start() throws IOException {
        int port = getPort();
        if (port == 0) {
            Logger.log("Ошибка загрузки параметров!", LogStatus.ERROR);
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.log("Сервер запущен.", LogStatus.INFO);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Logger.log("Новое подключение: " + clientSocket, LogStatus.INFO);

                OnLineClient newClient = new OnLineClient(clientSocket);
                String clientName;
                while (true) {
                    clientName = newClient.enterName();
                    if (clients.containsKey(clientName)) {
                        newClient.sendPersonalMessage("Такое имя уже занято...");
                    }else{
                        break;
                    }
                }

                clients.put(clientName, newClient);
                new Thread(newClient).start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
