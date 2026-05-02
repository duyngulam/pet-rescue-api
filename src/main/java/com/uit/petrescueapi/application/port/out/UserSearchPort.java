package com.uit.petrescueapi.application.port.out;

import com.uit.petrescueapi.application.dto.user.UserSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Output port for pluggable user search providers.
 */
public interface UserSearchPort {
    Page<UserSummaryResponseDto> searchUsers(String searchName, Pageable pageable);
}
