package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.banner.CreateBannerRequestDto;
import com.uit.petrescueapi.application.dto.banner.UpdateBannerRequestDto;
import com.uit.petrescueapi.domain.entity.Banner;

import java.util.UUID;

/**
 * Command (write) port for Banner operations.
 */
public interface BannerCommandPort {

    Banner create(CreateBannerRequestDto cmd);

    Banner update(UUID bannerId, UpdateBannerRequestDto cmd);

    Banner toggleActive(UUID bannerId);

    Banner updateDisplayOrder(UUID bannerId, Integer displayOrder);

    void delete(UUID bannerId);
}
