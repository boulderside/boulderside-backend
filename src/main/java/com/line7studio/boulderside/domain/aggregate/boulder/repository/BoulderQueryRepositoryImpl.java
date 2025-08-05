package com.line7studio.boulderside.domain.aggregate.boulder.repository;

import static com.line7studio.boulderside.domain.aggregate.boulder.entity.QBoulder.*;
import static com.line7studio.boulderside.domain.aggregate.region.entity.QRegion.*;
import static com.line7studio.boulderside.domain.association.like.entity.QUserBoulderLike.*;

import java.util.List;

import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;
import com.line7studio.boulderside.domain.association.like.entity.QUserBoulderLike;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoulderQueryRepositoryImpl implements BoulderQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<BoulderWithRegion> findBouldersWithRegionAndCursor(BoulderSortType sortType, Long cursor, Long cursorLikeCount, int size) {
		JPQLQuery<BoulderWithRegion> query =  jpaQueryFactory
				.select(Projections.constructor(
						BoulderWithRegion.class,
						// Boulder
						boulder.id,
						boulder.name,
						boulder.description,
						boulder.latitude,
						boulder.longitude,
						boulder.createdAt,
						boulder.updatedAt,
						// Region
						region.id,
						region.province,
						region.city,
						region.regionCode,
						region.officialDistrictCode
				))
				.from(boulder)
				.join(region).on(boulder.regionId.eq(region.id))
				.leftJoin(userBoulderLike).on(userBoulderLike.boulderId.eq(boulder.id))
				.groupBy(
						boulder.id, boulder.name, boulder.description,
						boulder.latitude, boulder.longitude,
						boulder.createdAt, boulder.updatedAt,
						region.id, region.province, region.city,
						region.regionCode, region.officialDistrictCode
				);

		// 정렬 및 커서 조건 처리
		if (sortType == BoulderSortType.POPULAR) {
			query.orderBy(userBoulderLike.count().desc(), boulder.id.desc());

			if (cursorLikeCount != null && cursor != null) {
				query.having(
						userBoulderLike.count().lt(cursorLikeCount)
								.or(userBoulderLike.count().eq(cursorLikeCount).and(boulder.id.lt(cursor)))
				);
			}
		} else if (sortType == BoulderSortType.LATEST) {
			query.orderBy(boulder.id.desc());

			if (cursor != null) {
				query.where(boulder.id.lt(cursor));
			}
		}

		return query
				.limit(size + 1) // 다음 페이지 유무 확인용
				.fetch();

	}

	@Override
	public BoulderWithRegion findBouldersWithRegionByBoulderId(Long boulderId) {
		return jpaQueryFactory
			.select(Projections.constructor(
				BoulderWithRegion.class,
				// Boulder
				boulder.id,
				boulder.name,
				boulder.description,
				boulder.latitude,
				boulder.longitude,
				boulder.createdAt,
				boulder.updatedAt,
				// Region
				region.id,
				region.province,
				region.city,
				region.regionCode,
				region.officialDistrictCode
			))
			.from(boulder)
			.join(region).on(boulder.regionId.eq(region.id))
			.where(boulder.id.eq(boulderId))
			.fetchOne();
	}
}