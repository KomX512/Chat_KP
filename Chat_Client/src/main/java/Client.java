import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.Cleaner;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;

public class Client {
    private final String INI_FILE = "settings.ini";
    private final String PORT_STRING = "port:";
    private final String HOST_STRING = "host:";
    // commands
    private final String EXIT_COM = "/exit";

    public Client() {

    }

    private int getPort() throws IOException {
        int port = 0;
        String settingString = Useful.getSettingFromIni(PORT_STRING, INI_FILE);
        if (settingString == null) {
            return 0;
        }
        port = Integer.parseInt(settingString);
        return port;
    }

    private String getHost() throws IOException {
        return Useful.getSettingFromIni(HOST_STRING, INI_FILE);
    }

    void start() throws IOException {

        int port = getPort();
        if (port <= 0 || port > 65535) {
            Logger.log("Ошибка загрузки параметров! Ошибочный порт.", LogStatus.ERROR);
            return;
        }

        String serverHost = getHost();
        if (serverHost == null || serverHost.equals("")) {
            Logger.log("Ошибка загрузки параметров!", LogStatus.ERROR);
            return;
        }
        try (Socket clientSocket = new Socket(serverHost, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            Logger.log("Клиент в сети", LogStatus.INFO);
            serverTrackingTread(in);
            sendMessage(out);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(PrintWriter out) throws IOException {
        String inputStr;
        Scanner reader = new Scanner(System.in);
        while ((inputStr = reader.nextLine()) != null) {
            if (inputStr.equalsIgnoreCase(EXIT_COM)) {
                break;
            }
            out.println(inputStr);
            Logger.log(inputStr, LogStatus.SEND);
        }
    }

    private void serverTrackingTread(BufferedReader in) {
        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {

                    System.out.println("[" + LocalDate.now() + "] " + serverMessage);
                    Logger.log(serverMessage, LogStatus.RECIEV);
                }
            } catch (IOException e) {
                System.out.println("До свидания!");
            }
        }).start();
    }
}
