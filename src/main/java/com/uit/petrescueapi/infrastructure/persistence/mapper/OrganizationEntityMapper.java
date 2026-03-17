package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.Organization;
import com.uit.petrescueapi.infrastructure.persistence.entity.OrganizationJpaEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationEntityMapper {

    GeometryFactory GF = new GeometryFactory(new PrecisionModel(), 4326);

    @Mapping(target = "latitude", source = "location", qualifiedByName = "pointToLat")
    @Mapping(target = "longitude", source = "location", qualifiedByName = "pointToLng")
    Organization toDomain(OrganizationJpaEntity entity);

    @Mapping(target = "location", source = ".", qualifiedByName = "latLngToPoint")
    OrganizationJpaEntity toEntity(Organization domain);

    List<Organization> toDomainList(List<OrganizationJpaEntity> entities);

    @Named("pointToLat")
    default Double pointToLat(Point point) {
        return point == null ? null : point.getY();
    }

    @Named("pointToLng")
    default Double pointToLng(Point point) {
        return point == null ? null : point.getX();
    }

    @Named("latLngToPoint")
    default Point latLngToPoint(Organization org) {
        if (org.getLatitude() == null || org.getLongitude() == null) return null;
        return GF.createPoint(new Coordinate(org.getLongitude(), org.getLatitude()));
    }
}
