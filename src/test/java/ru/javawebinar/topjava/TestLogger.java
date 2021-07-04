package ru.javawebinar.topjava;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class TestLogger {
    static final Logger log = getLogger("result");

    private static final StringBuilder results = new StringBuilder();

    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public static final Stopwatch INDIVIDUAL = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-30s %-25s %7d", description.getTestClass().getSimpleName(),
                    description.getMethodName(),
                    TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    public static final ExternalResource SUMMARY = new ExternalResource() {
        @Override
        protected void before() {
            results.setLength(0);
        }

        @Override
        protected void after() {
            log.info("\n----------------------------------------------------------------" +
                    "\nClass                          Test                 Duration, ms" +
                    "\n----------------------------------------------------------------" +
                    results +
                    "\n----------------------------------------------------------------");
        }
    };

    private TestLogger() {
    }
}
