package cookie.reader;

import com.java.lld.design.model.CookieLogEntry;
import com.java.lld.design.reader.CookieLogReader;
import com.java.lld.design.reader.CookieLogReaderCsv;

import cookie.support.CookieLogTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Cookie log reader")
class CookieLogReaderTest {

    private final CookieLogReader cookieLogReader = new CookieLogReaderCsv();

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("reads all entries and skips the header row")
    void shouldReadAllEntriesAndSkipHeader() throws Exception {
        Path logFile = CookieLogTestFixtures.writeSampleLog(tempDir);

        List<CookieLogEntry> entries = cookieLogReader.readEntries(logFile);

        assertThat(entries).hasSize(8);
        assertThat(entries.getFirst().cookie()).isEqualTo("AtY0laUfhglK3lC7");
    }

    @Test
    @DisplayName("ignores blank lines in the log file")
    void shouldIgnoreBlankLines() throws Exception {
        Path logFile = CookieLogTestFixtures.writeLog(tempDir, """
                cookie,timestamp
                cookieA,2018-12-09T14:19:00+00:00

                cookieB,2018-12-09T10:13:00+00:00
                """);

        List<CookieLogEntry> entries = cookieLogReader.readEntries(logFile);

        assertThat(entries)
                .extracting(CookieLogEntry::cookie)
                .containsExactly("cookieA", "cookieB");
    }

    @Test
    @DisplayName("parses cookie and timestamp from a CSV line")
    void shouldParseCookieAndTimestampFromLine() {
        CookieLogEntry entry = cookieLogReader.parseLine("AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00");

        assertThat(entry.cookie()).isEqualTo("AtY0laUfhglK3lC7");
        assertThat(entry.timestamp()).isEqualTo(OffsetDateTime.parse("2018-12-09T14:19:00+00:00"));
        assertThat(entry.occurredOn(LocalDate.parse("2018-12-09"))).isTrue();
    }

    @Test
    @DisplayName("returns no entries when the log file is empty")
    void shouldReturnNoEntriesWhenLogFileIsEmpty() throws Exception {
        Path logFile = CookieLogTestFixtures.writeEmptyLog(tempDir);

        List<CookieLogEntry> entries = cookieLogReader.readEntries(logFile);

        assertThat(entries).isEmpty();
    }

    @Test
    @DisplayName("returns no entries when the log file contains only the header")
    void shouldReturnNoEntriesWhenLogFileContainsHeaderOnly() throws Exception {
        Path logFile = CookieLogTestFixtures.writeHeaderOnlyLog(tempDir);

        List<CookieLogEntry> entries = cookieLogReader.readEntries(logFile);

        assertThat(entries).isEmpty();
    }

    @Test
    @DisplayName("throws when the log file does not exist")
    void shouldThrowWhenLogFileDoesNotExist() {
        Path missingFile = tempDir.resolve("missing.csv");

        assertThatThrownBy(() -> cookieLogReader.readEntries(missingFile))
                .isInstanceOf(java.nio.file.NoSuchFileException.class);
    }

    @Test
    @DisplayName("throws when the timestamp column is invalid")
    void shouldThrowWhenTimestampIsInvalid() {
        assertThatThrownBy(() -> cookieLogReader.parseLine("cookieA,not-a-date"))
                .isInstanceOf(java.time.format.DateTimeParseException.class);
    }

    @Test
    @DisplayName("throws when a data row is malformed")
    void shouldThrowWhenRowIsMalformed() throws Exception {
        Path logFile = CookieLogTestFixtures.writeLog(tempDir, """
                cookie,timestamp
                onlyCookieNoTimestamp
                """);

        assertThatThrownBy(() -> cookieLogReader.readEntries(logFile))
                .isInstanceOf(StringIndexOutOfBoundsException.class);
    }
}
