package com.java.lld.design.reader;

import com.java.lld.design.model.CookieLogEntry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


public interface CookieLogReader {

    List<CookieLogEntry> readEntries(Path logFile) throws IOException;

    CookieLogEntry parseLine(String line);
}
