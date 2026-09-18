package com.java.lld.design.reader;

import com.java.lld.design.model.CookieLogEntry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CookieLogReaderSql implements CookieLogReader{


    @Override
    public List<CookieLogEntry> readEntries(Path logFile) throws IOException {
        return List.of();
    }

    @Override
    public CookieLogEntry parseLine(String line) {
        return null;
    }
}
