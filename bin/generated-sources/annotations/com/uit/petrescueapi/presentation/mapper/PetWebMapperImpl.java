package com.uit.petrescueapi.presentation.mapper;

import com.uit.petrescueapi.application.dto.pet.PetResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetSummaryResponseDto;
import com.uit.petrescueapi.domain.entity.Pet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-27T02:54:39+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PetWebMapperImpl implements PetWebMapper {

    @Override
    public PetResponseDto toDto(Pet pet) {
        if ( pet == null ) {
            return null;
        }

        PetResponseDto.PetResponseDtoBuilder petResponseDto = PetResponseDto.builder();

        petResponseDto.id( pet.getId() );
        petResponseDto.name( pet.getName() );
        petResponseDto.species( pet.getSpecies() );
        petResponseDto.breed( pet.getBreed() );
        petResponseDto.age( pet.getAge() );
        petResponseDto.gender( pet.getGender() );
        petResponseDto.color( pet.getColor() );
        petResponseDto.weight( pet.getWeight() );
        petResponseDto.description( pet.getDescription() );
        petResponseDto.status( pet.getStatus() );
        petResponseDto.healthStatus( pet.getHealthStatus() );
        petResponseDto.vaccinated( pet.isVaccinated() );
        petResponseDto.neutered( pet.isNeutered() );
        petResponseDto.rescueDate( pet.getRescueDate() );
        petResponseDto.rescueLocation( pet.getRescueLocation() );
        List<String> list = pet.getImageUrls();
        if ( list != null ) {
            petResponseDto.imageUrls( new ArrayList<String>( list ) );
        }
        petResponseDto.shelterId( pet.getShelterId() );
        petResponseDto.createdAt( pet.getCreatedAt() );
        petResponseDto.updatedAt( pet.getUpdatedAt() );

        return petResponseDto.build();
    }

    @Override
    public List<PetResponseDto> toDtoList(List<Pet> pets) {
        if ( pets == null ) {
            return null;
        }

        List<PetResponseDto> list = new ArrayList<PetResponseDto>( pets.size() );
        for ( Pet pet : pets ) {
            list.add( toDto( pet ) );
        }

        return list;
    }

    @Override
    public PetSummaryResponseDto toSummaryDto(Pet pet) {
        if ( pet == null ) {
            return null;
        }

        PetSummaryResponseDto.PetSummaryResponseDtoBuilder petSummaryResponseDto = PetSummaryResponseDto.builder();

        petSummaryResponseDto.id( pet.getId() );
        petSummaryResponseDto.name( pet.getName() );
        petSummaryResponseDto.species( pet.getSpecies() );
        petSummaryResponseDto.breed( pet.getBreed() );
        petSummaryResponseDto.age( pet.getAge() );
        petSummaryResponseDto.vaccinated( pet.isVaccinated() );
        petSummaryResponseDto.gender( pet.getGender() );
        petSummaryResponseDto.status( pet.getStatus() );
        petSummaryResponseDto.healthStatus( pet.getHealthStatus() );

        petSummaryResponseDto.imageUrl( firstImage(pet.getImageUrls()) );

        return petSummaryResponseDto.build();
    }

    @Override
    public List<PetSummaryResponseDto> toSummaryDtoList(List<Pet> pets) {
        if ( pets == null ) {
            return null;
        }

        List<PetSummaryResponseDto> list = new ArrayList<PetSummaryResponseDto>( pets.size() );
        for ( Pet pet : pets ) {
            list.add( toSummaryDto( pet ) );
        }

        return list;
    }
}
