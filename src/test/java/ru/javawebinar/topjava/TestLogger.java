package ru.javawebinar.topjava;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestLogger {
    private static final Logger log = LoggerFactory.getLogger("duration");

    private static final String TEST_LOG_STRING = "Test %s finished, spent %d milliseconds";

    private static final String CLASS_LOG_STRING = "Test %s spent %d milliseconds";

    private static final String DELIMITER = "====================================================================";

    private static final Map<Description, Long> testsData = new HashMap<>();

    public static final Stopwatch INDIVIDUAL = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String output = '\n' +
                    DELIMITER +
                    '\n' +
                    logInfo(description, nanos, TEST_LOG_STRING) +
                    '\n' +
                    DELIMITER;
            log.info(output);
            testsData.put(description, nanos);
        }
    };

    public static final ExternalResource SUMMARY = new ExternalResource() {
        @Override
        protected void after() {
            StringBuilder output = new StringBuilder()
                    .append('\n')
                    .append(DELIMITER)
                    .append('\n');
            testsData.forEach((testName, duration) -> output.append(logInfo(testName, duration, CLASS_LOG_STRING)).append('\n'));
            output.append(DELIMITER);
            log.info(output.toString());
        }
    };

    private TestLogger() {
    }

    private static String logInfo(Description description, long nanos, String logFormat) {
        return String.format(logFormat,
                description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
    }
}
