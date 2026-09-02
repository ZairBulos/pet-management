package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WeightRecordTest {

    private static final PetId PET_ID = PetId.of("58807bd8-f42a-4ae9-9bd9-48ec3c6f0485");
    private static final LocalDate WEIGHT_DATE = LocalDate.of(2026, 9, 1);
    private static final Weight WEIGHT = Weight.of(10.5);

    @Nested
    class Creation {

        @Test
        void shouldCreateWeightRecord() {
            var weightRecord = WeightRecord.create(PET_ID, WEIGHT_DATE, WEIGHT);

            assertNotNull(weightRecord.getId());
            assertEquals(PET_ID, weightRecord.getPetId());
            assertEquals(WEIGHT_DATE, weightRecord.getWeightDate());
            assertEquals(WEIGHT, weightRecord.getWeight());
            assertNotNull(weightRecord.getCreatedAt());
            assertNotNull(weightRecord.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachWeightRecord() {
            var weightRecord1 = WeightRecord.create(PET_ID, WEIGHT_DATE, WEIGHT);
            var weightRecord2 = WeightRecord.create(PET_ID, WEIGHT_DATE, WEIGHT);

            assertNotEquals(weightRecord1.getId(), weightRecord2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var weightRecord = WeightRecord.create(PET_ID, WEIGHT_DATE, WEIGHT);

            assertEquals(weightRecord.getCreatedAt(), weightRecord.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetId() {
            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.create(null, WEIGHT_DATE, WEIGHT)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullWeightDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.create(PET_ID, null, WEIGHT)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullWeight() {
            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.create(PET_ID, WEIGHT_DATE, null)
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstituteWeightRecord() {
            var id = WeightRecordId.generate();
            var createdAt = Instant.parse("2024-01-15T10:00:00Z");
            var updatedAt = Instant.parse("2024-01-20T15:30:00Z");

            var weightRecord = WeightRecord.reconstitute(
                    id,
                    PET_ID,
                    WEIGHT_DATE,
                    WEIGHT,
                    createdAt,
                    updatedAt
            );

            assertEquals(id, weightRecord.getId());
            assertEquals(PET_ID, weightRecord.getPetId());
            assertEquals(WEIGHT_DATE, weightRecord.getWeightDate());
            assertEquals(WEIGHT, weightRecord.getWeight());
            assertEquals(createdAt, weightRecord.getCreatedAt());
            assertEquals(updatedAt, weightRecord.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(null, PET_ID, WEIGHT_DATE, WEIGHT, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(WeightRecordId.generate(), null, WEIGHT_DATE, WEIGHT, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullWeightDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(WeightRecordId.generate(), PET_ID, null, WEIGHT, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullWeight() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(WeightRecordId.generate(), PET_ID, WEIGHT_DATE, null, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(WeightRecordId.generate(), PET_ID, WEIGHT_DATE, WEIGHT, null, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullUpdatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(WeightRecordId.generate(), PET_ID, WEIGHT_DATE, WEIGHT, now, null)
            );
        }

    }

    @Nested
    class Update {

        @Test
        void shouldUpdateWeightRecord() {
            var weightRecord = WeightRecord.create(PET_ID, WEIGHT_DATE, WEIGHT);
            var newWeightDate = LocalDate.of(2026, 9, 15);
            var newWeight = Weight.of(12.5);

            weightRecord.update(newWeightDate, newWeight);

            assertEquals(newWeightDate, weightRecord.getWeightDate());
            assertEquals(newWeight, weightRecord.getWeight());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullWeightDate() {
            var weightRecord = WeightRecord.create(PET_ID, WEIGHT_DATE, WEIGHT);

            assertThrows(
                    NullPointerException.class,
                    () -> weightRecord.update(null, WEIGHT)
            );
        }

        @Test
        void shouldThrowWhenUpdatingWithNullWeight() {
            var weightRecord = WeightRecord.create(PET_ID, WEIGHT_DATE, WEIGHT);

            assertThrows(
                    NullPointerException.class,
                    () -> weightRecord.update(WEIGHT_DATE, null)
            );
        }

    }

}
