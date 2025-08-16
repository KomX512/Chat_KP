
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    private final String INI_FILE = "settings.ini";
    private final String PORT_STRING = "port:";
    private static final Map clients = new HashMap<String, OnLineClient>();
    private static final char PERSONAL_CHAR = '@';

    public Server() {

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

                new Thread(newClient).start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void addInClientMap(String clientName, OnLineClient newClient) {
        clients.put(clientName, newClient);
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

    public static Map getClients() {
        return clients;
    }

    private static boolean chekSendPrivate(String msg, OnLineClient sender) throws IOException {

        for (Object key : clients.keySet()) {
            String nameForSearch = PERSONAL_CHAR + (String) key;

            if (msg.substring(0, nameForSearch.length()).equalsIgnoreCase(nameForSearch)) {
                OnLineClient currentClient = (OnLineClient) clients.get(key);
                Logger.log("Отправка личного сообщения " + sender + " -> " + currentClient, LogStatus.INFO);
                currentClient.sendPersonalMessage(sender.getName() + ": " + msg);
                return true;
            }
        }

        return false;
    }

    public static void sendAllCome(String msg, OnLineClient sender) throws IOException {

        if (msg.charAt(0) == PERSONAL_CHAR) {
            if (chekSendPrivate(msg, sender)) {
                return;
            }
        }

        Logger.log("Отправка сообщения всем от " + sender.getName(), LogStatus.SEND);
        List onDelete = new ArrayList();
        for (Object key : clients.keySet()) {
            OnLineClient currentClient = (OnLineClient) clients.get(key);
            if (currentClient != sender) {
                currentClient.sendPersonalMessage(sender.getName() + ": " + msg);
            }

            if (!currentClient.getOnline()) {
                onDelete.add((String) key);
            }
        }

        if (onDelete.size()>0){
            for (Object currentKey : onDelete){
                clients.remove(currentKey);
            }
        }
    }

}
