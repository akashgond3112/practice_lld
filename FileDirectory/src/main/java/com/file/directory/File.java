package com.file.directory;

public class File extends Entry {
    private String content;
    private final String format;

    public File(String name, Double size, String format) {
        super.setName(name);
        super.setSize(size);
        this.format=format;
    }

    public String getContent() {
        return content;
    }

    public String getFormat() {
        return format;
    }

}
