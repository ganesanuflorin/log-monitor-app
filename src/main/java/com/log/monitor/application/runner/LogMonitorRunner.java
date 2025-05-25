package com.log.monitor.application.runner;

import com.log.monitor.application.InfoType;
import com.log.monitor.application.entity.LogEntry;
import com.log.monitor.application.entity.Report;
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

// add component because Spring won't detect the class and won't run the run() method
@Component
public class LogMonitorRunner implements CommandLineRunner {

    @Override
    public void run(String... args){
       String filePath = "src/main/resources/static/logs.log";

        Map<String, LogEntry> startMap = new HashMap<>();
        List<Report> reports = new ArrayList<>();

        // read from file
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                LogEntry entry = new LogEntry(line);

                // put the String "START" before to avoid the null pointer exceptions
                if("START".equals(entry.getStatus())) {
                    startMap.put(entry.getPid(), entry);
                }
                if("END".equals(entry.getStatus())) {
                    LogEntry startEntry = startMap.get(entry.getPid());
                    if (startEntry == null) {
                        continue;
                    }
                    // take the durations between start and end
                    long durationSeconds = Duration.between(startEntry.getTimestamp(), entry.getTimestamp()).getSeconds();
                    String description = String.format("PID %s (%s) took %d seconds.",
                            entry.getPid(), entry.getDescription(), durationSeconds);

                    // put the condition with 600 first because if I use 300 first and a job took 700 seconds
                    // then both conditions are true
                    if (durationSeconds > 600) {
                        reports.add(new Report(InfoType.ERROR, "Exceeded 10 minutes", description));
                    } else if (durationSeconds > 300) {
                        reports.add(new Report(InfoType.WARNING, "Exceeded 5 minutes", description));
                    } else {
                        reports.add(new Report(InfoType.INFO, "", description));
                    }

                    startMap.remove(entry.getPid());
                }
            }
            // filter the output to shown only the errors and warnings
            reports.stream()
                    .filter(r -> r.getType() != InfoType.INFO)
                    .forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
