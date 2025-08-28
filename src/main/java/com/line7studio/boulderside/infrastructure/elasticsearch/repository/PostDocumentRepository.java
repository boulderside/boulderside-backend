package com.line7studio.boulderside.infrastructure.elasticsearch.repository;

import com.line7studio.boulderside.infrastructure.elasticsearch.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, String> {
}