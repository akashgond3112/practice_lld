package com.java.lld.design.reader;

import com.java.lld.design.model.CookieLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CookieLogReaderCsv implements CookieLogReader{

    private static final Logger log = LoggerFactory.getLogger(CookieLogReaderCsv.class);

    public List<CookieLogEntry> readEntries(Path logFile) throws IOException {
        log.debug("Reading cookie log entries from file: {}", logFile);
        List<CookieLogEntry> entries = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    log.warn("Skipping blank line in log file: {}", logFile);
                    continue;
                }
                entries.add(parseLine(line));
            }
        }catch (IOException e){
           throw e;
        }

        log.debug("Successfully read {} entries from file: {}", entries.size(), logFile);
        return entries;
    }


    public Map<String, Long> countCookies(Path logFile, LocalDate targetDate) throws IOException {
        Map<String, Long> counts = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            reader.readLine(); // Skip header
            String line;

            while ((line = reader.readLine()) != null) {
                CookieLogEntry entry = parseLine(line);
                LocalDate rowDate = entry.timestamp().toLocalDate();

                if (rowDate.isAfter(targetDate)) {
                    continue; // Still in newer data (the "future" of our target date)
                } else if (rowDate.isBefore(targetDate)) {
                    break;    // Optimization: We've passed our target date. Stop reading!
                } else {
                    // It's the target date!
                    counts.merge(entry.cookie(), 1L, Long::sum);
                }
            }
        }
        return counts;
    }

    public CookieLogEntry parseLine(String line) {
        int separatorIndex = line.indexOf(',');
        String cookie = line.substring(0, separatorIndex);
        OffsetDateTime timestamp = OffsetDateTime.parse(line.substring(separatorIndex + 1));
        return new CookieLogEntry(cookie, timestamp);
    }
}
