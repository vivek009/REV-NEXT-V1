package com.revnext.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchIndexInitializer implements ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Creating GIN indexes for full-text search...");
        createIndex("idx_product_fts", "PRODUCT",
                "to_tsvector('english', coalesce(name,'') || ' ' || coalesce(description,'') || ' ' || coalesce(sku,''))");
        createIndex("idx_suite_fts", "suite",
                "to_tsvector('english', coalesce(name,'') || ' ' || coalesce(description,'') || ' ' || coalesce(remark,''))");
        createIndex("idx_customer_fts", "CUSTOMER",
                "to_tsvector('english', coalesce(name,'') || ' ' || coalesce(description,'') || ' ' || coalesce(address,''))");
        createIndex("idx_discount_fts", "DISCOUNT",
                "to_tsvector('english', coalesce(name,'') || ' ' || coalesce(discount_rule,''))");
        log.info("GIN indexes created successfully.");
    }

    private void createIndex(String indexName, String tableName, String expression) {
        try {
            String sql = String.format("CREATE INDEX IF NOT EXISTS %s ON %s USING GIN (%s)", indexName, tableName,
                    expression);
            jdbcTemplate.execute(sql);
            log.debug("Index {} on {} created/verified.", indexName, tableName);
        } catch (Exception e) {
            log.warn("Could not create index {} on {}: {}", indexName, tableName, e.getMessage());
        }
    }
}
