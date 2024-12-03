package com.microboxlabs.service.impl;

import com.microboxlabs.service.LogService;
import com.microboxlabs.service.contract.to.LogTO;
import com.microboxlabs.service.contract.to.PaginatedTO;
import com.microboxlabs.service.contract.to.criteria.AdvanceCriteriaTO;
import com.microboxlabs.service.contract.to.criteria.CriteriaTO;
import com.microboxlabs.service.datasource.LogRepository;
import com.microboxlabs.service.datasource.domain.Log;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static com.microboxlabs.service.contract.LogBinder.LOG_BINDER;

@ApplicationScoped
public class LogServiceImpl implements LogService {
    private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    private static final String LOG_REGEX = "\\[(.*?)\\] \\[(INFO|ERROR|WARNING)\\] ([A-Za-z0-9\\-]+): (.*)";
    private final LogRepository logRepository;
    private final int batchSize;

    public LogServiceImpl(
            LogRepository logRepository,
            @ConfigProperty(name = "application.logs.batch.adjust") int batchSize
    ) {
        this.logRepository = logRepository;
        this.batchSize = batchSize;
    }

    @Override
    public void parseAndSaveLogs(InputStream file) {
        final var lines = readFileLogLines(file);
        final var logs = this.parseLogs(lines);
        this.performBatchProcessor(logs);
    }

    @Override
    public PaginatedTO<LogTO> findAll(CriteriaTO criteria) {
        final var query = Log
                .findAll(Sort.by("timestamp").descending())
                .page(Page.of(criteria.getPage(), criteria.getSize()));
        return LOG_BINDER.bindPaginated(query);
    }

    @Override
    public PaginatedTO<LogTO> findAll(AdvanceCriteriaTO criteria) {
        if (Objects.isNull(criteria.getFields()) || criteria.getFields().isEmpty())
            return this.findAll(new CriteriaTO().withPage(criteria.getPage()).withSize(criteria.getSize()));
        final var filter = LOG_BINDER.bind(criteria);
        return LOG_BINDER.bindPaginated(filter.query());
    }


    private void performBatchProcessor(List<Log> logs) {
        final var batches = this.createBatches(logs, this.batchSize);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            batches.forEach(batch -> executor.submit(() -> {
                this.persistLogs(batch);
            }));
        }
    }

    @Transactional
    protected void persistLogs(List<Log> batch) {
        try {
            logRepository.persist(batch);
        } catch (Exception e) {
            logger.error("Error persist log batch {}", e.getMessage(), e);
        }
    }

    /**
     * Read all lines of given log files and create a list of lines.
     * This method read the log file in batch using a virtual thread implementation for performance improves.
     * The batchSize property can configure to adjust based on available memory and performance needs
     *
     * @param file {@link InputStream} the file to be processed
     * @return List of string that represent each log line processed
     */
    private List<String> readFileLogLines(InputStream file) {
        var lines = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            final var allLines = br.lines().toList();
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                final var batches = this.createBatches(allLines, this.batchSize);
                batches.forEach(batch -> executor.submit(() -> {
                    logger.info("Processing batch of size: {}", batch.size());
                    batch.forEach(line -> {
                        synchronized (lines) {
                            lines.add(line);
                        }
                    });
                }));
            } catch (Exception e) {
                logger.error("Error during batch processing", e);
            }
            return lines;
        } catch (IOException e) {
            logger.error("Error reading log file {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private <T> List<List<T>> createBatches(List<T> lines, int batchSize) {
        return IntStream
                .range(0, (lines.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> lines.subList(i * batchSize, Math.min((i + 1) * batchSize, lines.size())))
                .toList();
    }

    private List<Log> parseLogs(List<String> logLines) {
        final var pattern = Pattern.compile(LOG_REGEX);
        return logLines
                .stream()
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(LOG_BINDER::bindLog)
                .toList();
    }
}
