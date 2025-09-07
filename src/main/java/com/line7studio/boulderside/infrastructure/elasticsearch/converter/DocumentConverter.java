package com.line7studio.boulderside.infrastructure.elasticsearch.converter;

import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.region.entity.Region;
import com.line7studio.boulderside.domain.aggregate.route.Route;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.BoulderDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.PostDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.RouteDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.enums.DocumentDomainType;
import org.springframework.stereotype.Component;

@Component
public class DocumentConverter {

    public BoulderDocument toBoulderDocument(Boulder boulder, Region region, String thumbnailUrl) {
        return BoulderDocument.builder()
                .id(boulder.getId().toString())
                .boulderName(boulder.getName())
                .documentDomainType(DocumentDomainType.BOULDER)
                .thumbnailUrl(thumbnailUrl)
                .province(region != null ? region.getProvince() : null)
                .city(region != null ? region.getCity() : null)
                .viewCount(boulder.getViewCount())
                .likeCount(boulder.getLikeCount())
                .createdAt(boulder.getCreatedAt())
                .build();
    }

    public RouteDocument toRouteDocument(Route route) {
        return RouteDocument.builder()
                .id(route.getId().toString())
                .routeName(route.getName())
                .documentDomainType(DocumentDomainType.ROUTE)
                .level(route.getRouteLevel())
                .likeCount(route.getLikeCount())
                .climberCount(route.getClimberCount())
                .viewCount(route.getViewCount())
                .commentCount(route.getCommentCount())
                .createdAt(route.getCreatedAt())
                .build();
    }

    public PostDocument toPostDocument(Post post, String authorName) {
        return PostDocument.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .documentDomainType(DocumentDomainType.POST)
                .meetingDate(post.getMeetingDate())
                .authorName(authorName)
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}