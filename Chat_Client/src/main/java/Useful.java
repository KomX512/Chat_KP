import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Useful {

    public static void fillSettingFromIni(Map settings, String INI_FILE) throws IOException {

        String settingString = "";
        String readyString = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(INI_FILE))) {
            while ((settingString = bufferedReader.readLine()) != null) {
                if (settingString.contains(":")) {

                    String setName = settingString.substring(0, settingString.indexOf(':') + 1);

                    if (settings.containsKey(setName)) {
                        settingString = settingString.replaceAll(setName, "");
                        readyString = settingString.trim();
                        settings.put(setName, readyString);
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException ex) {
            Logger.log("Ошибка загрузки параметров!", LogStatus.ERROR);
            System.out.println(ex.getMessage());
        }

    }

}
