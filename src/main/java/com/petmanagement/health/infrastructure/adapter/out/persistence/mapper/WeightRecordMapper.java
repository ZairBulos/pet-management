package com.petmanagement.health.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.WeightRecordJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class WeightRecordMapper {

    public WeightRecordJpaEntity toJpaEntity(WeightRecord weightRecord) {
        return new WeightRecordJpaEntity(
                weightRecord.getId().value(),
                weightRecord.getPetId().value(),
                weightRecord.getWeightDate(),
                weightRecord.getWeight().value(),
                weightRecord.getCreatedAt(),
                weightRecord.getUpdatedAt()
        );
    }

    public WeightRecord toDomain(WeightRecordJpaEntity entity) {
        return WeightRecord.reconstitute(
                WeightRecordId.of(entity.getId()),
                PetId.of(entity.getPetId()),
                entity.getWeightDate(),
                new Weight(entity.getWeight()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
