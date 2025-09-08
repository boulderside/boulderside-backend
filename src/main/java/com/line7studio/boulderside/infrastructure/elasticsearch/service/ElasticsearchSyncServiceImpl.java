package com.line7studio.boulderside.infrastructure.elasticsearch.service;

import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.repository.BoulderRepository;
import com.line7studio.boulderside.domain.aggregate.comment.service.CommentService;
import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.aggregate.image.service.ImageService;
import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.post.repository.PostRepository;
import com.line7studio.boulderside.domain.aggregate.region.entity.Region;
import com.line7studio.boulderside.domain.aggregate.region.repository.RegionRepository;
import com.line7studio.boulderside.domain.aggregate.route.Route;
import com.line7studio.boulderside.domain.aggregate.route.repository.RouteRepository;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.repository.UserRepository;
import com.line7studio.boulderside.infrastructure.elasticsearch.converter.DocumentConverter;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.BoulderDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.PostDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.RouteDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.repository.BoulderDocumentRepository;
import com.line7studio.boulderside.infrastructure.elasticsearch.repository.PostDocumentRepository;
import com.line7studio.boulderside.infrastructure.elasticsearch.repository.RouteDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchSyncServiceImpl implements ElasticsearchSyncService {
    
    private final BoulderRepository boulderRepository;
    private final RouteRepository routeRepository;
    private final PostRepository postRepository;
    private final RegionRepository regionRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final CommentService commentService;
    
    private final BoulderDocumentRepository boulderDocumentRepository;
    private final RouteDocumentRepository routeDocumentRepository;
    private final PostDocumentRepository postDocumentRepository;
    
    private final DocumentConverter documentConverter;

    @Override
    @Transactional(readOnly = true)
    public void syncAllDataOnStartup() {
        log.info("Elasticsearch 데이터 동기화 시작");

        syncAllBoulders();
        syncAllRoutes();
        syncAllPosts();

        log.info("Elasticsearch 데이터 동기화 완료");
    }

    private void syncAllBoulders() {
        log.info("Syncing all boulders to Elasticsearch...");
        List<Boulder> boulders = boulderRepository.findAll();
        
        for (Boulder boulder : boulders) {
            try {
                Region region = regionRepository.findById(boulder.getRegionId()).orElse(null);

                String thumbnailUrl = imageService.getImageListByImageDomainTypeAndDomainIdAndOrderIndex(ImageDomainType.BOULDER, boulder.getId(), 0)
                        .stream()
                        .findFirst()
                        .map(Image::getImageUrl)
                        .orElse(null);
                
                BoulderDocument document = documentConverter.toBoulderDocument(boulder, region, thumbnailUrl);
                boulderDocumentRepository.save(document);
            } catch (Exception e) {
                log.error("Failed to sync boulder with ID: {}", boulder.getId(), e);
            }
        }
        log.info("Synced {} boulders to Elasticsearch", boulders.size());
    }

    private void syncAllRoutes() {
        log.info("Syncing all routes to Elasticsearch...");
        List<Route> routes = routeRepository.findAll();
        
        for (Route route : routes) {
            try {
                RouteDocument document = documentConverter.toRouteDocument(route);
                routeDocumentRepository.save(document);
            } catch (Exception e) {
                log.error("Failed to sync route with ID: {}", route.getId(), e);
            }
        }
        log.info("Synced {} routes to Elasticsearch", routes.size());
    }

    private void syncAllPosts() {
        log.info("Syncing all posts to Elasticsearch...");
        List<Post> posts = postRepository.findAll();
        
        for (Post post : posts) {
            try {
                String authorName = getUserName(post.getUserId());
                PostDocument document = documentConverter.toPostDocument(post, authorName);
                postDocumentRepository.save(document);
            } catch (Exception e) {
                log.error("Failed to sync post with ID: {}", post.getId(), e);
            }
        }
        log.info("Synced {} posts to Elasticsearch", posts.size());
    }

    @Override
    public void syncBoulder(Boulder boulder) {
        try {
            Region region = regionRepository.findById(boulder.getRegionId()).orElse(null);

            String thumbnailUrl = imageService.getImageListByImageDomainTypeAndDomainIdAndOrderIndex(ImageDomainType.BOULDER, boulder.getId(), 0)
                    .stream()
                    .findFirst()
                    .map(Image::getImageUrl)
                    .orElse(null);
            
            BoulderDocument document = documentConverter.toBoulderDocument(boulder, region, thumbnailUrl);
            boulderDocumentRepository.save(document);
            log.debug("Synced boulder to Elasticsearch: {}", boulder.getId());
        } catch (Exception e) {
            log.error("Failed to sync boulder to Elasticsearch: {}", boulder.getId(), e);
        }
    }

    @Override
    public void syncRoute(Route route) {
        try {
            RouteDocument document = documentConverter.toRouteDocument(route);
            routeDocumentRepository.save(document);
            log.debug("Synced route to Elasticsearch: {}", route.getId());
        } catch (Exception e) {
            log.error("Failed to sync route to Elasticsearch: {}", route.getId(), e);
        }
    }

    @Override
    public void syncPost(Post post) {
        try {
            String authorName = getUserName(post.getUserId());
            PostDocument document = documentConverter.toPostDocument(post, authorName);
            postDocumentRepository.save(document);
            log.debug("Synced post to Elasticsearch: {}", post.getId());
        } catch (Exception e) {
            log.error("Failed to sync post to Elasticsearch: {}", post.getId(), e);
        }
    }

    @Override
    public void deleteBoulder(Long boulderId) {
        try {
            boulderDocumentRepository.deleteById(boulderId.toString());
            log.debug("Deleted boulder from Elasticsearch: {}", boulderId);
        } catch (Exception e) {
            log.error("Failed to delete boulder from Elasticsearch: {}", boulderId, e);
        }
    }

    @Override
    public void deleteRoute(Long routeId) {
        try {
            routeDocumentRepository.deleteById(routeId.toString());
            log.debug("Deleted route from Elasticsearch: {}", routeId);
        } catch (Exception e) {
            log.error("Failed to delete route from Elasticsearch: {}", routeId, e);
        }
    }

    @Override
    public void deletePost(Long postId) {
        try {
            postDocumentRepository.deleteById(postId.toString());
            log.debug("Deleted post from Elasticsearch: {}", postId);
        } catch (Exception e) {
            log.error("Failed to delete post from Elasticsearch: {}", postId, e);
        }
    }

    private String getUserName(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getNickname).orElse("Unknown");
    }
}