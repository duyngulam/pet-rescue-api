package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.RescueCase;
import com.uit.petrescueapi.domain.valueobject.RescueCaseStatus;
import com.uit.petrescueapi.infrastructure.persistence.entity.RescueCaseJpaEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RescueCaseEntityMapper {

    GeometryFactory GF = new GeometryFactory(new PrecisionModel(), 4326);

    @Mapping(target = "latitude", source = "location", qualifiedByName = "pointToLat")
    @Mapping(target = "longitude", source = "location", qualifiedByName = "pointToLng")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    @Mapping(target = "reportedAt", source = "reportedAt", qualifiedByName = "offsetToLocal")
    @Mapping(target = "resolvedAt", source = "resolvedAt", qualifiedByName = "offsetToLocal")
    RescueCase toDomain(RescueCaseJpaEntity entity);

    @Mapping(target = "location", source = ".", qualifiedByName = "latLngToPoint")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "reportedAt", source = "reportedAt", qualifiedByName = "localToOffset")
    @Mapping(target = "resolvedAt", source = "resolvedAt", qualifiedByName = "localToOffset")
    @Mapping(target = "reporter", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "pet", ignore = true)
    RescueCaseJpaEntity toEntity(RescueCase domain);

    List<RescueCase> toDomainList(List<RescueCaseJpaEntity> entities);

    @Named("pointToLat")
    default Double pointToLat(Point point) {
        return point == null ? null : point.getY();
    }

    @Named("pointToLng")
    default Double pointToLng(Point point) {
        return point == null ? null : point.getX();
    }

    @Named("latLngToPoint")
    default Point latLngToPoint(RescueCase rc) {
        if (rc.getLatitude() == null || rc.getLongitude() == null) return null;
        return GF.createPoint(new Coordinate(rc.getLongitude(), rc.getLatitude()));
    }

    @Named("stringToStatus")
    default RescueCaseStatus stringToStatus(String s) {
        return s == null ? null : RescueCaseStatus.valueOf(s);
    }

    @Named("statusToString")
    default String statusToString(RescueCaseStatus s) {
        return s == null ? null : s.name();
    }

    @Named("offsetToLocal")
    default LocalDateTime offsetToLocal(OffsetDateTime odt) {
        return odt == null ? null : odt.toLocalDateTime();
    }

    @Named("localToOffset")
    default OffsetDateTime localToOffset(LocalDateTime ldt) {
        return ldt == null ? null : ldt.atOffset(ZoneOffset.UTC);
    }
}
