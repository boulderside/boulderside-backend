package com.line7studio.boulderside.infrastructure.elasticsearch.config;

import com.line7studio.boulderside.infrastructure.elasticsearch.service.ElasticsearchIndexService;
import com.line7studio.boulderside.infrastructure.elasticsearch.service.ElasticsearchSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchDataLoader implements ApplicationRunner {
    
    private final ElasticsearchIndexService elasticsearchIndexService;
    private final ElasticsearchSyncService elasticsearchSyncService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            // Recreate indices first to ensure proper mapping
            elasticsearchIndexService.recreateIndices();
            
            // Then sync all data
            elasticsearchSyncService.syncAllDataOnStartup();
        } catch (Exception e) {
            log.error("Failed to initialize Elasticsearch on startup", e);
            // Don't throw exception to prevent application startup failure
            // Elasticsearch sync is not critical for basic application functionality
        }
    }
}