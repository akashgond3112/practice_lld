package cookie.service;

import com.java.lld.design.reader.CookieLogReaderCsv;
import com.java.lld.design.service.MostActiveCookieService;

import cookie.support.CookieLogTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Most active cookie service")
class MostActiveCookieServiceTest {

    @TempDir
    Path tempDir;

    private MostActiveCookieService mostActiveCookieService;

    @BeforeEach
    void setUp() {
        mostActiveCookieService = new MostActiveCookieService(new CookieLogReaderCsv());
    }

    @Test
    @DisplayName("returns the cookie seen most often on the requested day")
    void shouldReturnMostActiveCookieForGivenDate() throws Exception {
        Path logFile = CookieLogTestFixtures.writeSampleLog(tempDir);

        List<String> mostActiveCookies = mostActiveCookieService.findMostActiveCookies(
                logFile,
                LocalDate.parse("2018-12-09")
        );

        assertThat(mostActiveCookies).containsExactly("AtY0laUfhglK3lC7");
    }

    @Test
    @DisplayName("returns all cookies when multiple cookies share the highest count")
    void shouldReturnAllCookiesWhenThereIsATie() throws Exception {
        Path logFile = CookieLogTestFixtures.writeLog(tempDir, """
                cookie,timestamp
                cookieA,2018-12-09T14:19:00+00:00
                cookieB,2018-12-09T10:13:00+00:00
                cookieA,2018-12-09T07:25:00+00:00
                cookieB,2018-12-09T06:19:00+00:00
                """);

        List<String> mostActiveCookies = mostActiveCookieService.findMostActiveCookies(
                logFile,
                LocalDate.parse("2018-12-09")
        );

        assertThat(mostActiveCookies).containsExactly("cookieA", "cookieB");
    }

    @Test
    @DisplayName("returns an empty list when no cookies exist for the requested day")
    void shouldReturnEmptyListWhenNoCookiesExistForDate() throws Exception {
        Path logFile = CookieLogTestFixtures.writeLog(tempDir, """
                cookie,timestamp
                AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00
                """);

        List<String> mostActiveCookies = mostActiveCookieService.findMostActiveCookies(
                logFile,
                LocalDate.parse("2018-12-08")
        );

        assertThat(mostActiveCookies).isEmpty();
    }

    @ParameterizedTest(name = "returns {1} for date {0}")
    @CsvSource({
            "2018-12-09, AtY0laUfhglK3lC7",
            "2018-12-08, 4sMM2LxV07bPJzwf | SAZuXPGUrfbcn5UA | fbcn5UAVanZf6UtG"
    })
    @DisplayName("identifies the most active cookie(s) for sample log dates")
    void shouldIdentifyMostActiveCookiesForSampleLogDates(String date, String expectedCookies) throws Exception {
        Path logFile = CookieLogTestFixtures.writeSampleLog(tempDir);

        List<String> mostActiveCookies = mostActiveCookieService.findMostActiveCookies(
                logFile,
                LocalDate.parse(date)
        );

        assertThat(mostActiveCookies).containsExactly(expectedCookies.split(" \\| "));
    }

    @Test
    @DisplayName("filters entries using UTC dates")
    void shouldFilterEntriesUsingUtcDates() throws Exception {
        Path logFile = CookieLogTestFixtures.writeLog(tempDir, """
                cookie,timestamp
                utcCookie,2018-12-09T23:59:59+00:00
                previousDayCookie,2018-12-08T22:03:00+00:00
                """);

        List<String> mostActiveCookies = mostActiveCookieService.findMostActiveCookies(
                logFile,
                LocalDate.parse("2018-12-09")
        );

        assertThat(mostActiveCookies).containsExactly("utcCookie");
    }

    @Test
    @DisplayName("returns an empty list when the log file is empty")
    void shouldReturnEmptyListWhenLogFileIsEmpty() throws Exception {
        Path logFile = CookieLogTestFixtures.writeEmptyLog(tempDir);

        List<String> mostActiveCookies = mostActiveCookieService.findMostActiveCookies(
                logFile,
                LocalDate.parse("2018-12-09")
        );

        assertThat(mostActiveCookies).isEmpty();
    }

    @Test
    @DisplayName("returns an empty list when the log file contains only the header")
    void shouldReturnEmptyListWhenLogFileContainsHeaderOnly() throws Exception {
        Path logFile = CookieLogTestFixtures.writeHeaderOnlyLog(tempDir);

        List<String> mostActiveCookies = mostActiveCookieService.findMostActiveCookies(
                logFile,
                LocalDate.parse("2018-12-09")
        );

        assertThat(mostActiveCookies).isEmpty();
    }

    @Test
    @DisplayName("throws when the log file does not exist")
    void shouldThrowWhenLogFileDoesNotExist() {
        Path missingFile = tempDir.resolve("missing.csv");

        assertThatThrownBy(() -> mostActiveCookieService.findMostActiveCookies(
                missingFile,
                LocalDate.parse("2018-12-09")
        )).isInstanceOf(java.nio.file.NoSuchFileException.class);
    }

    @Test
    @DisplayName("throws when the CSV contains an invalid timestamp")
    void shouldThrowWhenCsvTimestampIsInvalid() throws Exception {
        Path logFile = CookieLogTestFixtures.writeLog(tempDir, """
                cookie,timestamp
                cookieA,not-a-date
                """);

        assertThatThrownBy(() -> mostActiveCookieService.findMostActiveCookies(
                logFile,
                LocalDate.parse("2018-12-09")
        )).isInstanceOf(DateTimeParseException.class);
    }
}
