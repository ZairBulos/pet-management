package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestWeightRecordMother;
import com.petmanagement.health.support.WeightRecordTestBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class WeightRecordTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateWeightRecord() {
            var weightRecord = WeightRecordTestBuilder
                    .aWeightRecord()
                    .build();

            assertNotNull(weightRecord.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, weightRecord.getPetId());
            assertEquals(TestCommonMother.EARLY_DATE, weightRecord.getWeightDate());
            assertEquals(TestWeightRecordMother.MEDIUM_WEIGHT, weightRecord.getWeight());
            assertNotNull(weightRecord.getCreatedAt());
            assertNotNull(weightRecord.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachWeightRecord() {
            var weightRecord1 = WeightRecordTestBuilder
                    .aWeightRecord()
                    .build();
            var weightRecord2 = WeightRecordTestBuilder
                    .aWeightRecord()
                    .build();

            assertNotEquals(weightRecord1.getId(), weightRecord2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var weightRecord = WeightRecordTestBuilder
                    .aWeightRecord()
                    .build();

            assertEquals(weightRecord.getCreatedAt(), weightRecord.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetId() {
            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.create(
                            null,
                            TestCommonMother.EARLY_DATE,
                            TestWeightRecordMother.MEDIUM_WEIGHT
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullWeightDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestWeightRecordMother.MEDIUM_WEIGHT
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullWeight() {
            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            null
                    )
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
                    TestPetIdMother.EXISTING_PET_ID,
                    TestCommonMother.EARLY_DATE,
                    TestWeightRecordMother.MEDIUM_WEIGHT,
                    createdAt,
                    updatedAt
            );

            assertEquals(id, weightRecord.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, weightRecord.getPetId());
            assertEquals(TestCommonMother.EARLY_DATE, weightRecord.getWeightDate());
            assertEquals(TestWeightRecordMother.MEDIUM_WEIGHT, weightRecord.getWeight());
            assertEquals(createdAt, weightRecord.getCreatedAt());
            assertEquals(updatedAt, weightRecord.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            null,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestWeightRecordMother.MEDIUM_WEIGHT,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            WeightRecordId.generate(),
                            null,
                            TestCommonMother.EARLY_DATE,
                            TestWeightRecordMother.MEDIUM_WEIGHT,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullWeightDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            WeightRecordId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestWeightRecordMother.MEDIUM_WEIGHT,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullWeight() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            WeightRecordId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            null,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            WeightRecordId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestWeightRecordMother.MEDIUM_WEIGHT,
                            null,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullUpdatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            WeightRecordId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestWeightRecordMother.MEDIUM_WEIGHT,
                            now,
                            null
                    )
            );
        }

    }

    @Nested
    class Update {

        @Test
        void shouldUpdateWeightRecord() {
            var weightRecord = WeightRecordTestBuilder
                    .aWeightRecord()
                    .build();

            weightRecord.update(
                    TestCommonMother.UPDATE_DATE,
                    TestWeightRecordMother.UPDATED_WEIGHT
            );

            assertEquals(TestCommonMother.UPDATE_DATE, weightRecord.getWeightDate());
            assertEquals(TestWeightRecordMother.UPDATED_WEIGHT, weightRecord.getWeight());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullWeightDate() {
            var weightRecord = WeightRecordTestBuilder
                    .aWeightRecord()
                    .build();

            assertThrows(
                    NullPointerException.class,
                    () -> weightRecord.update(
                            null,
                            TestWeightRecordMother.UPDATED_WEIGHT
                    )
            );
        }

        @Test
        void shouldThrowWhenUpdatingWithNullWeight() {
            var weightRecord = WeightRecordTestBuilder
                    .aWeightRecord()
                    .build();

            assertThrows(
                    NullPointerException.class,
                    () -> weightRecord.update(
                            TestCommonMother.UPDATE_DATE,
                            null
                    )
            );
        }

    }

}
