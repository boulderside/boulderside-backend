package com.line7studio.boulderside.infrastructure.elasticsearch.repository;

import com.line7studio.boulderside.infrastructure.elasticsearch.document.BoulderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BoulderDocumentRepository extends ElasticsearchRepository<BoulderDocument, String> {
}