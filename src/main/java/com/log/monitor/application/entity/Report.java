package com.log.monitor.application.entity;

import com.log.monitor.application.InfoType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Report {
    private final InfoType type;
    private final String message;
    private final String description;

    @Override
    public String toString() {
        return String.format("[%s] %s %s", type, message, description);
    }
}
