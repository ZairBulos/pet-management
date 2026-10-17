package com.petmanagement.health.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.*;
import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.DewormingJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class DewormingMapper {

    public DewormingJpaEntity toJpaEntity(Deworming deworming) {
        return new DewormingJpaEntity(
                deworming.id().value(),
                deworming.petId().value(),
                deworming.dewormingDate(),
                deworming.drugName().value(),
                deworming.drugDose().value(),
                deworming.nextDueDate().value(),
                deworming.createdAt()
        );
    }

    public Deworming toDomain(DewormingJpaEntity entity) {
        return Deworming.reconstitute(
                DewormingId.of(entity.getId()),
                PetId.of(entity.getPetId()),
                entity.getDewormingDate(),
                new DrugName(entity.getDrugName()),
                new DrugDose(entity.getDrugDose()),
                new NextDueDate(entity.getNextDueDate()),
                entity.getCreatedAt()
        );
    }

}
