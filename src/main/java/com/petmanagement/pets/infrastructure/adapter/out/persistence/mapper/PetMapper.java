package com.petmanagement.pets.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;
import com.petmanagement.pets.infrastructure.adapter.out.persistence.entity.PetJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public PetJpaEntity toJpaEntity(Pet pet) {
        return new PetJpaEntity(
                pet.getId().value(),
                pet.getOwnerId().value(),
                pet.getName().value(),
                pet.getSpecies().value(),
                pet.getBreed().value(),
                pet.getCoat().value(),
                pet.getSex().name(),
                pet.getBirthDate(),
                pet.getCreatedAt(),
                pet.getUpdatedAt()
        );
    }

    public Pet toDomain(PetJpaEntity entity) {
        return Pet.reconstitute(
                PetId.of(entity.getId()),
                OwnerId.of(entity.getOwnerId()),
                new PetName(entity.getName()),
                new Species(entity.getSpecies()),
                entity.getBreed() == null ? Breed.unknown() : new Breed(entity.getBreed()),
                new Coat(entity.getCoat()),
                Sex.valueOf(entity.getSex()),
                entity.getBirthDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
