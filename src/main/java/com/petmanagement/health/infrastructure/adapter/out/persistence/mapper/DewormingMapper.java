package com.petmanagement.health.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.*;
import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.DewormingJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class DewormingMapper {

    public DewormingJpaEntity toJpaEntity(Deworming deworming) {
        return new DewormingJpaEntity(
                deworming.getId().value(),
                deworming.getPetId().value(),
                deworming.getDewormingDate(),
                deworming.getDrugName().value(),
                deworming.getDrugDose().value(),
                deworming.getNextDueDate().value(),
                deworming.getCreatedAt(),
                deworming.getUpdatedAt()
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
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
