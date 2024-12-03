CREATE TABLE  log (
                            created_at timestamp(6) DEFAULT CURRENT_TIMESTAMP NULL,
                            id int8 PRIMARY KEY,
                            "timestamp" timestamp(6) NOT NULL,
                            log_level varchar(255) NOT NULL,
                            message varchar(255) NOT NULL,
                            service_name varchar(255) NOT NULL
);

CREATE INDEX idx_timestamp ON log (timestamp);
CREATE INDEX idx_log_level ON log (log_level);
CREATE INDEX idx_service_name ON log (service_name);