package com.microboxlabs.service;

import com.microboxlabs.service.contract.to.LogTO;
import com.microboxlabs.service.contract.to.PaginatedTO;
import com.microboxlabs.service.contract.to.criteria.AdvanceCriteriaTO;
import com.microboxlabs.service.contract.to.criteria.CriteriaTO;

import java.io.InputStream;


public interface LogService {
    /**
     * Parse the uploaded file and save logs to the database.
     *
     * @param file {@link InputStream} The uploaded log file.
     */
    void parseAndSaveLogs(InputStream file);

    PaginatedTO<LogTO> findAll(CriteriaTO criteria);

    PaginatedTO<LogTO> findAll(AdvanceCriteriaTO criteria);
}
