import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Useful {

    public static String getSettingFromIni(String setName, String INI_FILE) throws IOException {
        //Делаем чтение настроек циклом, может потом еще настройки надо будет читать
        String settingString = "";
        String readyString = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(INI_FILE))) {
            while ((settingString = bufferedReader.readLine()) != null) {
                if (settingString.contains(setName)) {
                    settingString = settingString.replaceAll(setName, "");
                    readyString = settingString.trim();
                }
            }
            bufferedReader.close();
        } catch (IOException ex) {
            Logger.log("Ошибка загрузки параметров!", LogStatus.ERROR);
            System.out.println(ex.getMessage());
        }

        return readyString;
    }

}
