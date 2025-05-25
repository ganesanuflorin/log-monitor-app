package com.log.monitor.application.runner;

import com.log.monitor.application.entity.LogEntry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LogMonitorRunner implements CommandLineRunner {

    @Override
    public void run(String... args){
       String filePath = "src/main/resources/static/logs.log";

        Map<String, LogEntry> startMap = new HashMap<>();
        List<String> outputLogs = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                LogEntry entry = new LogEntry(line);

                if(entry.getStatus().equals("START")) {
                    startMap.put(entry.getPid(), entry);
                }
                if(entry.getStatus().equals("END")) {
                    LogEntry startEntry = startMap.get(entry.getPid());
                    if (startEntry == null) {
                        continue;
                    }
                    long durationSeconds = Duration.between(startEntry.getTimestamp(), entry.getTimestamp()).getSeconds();
                    StringBuilder logLine = new StringBuilder(String.format("PID %s (%s) took %d seconds.",
                            entry.getPid(), entry.getDescription(), durationSeconds));

                    if (durationSeconds > 600) {
                        logLine.append("Error: Exceeded 10 minuts");
                    } else if (durationSeconds > 300) {
                        logLine.append("Warning: Exceeded 5 minutes");
                    }

                    outputLogs.add(logLine.toString());
                    startMap.remove(entry.getPid());
                }
            }
            outputLogs.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
