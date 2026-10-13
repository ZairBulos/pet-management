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
            var weightRecord = WeightRecordTestBuilder.aWeightRecord().build();

            assertNotNull(weightRecord.id());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, weightRecord.petId());
            assertEquals(TestCommonMother.EARLY_DATE, weightRecord.weightDate());
            assertEquals(TestWeightRecordMother.MEDIUM_WEIGHT, weightRecord.weight());
            assertNotNull(weightRecord.createdAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachWeightRecord() {
            var weightRecord1 = WeightRecordTestBuilder.aWeightRecord().build();
            var weightRecord2 = WeightRecordTestBuilder.aWeightRecord().build();

            assertNotEquals(weightRecord1.id(), weightRecord2.id());
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

            var weightRecord = WeightRecord.reconstitute(
                    id,
                    TestPetIdMother.EXISTING_PET_ID,
                    TestCommonMother.EARLY_DATE,
                    TestWeightRecordMother.MEDIUM_WEIGHT,
                    createdAt
            );

            assertEquals(id, weightRecord.id());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, weightRecord.petId());
            assertEquals(TestCommonMother.EARLY_DATE, weightRecord.weightDate());
            assertEquals(TestWeightRecordMother.MEDIUM_WEIGHT, weightRecord.weight());
            assertEquals(createdAt, weightRecord.createdAt());
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
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var id = WeightRecordId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            id,
                            null,
                            TestCommonMother.EARLY_DATE,
                            TestWeightRecordMother.MEDIUM_WEIGHT,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullWeightDate() {
            var id = WeightRecordId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestWeightRecordMother.MEDIUM_WEIGHT,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullWeight() {
            var id = WeightRecordId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            null,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var id = WeightRecordId.generate();

            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecord.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestWeightRecordMother.MEDIUM_WEIGHT,
                            null
                    )
            );
        }

    }

}
