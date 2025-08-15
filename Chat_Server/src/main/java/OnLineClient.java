import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OnLineClient implements Runnable {
    private Socket clientSocket;
    private String clientName;
    private PrintWriter out;
    private BufferedReader in;

    public OnLineClient(Socket clientSocket) {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            clientName = "";

        } catch (IOException e) {
            closeConnection(clientName, this);
        }
    }

    public String enterName() throws IOException {
        out.println("Давайте знакомиться \nКак вас зовут?");
        String name = "";
        while (true) {
            name = in.readLine();
            if (name == null || !name.equals("")){
                break;
            }
        }
        return name;
    }

    public void sendPersonalMessage(String msg) throws IOException {
        out.println(msg);
        Logger.log(msg, LogStatus.SEND);
    }

    public static void closeConnection(String clientName, OnLineClient client) {
        try {
            String msg = clientName + " отключился ";
            Server.sendAllCome(msg, client);
            Logger.log(msg, LogStatus.INFO);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getName() {
        return clientName;
    }

    @Override
    public void run() {

    }
}
