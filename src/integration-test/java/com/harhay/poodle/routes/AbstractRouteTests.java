package com.harhay.poodle.routes;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.harhay.poodle.config.EmbeddedDatabaseTestConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.harhay.poodle.utils.FileUtils.fileToString;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.StringUtils.hasText;

@ExtendWith(SpringExtension.class)
@Import(EmbeddedDatabaseTestConfig.class)
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
class AbstractRouteTests {

    @AfterEach
    public void tearDown() {
        WireMock.reset();
    }

    protected void createUpstreamServiceStub(String method, String uri, String body) {
        ResponseDefinitionBuilder responseBuilder = aResponse();
        if (hasText(body)) {
            responseBuilder.withHeader(CONTENT_TYPE, MimeMappings.DEFAULT.get(getExtension(body)))
                .withStatus(OK.value())
                .withBody(requireNonNull(fileToString(body, getClass())).getBytes());
        }

        ScenarioMappingBuilder mappingBuilder = request(method, urlEqualTo(uri))
            .inScenario(uri)
            .willReturn(responseBuilder.withStatus(OK.value()));

        stubFor(mappingBuilder);
    }
}
