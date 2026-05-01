package com.uit.petrescueapi.domain.repository;

public interface VisualCodeRepository {

    String nextUserCode();

    String nextOrganizationCode();

    String nextPetCode();

    String nextRescueCaseCode();

    String nextAdoptionCode();
}
