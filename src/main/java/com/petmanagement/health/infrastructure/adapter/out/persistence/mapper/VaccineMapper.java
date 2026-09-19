package com.petmanagement.health.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.VaccineJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VaccineMapper {

    public VaccineJpaEntity toJpaEntity(Vaccine vaccine) {
        return new VaccineJpaEntity(
                vaccine.getId().value(),
                vaccine.getPetId().value(),
                vaccine.getVaccinationDate(),
                vaccine.getVaccineName().value(),
                vaccine.getNextDueDate().value(),
                vaccine.getCreatedAt(),
                vaccine.getUpdatedAt()
        );
    }

    public Vaccine toDomain(VaccineJpaEntity entity) {
        return Vaccine.reconstitute(
                VaccineId.of(entity.getId()),
                PetId.of(entity.getPetId()),
                entity.getVaccinationDate(),
                new VaccineName(entity.getVaccineName()),
                new NextDueDate(entity.getNextDueDate()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
