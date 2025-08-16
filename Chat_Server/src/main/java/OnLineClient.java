import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class OnLineClient implements Runnable {

    private Socket clientSocket;
    private String clientName;
    private PrintWriter out;
    private BufferedReader in;
    private boolean online;

    public OnLineClient(Socket clientSocket) {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);

            this.clientName = "";

        } catch (IOException e) {
            closeConnection(clientName, this);
        }
    }

    public String enterName() throws IOException {
        out.println("Давайте знакомиться \nКак вас зовут?");
        String name = "";
        while (true) {
            name = in.readLine();
            if (name == null || !name.equals("")) {
                break;
            }
        }
        return name;
    }

    public void sendPersonalMessage(String msg) throws IOException {
        out.println(msg);
        Logger.log("TO " + getName() + " : " + msg, LogStatus.SEND);
    }

    public static void closeConnection(String clientName, OnLineClient client) {
        try {
            String msg = clientName + " отключился ";
            client.setOnline(false);
            Server.sendAllCome(msg, client);
            Logger.log(msg, LogStatus.INFO);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean getOnline() {
        return online;
    }

    public String getName() {
        return clientName;
    }

    public void setName(String name) {
        clientName = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void run() {

        try {
            getNameFromUser();
        } catch (IOException e) {
            closeConnection(clientName, this);
        }

        setOnline(true);

        try {
            Server.sendAllCome("К нам присоединился: " + clientName, this);
            sendPersonalMessage("Добро пожаловать: " + clientName);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.trim().length() > 0) {
                    Logger.log(clientName + ": " + inputLine, LogStatus.RECIEV);
                    Server.sendAllCome(inputLine.trim(), this);
                }
            }

            clientSocket.close();
            closeConnection(clientName, this);

        } catch (IOException eх) {
            closeConnection(clientName, this);
        }
    }

    private void getNameFromUser() throws IOException {
        String clientName;
        while (true) {
            clientName = enterName();
            if (Server.getClients().containsKey(clientName)) {
                sendPersonalMessage("Такое имя уже занято...");
            } else {
                break;
            }
        }
        Logger.log("Новый клиент " + clientName, LogStatus.INFO);
        setName(clientName);

        Server.addInClientMap(clientName, this);
    }
}
