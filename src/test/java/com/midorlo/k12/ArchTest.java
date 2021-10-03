package com.midorlo.k12;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.midorlo.k12");

        noClasses()
            .that()
            .resideInAnyPackage("com.midorlo.k12.service..")
            .or()
            .resideInAnyPackage("com.midorlo.k12.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.midorlo.k12.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
