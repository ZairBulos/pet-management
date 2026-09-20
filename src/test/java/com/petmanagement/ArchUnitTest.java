package com.petmanagement;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.ValueObject;
import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
        packages = "com.petmanagement",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class ArchUnitTest {

    // =========================================================================
    // Hexagonal Rules
    // =========================================================================

    @ArchTest
    static final ArchRule domain_should_only_depend_on_domain =
            classes().that().resideInAPackage("..domain..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage(
                            "..domain..",
                            "..java..",
                            "org.jmolecules..",
                            "org.springframework.modulith.."
                    )
                    .because("Domain layer must be independent and not depend on application or infrastructure");

    @ArchTest
    static final ArchRule application_should_only_depend_on_domain_and_application =
            classes().that().resideInAPackage("..application..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage(
                            "..domain..",
                            "..application..",
                            "..api..",
                            "..java..",
                            "org.jmolecules..",
                            "org.springframework.."
                    )
                    .because("Application layer must not depend on infrastructure layer");

    @ArchTest
    static final ArchRule infrastructure_can_depend_on_everything_above =
            classes().that().resideInAPackage("..infrastructure..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage(
                            "..domain..",
                            "..application..",
                            "..infrastructure..",
                            "..java..",
                            "..jakarta..",
                            "org.jmolecules..",
                            "org.springframework.."
                    )
                    .because("Infrastructure can depend on all layers");

    // =========================================================================
    // Domain Rules
    // =========================================================================

    // --- Aggregate Roots ----------------------------------------------------

    @ArchTest
    static final ArchRule domain_aggregates_must_be_in_aggregate_package =
            classes().that().areAnnotatedWith(AggregateRoot.class)
                    .should().resideInAPackage("..domain.model.aggregate..")
                    .allowEmptyShould(true)
                    .because("All aggregates must be in the domain.model.aggregate package");

    @ArchTest
    static final ArchRule domain_aggregate_roots_must_be_annotated =
            classes().that().resideInAPackage("..domain.model.aggregate..")
                    .and().areTopLevelClasses()
                    .should().beAnnotatedWith(AggregateRoot.class)
                    .allowEmptyShould(true)
                    .because("All aggregates must be annotated with @AggregateRoot");

    // --- Entities -----------------------------------------------------------

    @ArchTest
    static final ArchRule domain_entities_must_be_in_entity_package =
            classes().that().areAnnotatedWith(Entity.class)
                    .should().resideInAPackage("..domain.model.entity..")
                    .allowEmptyShould(true)
                    .because("All entities must be in the domain.model.entity package");

    @ArchTest
    static final ArchRule domain_entities_must_be_annotated =
            classes().that().resideInAPackage("..domain.model.entity..")
                    .and().areTopLevelClasses()
                    .should().beAnnotatedWith(Entity.class)
                    .allowEmptyShould(true)
                    .because("All entities must be annotated with @Entity");

    // --- Value Objects ------------------------------------------------------

    @ArchTest
    static final ArchRule domain_value_objects_must_be_in_valueobject_package =
            classes().that().areAnnotatedWith(ValueObject.class)
                    .should().resideInAPackage("..domain.model.valueobject..")
                    .allowEmptyShould(true)
                    .because("All value objects must be in the domain.model.valueobject package");

    @ArchTest
    static final ArchRule domain_value_objects_must_be_annotated =
            classes().that().resideInAPackage("..domain.model.valueobject..")
                    .and().areTopLevelClasses()
                    .should().beAnnotatedWith(ValueObject.class)
                    .allowEmptyShould(true)
                    .because("All value objects must be annotated with @ValueObject");

    // --- Domain Events ------------------------------------------------------

    @ArchTest
    static final ArchRule domain_events_must_be_in_event_package =
            classes().that().areAnnotatedWith(DomainEvent.class)
                    .should().resideInAPackage("..domain.event..")
                    .allowEmptyShould(true)
                    .because("All domain events must be in the domain.event package");

    @ArchTest
    static final ArchRule domain_events_must_be_annotated =
            classes().that().resideInAPackage("..domain.event..")
                    .and().areTopLevelClasses()
                    .should().beAnnotatedWith(DomainEvent.class)
                    .allowEmptyShould(true)
                    .because("All domain events must be annotated with @DomainEvent");

    @ArchTest
    static final ArchRule domain_events_must_implements_domain_event_interface =
            classes().that().resideInAPackage("..domain.event..")
                    .and().areTopLevelClasses()
                    .should().implement(org.jmolecules.event.types.DomainEvent.class)
                    .allowEmptyShould(true)
                    .because("All domain events must implement DomainEvent interface");

    // --- Domain Exceptions --------------------------------------------------

    @ArchTest
    static final ArchRule domain_exceptions_must_be_in_exception_package =
            classes().that().haveSimpleNameEndingWith("Exception")
                    .and().resideInAPackage("..domain..")
                    .should().resideInAPackage("..domain.exception..")
                    .allowEmptyShould(true)
                    .because("All domain exceptions must be in the domain.exception package");

    @ArchTest
    static final ArchRule domain_exceptions_must_extend_runtime_exception =
            classes().that().resideInAPackage("..domain.exception..")
                    .should().beAssignableTo(RuntimeException.class)
                    .allowEmptyShould(true)
                    .because("All domain exceptions must extend RuntimeException");

    @ArchTest
    static final ArchRule domain_exceptions_must_follow_naming_convention =
            classes().that().resideInAPackage("..domain.exception..")
                    .should().haveSimpleNameEndingWith("Exception")
                    .allowEmptyShould(true)
                    .because("All domain exception classes must end with 'Exception'");

    // =========================================================================
    // Application Rules
    // =========================================================================

    // --- Application Ports (In / Out) ---------------------------------------

    @ArchTest
    static final ArchRule application_ports_must_be_interfaces =
            classes().that().resideInAPackage("..application.port..")
                    .and().areNotNestedClasses()
                    .should().beInterfaces()
                    .because("All top-level ports (in/out) must be interfaces, not concrete classes");

    @ArchTest
    static final ArchRule application_ports_are_public =
            classes().that().resideInAPackage("..application.port..")
                    .and().areTopLevelClasses()
                    .should().bePublic()
                    .because("All top-level ports (in/out) must be public to allow external access");

    @ArchTest
    static final ArchRule application_input_ports_must_follow_naming_convention =
            classes().that().resideInAPackage("..application.port.in..")
                    .and().areTopLevelClasses()
                    .should().haveSimpleNameEndingWith("UseCase")
                    .because("All use case interfaces must end with 'UseCase'");

    @ArchTest
    static final ArchRule application_output_ports_must_follow_naming_convention =
            classes().that().resideInAPackage("..application.port.out..")
                    .and().areTopLevelClasses()
                    .should().haveSimpleNameEndingWith("Port")
                    .because("All output ports must end with 'Port'");

    // --- Application Services -----------------------------------------------

    @ArchTest
    static final ArchRule application_services_must_be_package_private =
            classes().that().resideInAPackage("..application.service..")
                    .and().areAnnotatedWith(Service.class)
                    .should().notBePublic()
                    .allowEmptyShould(true)
                    .because("Services must not be directly accessible, only through ports");

    @ArchTest
    static final ArchRule application_services_must_use_service_annotation =
            classes().that().resideInAPackage("..application.service..")
                    .and().haveSimpleNameEndingWith("Service")
                    .should().beAnnotatedWith(Service.class)
                    .allowEmptyShould(true)
                    .because("All service implementations must use @Service annotation");

    @ArchTest
    static final ArchRule application_services_must_follow_naming_convention =
            classes().that().resideInAPackage("..application.service..")
                    .and().areTopLevelClasses()
                    .should().haveSimpleNameEndingWith("Service")
                    .allowEmptyShould(true)
                    .because("All application services must end with 'Service'");

    // =========================================================================
    // Infrastructure Rules
    // =========================================================================

    // --- Persistence Entities -----------------------------------------------

    @ArchTest
    static final ArchRule infrastructure_entities_must_be_in_entity_package =
            classes().that().resideInAPackage("..infrastructure.adapter.out.persistence..")
                    .and().areAnnotatedWith("jakarta.persistence.Entity")
                    .should().resideInAPackage("..infrastructure.adapter.out.persistence.entity..")
                    .allowEmptyShould(true)
                    .because("All infrastructure entities must be in the entity package");

    @ArchTest
    static final ArchRule infrastructure_entities_must_follow_naming_convention =
            classes().that().resideInAPackage("..infrastructure.adapter.out.persistence.entity..")
                    .and().areTopLevelClasses()
                    .should().haveSimpleNameEndingWith("Entity")
                    .allowEmptyShould(true)
                    .because("All infrastructure entities must end with 'Entity'");


    // --- Repositories -------------------------------------------------------

    @ArchTest
    static final ArchRule infrastructure_repositories_must_be_in_repository_package =
            classes().that().resideInAPackage("..infrastructure.adapter.out.persistence..")
                    .and().haveSimpleNameEndingWith("Repository")
                    .should().resideInAPackage("..infrastructure.adapter.out.persistence.repository..")
                    .allowEmptyShould(true)
                    .because("All infrastructure repositories must be in the repository package");

    @ArchTest
    static final ArchRule infrastructure_repositories_must_be_interfaces =
            classes().that().resideInAPackage("..infrastructure.adapter.out.persistence.repository..")
                    .and().areTopLevelClasses()
                    .should().beInterfaces()
                    .allowEmptyShould(true)
                    .because("Spring Data repositories must be interfaces");

    // --- Mappers ------------------------------------------------------------

    @ArchTest
    static final ArchRule infrastructure_mappers_must_be_in_mapper_package =
            classes().that().resideInAPackage("..infrastructure.adapter.out.persistence..")
                    .and().haveSimpleNameEndingWith("Mapper")
                    .should().resideInAPackage("..infrastructure.adapter.out.persistence.mapper..")
                    .allowEmptyShould(true)
                    .because("All mappers must be in the mapper package");

    @ArchTest
    static final ArchRule infrastructure_mappers_must_follow_naming_convention =
            classes().that().resideInAPackage("..infrastructure.adapter.out.persistence.mapper..")
                    .and().areTopLevelClasses()
                    .should().haveSimpleNameEndingWith("Mapper")
                    .allowEmptyShould(true)
                    .because("All mappers must end with 'Mapper'");

}
