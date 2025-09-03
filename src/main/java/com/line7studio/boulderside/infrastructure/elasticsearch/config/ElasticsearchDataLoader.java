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
    public void run(ApplicationArguments args) {
        try {
            elasticsearchIndexService.recreateIndices();
            elasticsearchSyncService.syncAllDataOnStartup();
        } catch (Exception e) {
            log.error("엘라스틱 서치를 위한 데이터 동기화 실패", e);
        }
    }
}