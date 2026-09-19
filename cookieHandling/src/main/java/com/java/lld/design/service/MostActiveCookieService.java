package com.java.lld.design.service;

import com.java.lld.design.model.CookieLogEntry;
import com.java.lld.design.reader.CookieLogReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MostActiveCookieService {

    private static final Logger log = LoggerFactory.getLogger(MostActiveCookieService.class);

    private final CookieLogReader cookieLogReader;

    public MostActiveCookieService(CookieLogReader cookieLogReader) {
        this.cookieLogReader = cookieLogReader;
    }

    public List<String> findMostActiveCookies(Path logFile, LocalDate date) throws IOException {
        log.debug("Finding most active cookies for date: {} in file: {}", date, logFile);

        Map<String, Long> cookieCounts = countCookiesForDate(logFile, date);

        if (cookieCounts.isEmpty()) {
            log.warn("No cookies found for date: {}", date);
            return List.of();
        }

        long highestCount = cookieCounts.values().stream()
                .max(Comparator.naturalOrder())
                .orElseThrow();

        List<String> result = cookieCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == highestCount)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        log.info("Most active cookie(s) for {}: {} (count={})", date, result, highestCount);
        return result;
    }

    /**
     * Counts cookies for the given date using early termination, exploiting the guarantee
     * that the log file is sorted in <b>descending</b> order by timestamp (most recent first).
     *
     * <ul>
     *   <li>{@code dropWhile} — skips entries <em>after</em> the target date (more recent)</li>
     *   <li>{@code takeWhile} — stops as soon as entries go <em>before</em> the target date</li>
     * </ul>
     *
     * <p><b>Time Complexity: O(P)</b> where P = entries up to and including the target date.
     * In practice P &lt;&lt; N for large log files, unlike a full O(N) scan.</p>
     *
     * @param logFile the path to the log file, sorted descending by timestamp
     * @param date    the target date to count cookies for
     * @return a map of cookie name to occurrence count for the given date
     * @throws IOException if the log file cannot be read
     */
    private Map<String, Long> countCookiesForDate(Path logFile, LocalDate date) throws IOException {
        Map<String, Long> counts = cookieLogReader.readEntries(logFile).stream()
                .dropWhile(entry -> entry.occurredAfter(date))
                .takeWhile(entry -> entry.occurredOn(date))
                .collect(Collectors.groupingBy(CookieLogEntry::cookie, Collectors.counting()));

        log.debug("Found {} unique cookie(s) for date: {}", counts.size(), date);
        return counts;
    }
}
