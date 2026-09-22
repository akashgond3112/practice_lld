package cookie.support;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CookieLogTestFixtures {

    private static final String SAMPLE_LOG = "/cookie_log.csv";

    private CookieLogTestFixtures() {
    }

    public static Path writeSampleLog(Path directory) throws IOException {
        try (InputStream inputStream = resourceStream()) {
            Path logFile = directory.resolve("cookie_log.csv");
            Files.copy(inputStream, logFile);
            return logFile;
        }
    }

    public static Path writeLog(Path directory, String content) throws IOException {
        Path logFile = directory.resolve("cookie_log.csv");
        Files.writeString(logFile, content);
        return logFile;
    }

    public static Path writeEmptyLog(Path directory) throws IOException {
        return writeLog(directory, "");
    }

    public static Path writeHeaderOnlyLog(Path directory) throws IOException {
        return writeLog(directory, "cookie,timestamp\n");
    }

    private static InputStream resourceStream() {
        InputStream inputStream = CookieLogTestFixtures.class.getResourceAsStream(CookieLogTestFixtures.SAMPLE_LOG);
        if (inputStream == null) {
            throw new IllegalStateException("Missing test resource: " + CookieLogTestFixtures.SAMPLE_LOG);
        }
        return inputStream;
    }
}
