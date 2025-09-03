package com.line7studio.boulderside.infrastructure.elasticsearch.service;

import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.route.Route;

public interface ElasticsearchSyncService {
    void syncAllDataOnStartup();
    void syncBoulder(Boulder boulder);
    void syncRoute(Route route);
    void syncPost(Post post);
    void deleteBoulder(Long boulderId);
    void deleteRoute(Long routeId);
    void deletePost(Long postId);
}