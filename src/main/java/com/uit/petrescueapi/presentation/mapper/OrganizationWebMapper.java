package com.uit.petrescueapi.presentation.mapper;

import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;
import com.uit.petrescueapi.domain.entity.Organization;
import com.uit.petrescueapi.domain.entity.OrganizationMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper: domain entities → presentation DTOs for Organization module.
 *
 * <ul>
 *   <li>{@link OrganizationResponseDto} — full detail / write response</li>
 *   <li>{@link OrganizationMemberResponseDto} — member write response (username not available on command path)</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface OrganizationWebMapper {

    /**
     * Maps domain Organization → response DTO.
     * {@code street_address} is exposed as {@code address} in the response for brevity.
     */
    @Mapping(target = "address", source = "street_address")
    @Mapping(target = "officialLink", source = "officialLink")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    OrganizationResponseDto toDto(Organization organization);

    /**
     * Maps domain OrganizationMember → response DTO.
     * {@code username} is not present in the domain entity (resolved only by JOIN on query path)
     * so it is ignored here and will be null in command responses.
     */
    @Mapping(target = "username", ignore = true)
    OrganizationMemberResponseDto toMemberDto(OrganizationMember member);
}
