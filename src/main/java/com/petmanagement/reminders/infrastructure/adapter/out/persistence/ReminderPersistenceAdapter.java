package com.petmanagement.reminders.infrastructure.adapter.out.persistence;

import com.petmanagement.reminders.application.port.out.ReminderRepositoryPort;
import com.petmanagement.reminders.domain.model.aggregate.Reminder;
import com.petmanagement.reminders.domain.model.valueobject.SourceId;
import com.petmanagement.reminders.infrastructure.adapter.out.persistence.mapper.ReminderMapper;
import com.petmanagement.reminders.infrastructure.adapter.out.persistence.repository.ReminderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ReminderPersistenceAdapter implements ReminderRepositoryPort {

    private final ReminderJpaRepository repository;
    private final ReminderMapper mapper;

    ReminderPersistenceAdapter(ReminderJpaRepository repository, ReminderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Reminder> findBySourceId(SourceId sourceId) {
        return repository.findBySourceId(sourceId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void save(Reminder reminder) {
        repository.save(mapper.toJpaEntity(reminder));
    }

}
