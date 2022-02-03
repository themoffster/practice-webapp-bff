package com.harhay.poodle.routes;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ParameterisedRouteTests extends AbstractRouteTests {

    @Autowired
    private WebTestClient webClient;

    /**
     * Test that requests made to the BFF are correctly mapped to the various services upstream.
     *
     * @param method          the http method to invoke
     * @param uri             the uri to invoke
     * @param upstreamMethod  the upstream method expected to be invoked
     * @param upstreamUri     the upstream uri expected to be invoked
     * @param body            the body to simulate being returned by te upstream service
     */
    @ParameterizedTest
    @CsvSource({
        "GET, /api/patients, GET, /patients,",
        "GET, /api/patients/123, GET, /patients/123, foo.json",
    })
    void shouldRouteUpstreamServices(
        String method, String uri, String upstreamMethod, String upstreamUri, String body) {

        createUpstreamServiceStub(upstreamMethod, upstreamUri, body);

        webClient.method(HttpMethod.valueOf(method)).uri(uri)
            .exchange()
            .expectStatus().isOk();
    }
}
