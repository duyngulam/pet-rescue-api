package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.AuditLog;
import com.uit.petrescueapi.domain.valueobject.AuditAction;
import com.uit.petrescueapi.infrastructure.persistence.entity.AuditLogJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditLogEntityMapper {

    @Mapping(target = "action", source = "action", qualifiedByName = "stringToAction")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "offsetToLocal")
    AuditLog toDomain(AuditLogJpaEntity entity);

    @Mapping(target = "action", source = "action", qualifiedByName = "actionToString")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localToOffset")
    AuditLogJpaEntity toEntity(AuditLog domain);

    List<AuditLog> toDomainList(List<AuditLogJpaEntity> entities);

    @Named("stringToAction")
    default AuditAction stringToAction(String s) {
        return s == null ? null : AuditAction.valueOf(s);
    }

    @Named("actionToString")
    default String actionToString(AuditAction a) {
        return a == null ? null : a.name();
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
