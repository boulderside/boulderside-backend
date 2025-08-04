package com.line7studio.boulderside.domain.aggregate.boulder.repository;

import static com.line7studio.boulderside.domain.aggregate.boulder.entity.QBoulder.*;
import static com.line7studio.boulderside.domain.aggregate.region.entity.QRegion.*;
import static com.line7studio.boulderside.domain.association.like.entity.QUserBoulderLike.*;

import java.util.List;

import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;
import com.line7studio.boulderside.domain.association.like.entity.QUserBoulderLike;
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
	public List<BoulderWithRegion> findBouldersWithRegionAndCursor(BoulderSortType sortType, Long cursor, int size) {
		BooleanExpression predicate = null;

		if (cursor != null && sortType == BoulderSortType.LATEST) {
			predicate = boulder.id.lt(cursor);
		}

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
				.leftJoin(userBoulderLike).on(userBoulderLike.boulderId.eq(boulder.id))
				.where(predicate)
				.groupBy(
						boulder.id, boulder.name, boulder.description,
						boulder.latitude, boulder.longitude,
						boulder.createdAt, boulder.updatedAt,
						region.id, region.province, region.city,
						region.regionCode, region.officialDistrictCode
				)
				.orderBy(
						sortType == BoulderSortType.POPULAR
								? userBoulderLike.count().desc()
								: boulder.id.desc()
				)
				.limit(size + 1)
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