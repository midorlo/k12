package com.midorlo.k12.configuration.documentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.midorlo.k12.configuration.ApplicationConstants.ContextConstants.SPRING_PROFILE_API_DOCS;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
    properties = {
        "spring.liquibase.enabled=false",
        "security.basic.enabled=false",
        "application.api-docs.default-include-pattern=/scanned/.*",
        "application.api-docs.host=test.application.com",
        "application.api-docs.protocols=http,https",
        "application.api-docs.title=test title",
        "application.api-docs.description=test description",
        "application.api-docs.version=test version",
        "application.api-docs.terms-of-service-url=test tos url",
        "application.api-docs.contact-name=test contact name",
        "application.api-docs.contact-email=test contact email",
        "application.api-docs.contact-url=test contact url",
        "application.api-docs.license=test license name",
        "application.api-docs.license-url=test license url",
        "application.api-docs.servers[0].url=test server url",
        "management.endpoints.web.base-path=/management",
        "spring.application.name=testApp"

    })
@ActiveProfiles(SPRING_PROFILE_API_DOCS)
@AutoConfigureMockMvc
class SpringfoxAutoconfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generatesOAS() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect((status().isOk()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.info.title").value("test title"))
            .andExpect(jsonPath("$.info.description").value("test description"))
            .andExpect(jsonPath("$.info.version").value("test version"))
            .andExpect(jsonPath("$.info.termsOfService").value("test tos url"))
            .andExpect(jsonPath("$.info.contact.name").value("test contact name"))
            .andExpect(jsonPath("$.info.contact.url").value("test contact url"))
            .andExpect(jsonPath("$.info.contact.email").value("test contact email"))
            .andExpect(jsonPath("$.info.license.name").value("test license name"))
            .andExpect(jsonPath("$.info.license.url").value("test license url"))
            .andExpect(jsonPath("$.servers.[*].url").value(hasItem("http://localhost:80")));
    }

    @Test
    void generatesSwaggerV2() throws Exception {
        mockMvc.perform(get("/v2/api-docs"))
            .andExpect((status().isOk()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.host").value("test.application.com"))
            .andExpect(jsonPath("$.schemes").value(hasItems("http", "https")));
    }

    @Test
    void generatesManagementOAS() throws Exception {
        mockMvc.perform(get("/v3/api-docs?group=management"))
            .andExpect((status().isOk()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.info.title").value("TestApp Management API"))
            .andExpect(jsonPath("$.info.description").value("Management endpoints documentation"))
            .andExpect(jsonPath("$.info.version").value("test version"))
            .andExpect(jsonPath("$.info.termsOfService").doesNotExist())
            .andExpect(jsonPath("$.info.contact").isEmpty())
            .andExpect(jsonPath("$.info.license").isEmpty())
            .andExpect(jsonPath("$.paths./management/health").exists())
            .andExpect(jsonPath("$.servers.[*].url").value(hasItem("http://localhost:80")));
    }

    @Test
    void generatesManagementSwaggerV2() throws Exception {
        mockMvc.perform(get("/v2/api-docs?group=management"))
            .andExpect((status().isOk()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.paths./management/health").exists())
            .andExpect(jsonPath("$.host").value("test.application.com"))
            .andExpect(jsonPath("$.schemes").value(hasItems("http", "https")));    }
}


