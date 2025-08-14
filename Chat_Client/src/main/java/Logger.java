import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Logger {
    private static final String LOG_FILE = "logfile.txt";

    public static void log(String message, LogStatus status) throws IOException {
        String msg = "[" + LocalDate.now() + "] (" + status + ") " + message;
        System.out.println(msg);
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            fileWriter.write(msg + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            throw new IOException();
        }
    }

}
