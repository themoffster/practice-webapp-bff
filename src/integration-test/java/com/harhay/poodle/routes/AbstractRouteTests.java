package com.harhay.poodle.routes;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.harhay.poodle.utils.FileUtils.fileToString;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.springframework.cloud.gateway.filter.headers.ForwardedHeadersFilter.FORWARDED_HEADER;
import static org.springframework.cloud.gateway.filter.headers.XForwardedHeadersFilter.X_FORWARDED_FOR_HEADER;
import static org.springframework.cloud.gateway.filter.headers.XForwardedHeadersFilter.X_FORWARDED_HOST_HEADER;
import static org.springframework.cloud.gateway.filter.headers.XForwardedHeadersFilter.X_FORWARDED_PORT_HEADER;
import static org.springframework.cloud.gateway.filter.headers.XForwardedHeadersFilter.X_FORWARDED_PROTO_HEADER;
import static org.springframework.http.HttpHeaders.ACCEPT_ENCODING;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.util.StringUtils.hasText;

@ExtendWith(SpringExtension.class)
class AbstractRouteTests {

    @AfterEach
    public void tearDown() {
        WireMock.reset();
    }

    @SuppressWarnings("SameParameterValue")
    protected void createUpstreamServiceStub(String method, String uri, String body, HttpStatus status) {
        ResponseDefinitionBuilder responseBuilder = aResponse();
        if (hasText(body)) {
            responseBuilder.withHeader(CONTENT_TYPE, MimeMappings.DEFAULT.get(getExtension(body)))
                .withStatus(status.value())
                .withBody(fileToString(body, getClass()).getBytes());
        }

        ScenarioMappingBuilder mappingBuilder = request(method, urlEqualTo(uri))
            .inScenario(uri)
            .withHeader(FORWARDED_HEADER, containing("proto=https;host=\"www.eservices.ros.gov.uk:443\";for=\"1.2.3.4:"))
            .withHeader(X_FORWARDED_HOST_HEADER, equalTo("www.eservices.ros.gov.uk"))
            .withHeader(X_FORWARDED_PORT_HEADER, equalTo("443"))
            .withHeader(X_FORWARDED_PROTO_HEADER, equalTo("https"))
            .willReturn(responseBuilder.withStatus(status.value()));

        stubFor(mappingBuilder);
    }

    protected Consumer<HttpHeaders> headers() {
        return httpHeaders -> {
            httpHeaders.put(ACCEPT_ENCODING, singletonList("none"));
            httpHeaders.put(HOST, singletonList("http://localhost"));
            httpHeaders.put(X_FORWARDED_FOR_HEADER, singletonList("1.2.3.4"));
            httpHeaders.put(X_FORWARDED_HOST_HEADER, singletonList("www.eservices.ros.gov.uk"));
            httpHeaders.put(X_FORWARDED_PORT_HEADER, singletonList("443"));
            httpHeaders.put(X_FORWARDED_PROTO_HEADER, singletonList("https"));
        };
    }
}
