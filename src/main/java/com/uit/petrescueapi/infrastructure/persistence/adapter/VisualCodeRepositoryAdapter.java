package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.repository.VisualCodeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisualCodeRepositoryAdapter implements VisualCodeRepository {

    private static final String USER_SEQUENCE = "user_visual_code_seq";
    private static final String ORG_SEQUENCE = "org_visual_code_seq";
    private static final String PET_SEQUENCE = "pet_visual_code_seq";
    private static final String RESCUE_CASE_SEQUENCE = "rescue_case_visual_code_seq";
    private static final String ADOPTION_SEQUENCE = "adoption_visual_code_seq";

    private final EntityManager entityManager;

    @Override
    public String nextUserCode() {
        return "U-" + pad4(nextSequenceValue(USER_SEQUENCE));
    }

    @Override
    public String nextOrganizationCode() {
        return "O-" + nextSequenceValue(ORG_SEQUENCE);
    }

    @Override
    public String nextPetCode() {
        return "P-" + pad4(nextSequenceValue(PET_SEQUENCE));
    }

    @Override
    public String nextRescueCaseCode() {
        return "R-" + pad4(nextSequenceValue(RESCUE_CASE_SEQUENCE));
    }

    @Override
    public String nextAdoptionCode() {
        return "A-" + pad4(nextSequenceValue(ADOPTION_SEQUENCE));
    }

    private long nextSequenceValue(String sequenceName) {
        Number value = (Number) entityManager
                .createNativeQuery("SELECT nextval('" + sequenceName + "')")
                .getSingleResult();
        return value.longValue();
    }

    private String pad4(long value) {
        return String.format("%04d", value);
    }
}
