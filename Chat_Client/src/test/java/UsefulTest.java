import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class UsefulTest {
    private static final String INI_FILE = "settings.ini";
    private static final String PORT_STRING = "port:";
    private static final String HOST_STRING = "host:";
    private static final String CODEPAGE_STRING = "codepage:";

    public static Stream<Arguments> fillSettingFromIniTest() {
        return Stream.of(
                Arguments.of(PORT_STRING, "3456"),
                Arguments.of(HOST_STRING, "127.0.0.1"),
                Arguments.of(CODEPAGE_STRING, "сp866")
                );
    }

    @ParameterizedTest
    @MethodSource
    void fillSettingFromIniTest(String searchParam, String expected) throws IOException {
        Map<String, String> settings = new HashMap<>();
        settings.put(PORT_STRING, "");
        settings.put(HOST_STRING, "");
        settings.put(CODEPAGE_STRING, "");

        Useful.fillSettingFromIni(settings, INI_FILE);

        Assertions.assertEquals(expected, settings.get(searchParam));
    }


}
