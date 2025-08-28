package com.line7studio.boulderside.infrastructure.elasticsearch.repository;

import com.line7studio.boulderside.infrastructure.elasticsearch.document.RouteDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RouteDocumentRepository extends ElasticsearchRepository<RouteDocument, String> {
}