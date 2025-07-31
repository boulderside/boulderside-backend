package com.example.boulderside.domain.boulder.repository;

import static com.example.boulderside.domain.boulder.entity.QBoulder.*;
import static com.example.boulderside.domain.region.entity.QRegion.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.boulderside.application.boulder.dto.BoulderWithRegion;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoulderQueryRepositoryImpl implements BoulderQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<BoulderWithRegion> findBouldersWithRegionAndCursor(Long cursor, int size) {
		BooleanExpression predicate = (cursor != null)
			? boulder.id.gt(cursor)
			: null;

		return jpaQueryFactory
			.select(Projections.constructor(
				BoulderWithRegion.class,
				// Boulder
				boulder.id,
				boulder.name,
				boulder.description,
				boulder.latitude,
				boulder.longitude,
				boulder.likeCount,
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
			.where(predicate)
			.orderBy(boulder.id.asc())
			.limit(size + 1)
			.fetch();
	}

	@Override
	public BoulderWithRegion findBouldersWithRegionById(Long boulderId) {
		return jpaQueryFactory
			.select(Projections.constructor(
				BoulderWithRegion.class,
				// Boulder
				boulder.id,
				boulder.name,
				boulder.description,
				boulder.latitude,
				boulder.longitude,
				boulder.likeCount,
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
			.fetchOne();
	}
}