package com.java.lld.design.command;

import com.java.lld.design.service.MostActiveCookieService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.Callable;

@Component
@Command(name = "cookie-filter", description = "Find the most active cookie for a specific day")
public class CookieFilterCommand implements Callable<Integer> {

    private final MostActiveCookieService mostActiveCookieService;

    @Option(names = "-f", required = true, description = "Path to the cookie log file")
    private File logFile;

    @Option(names = "-d", required = true, description = "Date in UTC (yyyy-MM-dd)")
    private String date;

    @Option(names = "-s", required = true, description = "Date in UTC (yyyy-MM-dd)")
    private String dbUrl;

    public CookieFilterCommand(MostActiveCookieService mostActiveCookieService) {
        this.mostActiveCookieService = mostActiveCookieService;
    }

    @Override
    public Integer call() {
        final LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            System.err.println("Invalid date format. Expected: yyyy-MM-dd (UTC)");
            return 1;
        }

        try {
            List<String> mostActiveCookies = mostActiveCookieService.findMostActiveCookies(
                    logFile.toPath(),
                    targetDate
            );

            mostActiveCookies.forEach(System.out::println);
            return 0;
        } catch (NoSuchFileException ex) {
            System.err.println("Log file not found: " + logFile.getPath());
            return 1;
        } catch (DateTimeParseException ex) {
            System.err.println("Invalid timestamp in log file.");
            return 1;
        } catch (IOException ex) {
            System.err.println("Failed to read log file: " + ex.getMessage());
            return 1;
        } catch (RuntimeException ex) {
            System.err.println("Failed to process log file: " + ex.getMessage());
            return 1;
        }
    }
}
