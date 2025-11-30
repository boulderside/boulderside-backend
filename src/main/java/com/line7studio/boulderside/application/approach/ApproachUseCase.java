package com.line7studio.boulderside.application.approach;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.common.dto.PointInfo;
import com.line7studio.boulderside.controller.approach.request.CreateApproachRequest;
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
                .boulderId(request.getBoulderId())
                .orderIndex(request.getOrderIndex())
                .transportInfo(request.getTransportInfo())
                .parkingInfo(request.getParkingInfo())
                .duration(request.getDuration())
                .tip(request.getTip())
                .build();
        
        Approach savedApproach = approachService.save(approach);

        List<PointInfo> pointInfos = List.of();
        if (request.getPoints() != null && !request.getPoints().isEmpty()) {
            List<Point> points = request.getPoints().stream()
                    .map(pointRequest -> Point.builder()
                            .approachId(savedApproach.getId())
                            .orderIndex(pointRequest.getOrderIndex())
                            .name(pointRequest.getName())
                            .description(pointRequest.getDescription())
                            .note(pointRequest.getNote())
                            .build())
                    .toList();
            
            List<Point> savedPoints = pointService.saveAll(points);
            pointInfos = savedPoints.stream()
                    .map(point -> PointInfo.of(point, List.of()))
                    .toList();
        }

        return ApproachResponse.of(savedApproach, pointInfos);
    }

    public List<ApproachResponse> getApproachesByBoulderId(Long boulderId) {
        List<Approach> approacheList = approachService.findByBoulderIdOrderByOrderIndexAsc(boulderId);

        if (approacheList.isEmpty()) {
            return List.of();
        }

        List<Long> approachIdList = approacheList.stream().map(Approach::getId).toList();

        List<Point> pointList = pointService.findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(approachIdList);

        Map<Long, List<Point>> pointsByApproachId = pointList.stream()
                .collect(Collectors.groupingBy(Point::getApproachId));

        List<Long> pointIdList = pointList.stream().map(Point::getId).toList();
        Map<Long, List<ImageInfo>> imagesByPointId = pointIdList.isEmpty() ? Map.of() :
                imageService.getImageListByImageDomainTypeAndDomainIdList(ImageDomainType.POINT, pointIdList)
                        .stream()
                        .map(ImageInfo::from)
                        .collect(Collectors.groupingBy(ImageInfo::getDomainId));

        return approacheList.stream()
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
}