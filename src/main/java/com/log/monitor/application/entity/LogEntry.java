package com.log.monitor.application.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class LogEntry {
    private LocalTime timestamp;
    private String description;
    private String status;
    private String pid;

    public LogEntry(String line) {
        String[] parts = line.split(",");
        this.timestamp =  LocalTime.parse(parts[0].trim(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.description = parts[1].trim();
        this.status = parts[2].trim();
        this.pid = parts[3].trim();
    }
}
