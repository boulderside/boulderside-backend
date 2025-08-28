package com.line7studio.boulderside.infrastructure.elasticsearch.service;

import com.line7studio.boulderside.infrastructure.elasticsearch.document.BoulderDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.PostDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.RouteDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchIndexService {
    
    private final ElasticsearchOperations elasticsearchOperations;

    public void recreateIndices() {
        recreateIndex(BoulderDocument.class, "boulders");
        recreateIndex(RouteDocument.class, "routes");
        recreateIndex(PostDocument.class, "posts");
    }

    private void recreateIndex(Class<?> documentClass, String indexName) {
        try {
            IndexOperations indexOps = elasticsearchOperations.indexOps(documentClass);
            
            if (indexOps.exists()) {
                indexOps.delete();
            }

            Document settings = Document.create()
                .append("analysis", Document.create()
                    .append("analyzer", Document.create()
                        .append("korean_autocomplete", Document.create()
                            .append("type", "custom")
                            .append("tokenizer", "nori_tokenizer")
                            .append("filter", new String[]{"lowercase", "nori_part_of_speech", "korean_autocomplete_edge_ngram"}))
                        .append("korean_search", Document.create()
                            .append("type", "custom")
                            .append("tokenizer", "nori_tokenizer")
                            .append("filter", new String[]{"lowercase", "nori_part_of_speech"})))
                    .append("filter", Document.create()
                        .append("korean_autocomplete_edge_ngram", Document.create()
                            .append("type", "edge_ngram")
                            .append("min_gram", 1)
                            .append("max_gram", 20)))
                    .append("tokenizer", Document.create()
                        .append("nori_tokenizer", Document.create()
                            .append("type", "nori_tokenizer")
                            .append("decompound_mode", "mixed"))));
            
            indexOps.create(settings);
            indexOps.putMapping();
            
            log.info("Index {} created successfully", indexName);
        } catch (Exception e) {
            log.error("Failed to recreate index: {}", indexName, e);
        }
    }
}