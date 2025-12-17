package com.line7studio.boulderside.usecase.approach;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.common.dto.PointInfo;
import com.line7studio.boulderside.controller.approach.request.CreateApproachRequest;
import com.line7studio.boulderside.controller.approach.request.CreatePointRequest;
import com.line7studio.boulderside.controller.approach.request.UpdateApproachRequest;
import com.line7studio.boulderside.controller.approach.response.ApproachResponse;
import com.line7studio.boulderside.domain.feature.approach.Approach;
import com.line7studio.boulderside.domain.feature.approach.service.ApproachService;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.feature.image.service.ImageService;
import com.line7studio.boulderside.domain.feature.point.Point;
import com.line7studio.boulderside.domain.feature.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApproachUseCase {
    private final ApproachService approachService;
    private final PointService pointService;
    private final ImageService imageService;

    @Transactional
    public ApproachResponse createApproach(CreateApproachRequest request) {
        Approach approach = Approach.builder()
            .boulderId(request.boulderId())
            .orderIndex(request.orderIndex())
            .transportInfo(request.transportInfo())
            .parkingInfo(request.parkingInfo())
            .duration(request.duration())
            .tip(request.tip())
            .build();

        Approach savedApproach = approachService.save(approach);
        List<PointInfo> pointInfos = createPoints(savedApproach.getId(), request.points());
        return ApproachResponse.of(savedApproach, pointInfos);
    }

    @Transactional
    public ApproachResponse updateApproach(Long approachId, UpdateApproachRequest request) {
        Approach approach = approachService.getById(approachId);
        approach.update(
            request.boulderId(),
            request.orderIndex(),
            request.transportInfo(),
            request.parkingInfo(),
            request.duration(),
            request.tip()
        );

        List<Point> existingPoints = pointService.findByApproachIdOrderByOrderIndexAsc(approachId);
        deletePointResources(existingPoints);
        pointService.deleteByApproachId(approachId);

        List<PointInfo> pointInfos = createPoints(approachId, request.points());
        return ApproachResponse.of(approach, pointInfos);
    }

    @Transactional
    public void deleteApproach(Long approachId) {
        approachService.getById(approachId);
        List<Point> existingPoints = pointService.findByApproachIdOrderByOrderIndexAsc(approachId);
        deletePointResources(existingPoints);
        pointService.deleteByApproachId(approachId);
        approachService.deleteById(approachId);
    }

    public List<ApproachResponse> getApproachesByBoulderId(Long boulderId) {
        List<Approach> approachList = approachService.findByBoulderIdOrderByOrderIndexAsc(boulderId);
        return buildApproachResponses(approachList);
    }

    public List<ApproachResponse> getAllApproaches() {
        List<Approach> approachList = approachService.findAll();
        return buildApproachResponses(approachList);
    }

    private List<ApproachResponse> buildApproachResponses(List<Approach> approachList) {
        if (approachList.isEmpty()) {
            return List.of();
        }

        List<Long> approachIdList = approachList.stream().map(Approach::getId).toList();

        List<Point> pointList = pointService.findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(approachIdList);

        Map<Long, List<Point>> pointsByApproachId = pointList.stream()
            .collect(Collectors.groupingBy(Point::getApproachId));

        List<Long> pointIdList = pointList.stream().map(Point::getId).toList();
        Map<Long, List<ImageInfo>> imagesByPointId = pointIdList.isEmpty() ? Map.of() :
            imageService.getImageListByImageDomainTypeAndDomainIdList(ImageDomainType.POINT, pointIdList)
                .stream()
                .map(ImageInfo::from)
                .collect(Collectors.groupingBy(ImageInfo::getDomainId));

        return approachList.stream()
            .map(approach -> buildApproachResponseWithPreloadedData(approach, pointsByApproachId, imagesByPointId))
            .collect(Collectors.toList());
    }

    private ApproachResponse buildApproachResponseWithPreloadedData(
            Approach approach,
            Map<Long, List<Point>> pointsByApproachId,
            Map<Long, List<ImageInfo>> imagesByPointId) {
        
        List<Point> points = pointsByApproachId.getOrDefault(approach.getId(), List.of());
        
        List<PointInfo> pointInfos = points.stream()
                .map(point -> PointInfo.of(point, imagesByPointId.getOrDefault(point.getId(), List.of())))
                .collect(Collectors.toList());

        return ApproachResponse.of(approach, pointInfos);
    }
    private List<PointInfo> createPoints(Long approachId, List<CreatePointRequest> pointRequests) {
        if (pointRequests == null || pointRequests.isEmpty()) {
            return List.of();
        }

        List<Point> points = pointRequests.stream()
            .map(pointRequest -> Point.builder()
                .approachId(approachId)
                .orderIndex(pointRequest.orderIndex())
                .name(pointRequest.name())
                .description(pointRequest.description())
                .note(pointRequest.note())
                .build())
            .toList();

        List<Point> savedPoints = pointService.saveAll(points);
        return savedPoints.stream()
            .map(point -> PointInfo.of(point, List.of()))
            .toList();
    }

    private void deletePointResources(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return;
        }

        points.forEach(point ->
            imageService.deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType.POINT, point.getId())
        );
    }
}
