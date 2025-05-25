# Log Monitoring Application

This Spring Boot application processes a log file (`logs.log`) and reports the duration of each job, classifying them based on how long they take to complete.

## ✅ Features

* Parses a CSV log file containing job START and END entries.
* Calculates job duration based on timestamps.
* Categorizes each job duration as:

    * `INFO`: ≤ 5 minutes
    * `WARNING`: > 5 and ≤ 10 minutes
    * `ERROR`: > 10 minutes
* Outputs only `WARNING` and `ERROR` reports.


## 🚀 Getting Started

### Prerequisites

* Java 21
* Maven 

