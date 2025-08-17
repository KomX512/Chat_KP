import java.io.*;
import java.lang.ref.Cleaner;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private final String INI_FILE = "settings.ini";
    private final String PORT_STRING = "port:";
    private final String HOST_STRING = "host:";
    private final String CODEPAGE_STRING = "codepage:";

    private Map<String, String> settings;
    // commands
    private final String EXIT_COM = "/exit";

    public Client() throws IOException {
        initSettings(INI_FILE);
    }

    private void initSettings(String iniFile) throws IOException {

        settings = new HashMap<>();
        settings.put(PORT_STRING, "");
        settings.put(HOST_STRING, "");
        settings.put(CODEPAGE_STRING, "");

        Useful.fillSettingFromIni(settings, iniFile);
    }

    private int getPort() {
        int port = 0;

        String settingString = settings.get(PORT_STRING).trim();

        if (settingString == null || settingString.equals("")) {
            return 0;
        }
        port = Integer.parseInt(settingString);
        return port;
    }

    private String getHost() {
        return settings.get(HOST_STRING).trim();
    }

    private String getCodepage() {
        return settings.get(CODEPAGE_STRING).trim();
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
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);) {
            Logger.log("Клиент в сети", LogStatus.INFO);
            serverTrackingTread(in);
            sendMessage(out);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(PrintWriter out) throws IOException {
        String inputStr;
        String codePage = getCodepage();
        Scanner reader;
        try {
            if (codePage.equals("")) {
                reader = new Scanner(System.in);
            } else {
                reader = new Scanner(System.in, codePage); //ДЛЯ РУССКОГО В КОНСОЛИ!!!!
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

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
