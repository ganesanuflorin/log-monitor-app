package com.log.monitor.application.entity;

import java.time.LocalTime;

public class LogEntry {
    private LocalTime timestamp;
    private String description;
    private String status;
    private String pid;

    public LogEntry() {
    }
}
