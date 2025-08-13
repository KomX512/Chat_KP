import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final String INI_FILE = "settings.ini";
    private final String PORT_STRING = "port:";

    public Server() {

    }

    private int getPort() {
        int port = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(INI_FILE))) {
            String currenString;
            while ((currenString = bufferedReader.readLine()) != null) {
                if (currenString.contains(PORT_STRING)) {
                    currenString = currenString.replaceAll(PORT_STRING, "");
                    currenString = currenString.replaceAll(" ", "");
                    port = Integer.parseInt(currenString);
                }
            }
            bufferedReader.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        //Делаем чтение настроек циклом, может потом еще настройки надо будет читать

        return port;
    }

    public void start() {
        int port = getPort();
        if (port == 0) {
            System.out.println("Ошибка загрузки параметров!");
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            Socket clientSocket = serverSocket.accept();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
