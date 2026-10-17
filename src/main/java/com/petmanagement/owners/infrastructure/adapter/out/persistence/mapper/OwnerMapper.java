package com.petmanagement.owners.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;
import com.petmanagement.owners.infrastructure.adapter.out.persistence.entity.OwnerJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {

    public OwnerJpaEntity toJpaEntity(Owner owner) {
        return new OwnerJpaEntity(
                owner.getId().value(),
                owner.getName().value(),
                owner.getEmail().value(),
                owner.getPhone().value(),
                owner.getCreatedAt(),
                owner.getUpdatedAt()
        );
    }

    public Owner toDomain(OwnerJpaEntity entity) {
        return Owner.reconstitute(
                OwnerId.of(entity.getId()),
                new OwnerName(entity.getName()),
                new Email(entity.getEmail()),
                new PhoneNumber(entity.getPhoneNumber()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
