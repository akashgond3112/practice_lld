package cookie.command;

import com.java.lld.design.command.CookieFilterCommand;
import com.java.lld.design.reader.CookieLogReaderCsv;
import com.java.lld.design.service.MostActiveCookieService;
import cookie.support.CookieLogTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import picocli.CommandLine;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@DisplayName("Cookie filter command")
class CookieFilterCommandTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("writes the most active cookie to standard output")
    void shouldWriteMostActiveCookieToStandardOutput(CapturedOutput output) throws Exception {
        Path logFile = CookieLogTestFixtures.writeSampleLog(tempDir);
        CookieFilterCommand command = new CookieFilterCommand(
                new MostActiveCookieService(new CookieLogReaderCsv())
        );

        int exitCode = new CommandLine(command).execute(
                "-f", logFile.toString(),
                "-d", "2018-12-09"
        );

        assertThat(exitCode).isZero();
        assertThat(output.getOut().trim()).isEqualTo("AtY0laUfhglK3lC7");
    }

    @Test
    @DisplayName("writes each tied cookie on a separate line")
    void shouldWriteEachTiedCookieOnSeparateLine(CapturedOutput output) throws Exception {
        Path logFile = CookieLogTestFixtures.writeLog(tempDir, """
                cookie,timestamp
                cookieA,2018-12-09T14:19:00+00:00
                cookieB,2018-12-09T10:13:00+00:00
                cookieA,2018-12-09T07:25:00+00:00
                cookieB,2018-12-09T06:19:00+00:00
                """);
        CookieFilterCommand command = new CookieFilterCommand(
                new MostActiveCookieService(new CookieLogReaderCsv())
        );

        new CommandLine(command).execute(
                "-f", logFile.toString(),
                "-d", "2018-12-09"
        );

        assertThat(output.getOut().lines()).containsExactly("cookieA", "cookieB");
    }

    @Test
    @DisplayName("produces no output when no cookies match the requested date")
    void shouldProduceNoOutputWhenNoCookiesMatchDate(CapturedOutput output) throws Exception {
        Path logFile = CookieLogTestFixtures.writeSampleLog(tempDir);
        CookieFilterCommand command = new CookieFilterCommand(
                new MostActiveCookieService(new CookieLogReaderCsv())
        );

        int exitCode = new CommandLine(command).execute(
                "-f", logFile.toString(),
                "-d", "2020-01-01"
        );

        assertThat(exitCode).isZero();
        assertThat(output.getOut().trim()).isEmpty();
    }

    @Test
    @DisplayName("shows usage when required options are missing")
    void shouldShowUsageWhenRequiredOptionsAreMissing(CapturedOutput output) {
        CookieFilterCommand command = new CookieFilterCommand(
                new MostActiveCookieService(new CookieLogReaderCsv())
        );

        int exitCode = new CommandLine(command).execute();

        assertThat(exitCode).isNotZero();
        assertThat(output.getErr() + output.getOut())
                .contains("Missing required options")
                .contains("-f")
                .contains("-d");
    }

    @Test
    @DisplayName("fails when the date format is invalid")
    void shouldFailWhenDateFormatIsInvalid(CapturedOutput output) throws Exception {
        Path logFile = CookieLogTestFixtures.writeSampleLog(tempDir);
        CookieFilterCommand command = new CookieFilterCommand(
                new MostActiveCookieService(new CookieLogReaderCsv())
        );

        int exitCode = new CommandLine(command).execute(
                "-f", logFile.toString(),
                "-d", "09-12-2018"
        );

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.getErr()).contains("Invalid date format. Expected: yyyy-MM-dd (UTC)");
    }

    @Test
    @DisplayName("fails when the log file does not exist")
    void shouldFailWhenLogFileDoesNotExist(CapturedOutput output) {
        CookieFilterCommand command = new CookieFilterCommand(
                new MostActiveCookieService(new CookieLogReaderCsv())
        );

        int exitCode = new CommandLine(command).execute(
                "-f", tempDir.resolve("missing.csv").toString(),
                "-d", "2018-12-09"
        );

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.getErr()).contains("Log file not found");
    }

    @Test
    @DisplayName("fails when the CSV timestamp is invalid")
    void shouldFailWhenCsvTimestampIsInvalid(CapturedOutput output) throws Exception {
        Path logFile = CookieLogTestFixtures.writeLog(tempDir, """
                cookie,timestamp
                cookieA,not-a-date
                """);
        CookieFilterCommand command = new CookieFilterCommand(
                new MostActiveCookieService(new CookieLogReaderCsv())
        );

        int exitCode = new CommandLine(command).execute(
                "-f", logFile.toString(),
                "-d", "2018-12-09"
        );

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.getErr()).contains("Invalid timestamp in log file.");
    }
}
