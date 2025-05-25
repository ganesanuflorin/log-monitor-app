package com.log.monitor.application;

import com.log.monitor.application.entity.LogEntry;
import com.log.monitor.application.entity.Report;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogMonitorRunnerTest {

    @Test
    void testShortJobProducesInfoReport() {
        LogEntry start = new LogEntry("12:00:00,TestJob,START,123");
        LogEntry end = new LogEntry("12:01:00,TestJob,END,123");

        Report report = generateReport(start, end);

        assertEquals(InfoType.INFO, report.getType());
        assertTrue(report.toString().contains("took 60 seconds"));
    }

    @Test
    void testMediumJobProducesWarningReport() {
        LogEntry start = new LogEntry("12:00:00,TestJob,START,456");
        LogEntry end = new LogEntry("12:06:00,TestJob,END,456");

        Report report = generateReport(start, end);

        assertEquals(InfoType.WARNING, report.getType());
        assertTrue(report.toString().contains("Exceeded 5 minutes"));
    }

    @Test
    void testLongJobProducesErrorReport() {
        LogEntry start = new LogEntry("12:00:00,TestJob,START,789");
        LogEntry end = new LogEntry("12:11:00,TestJob,END,789");

        Report report = generateReport(start, end);

        assertEquals(InfoType.ERROR, report.getType());
        assertTrue(report.toString().contains("Exceeded 10 minutes"));
    }

    private Report generateReport(LogEntry start, LogEntry end) {
        long durationSeconds = java.time.Duration.between(start.getTimestamp(), end.getTimestamp()).getSeconds();
        String message = String.format("PID %s (%s) took %d seconds.", end.getPid(), end.getDescription(), durationSeconds);

        if (durationSeconds > 600) {
            return new Report(InfoType.ERROR, "Exceeded 10 minutes.", message);
        } else if (durationSeconds > 300) {
            return new Report(InfoType.WARNING, "Exceeded 5 minutes.", message);
        } else {
            return new Report(InfoType.INFO,"", message);
        }
    }
}
