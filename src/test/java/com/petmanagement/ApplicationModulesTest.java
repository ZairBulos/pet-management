package com.petmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ApplicationModulesTest {

    private final ApplicationModules applicationModules = ApplicationModules.of(PetManagementApplication.class);

    @Test
    void verifiesModularStructure() {
        applicationModules.verify();
    }

    @Test
    void documentsModularStructure() {
        new Documenter(applicationModules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }

}
