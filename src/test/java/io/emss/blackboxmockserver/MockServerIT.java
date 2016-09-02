package io.emss.blackboxmockserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockServerIT {
  @LocalServerPort
  private int port;

  @Autowired
  private RestTemplate restTemplate;

  private URI productEndpoint;

  private void setupMockRequests(MockRestServiceServer server) {
    server
      .expect(requestTo("http://emss.io/dk/products"))
      .andRespond(withSuccess("[{\"countryCode\":\"dk\", \"name\":\"danish pastry\"}]", MediaType.APPLICATION_JSON));

    server
      .expect(requestTo("http://emss.io/ie/products"))
      .andRespond(withSuccess("[{\"countryCode\":\"ie\", \"name\":\"shamrock\"}]", MediaType.APPLICATION_JSON));
  }

  @Before
  public void setup() {
    productEndpoint = new UriTemplate("http://localhost:{port}/products").expand(port);
  }

  @Test
  public void shouldCallDownstreamServicesSuccess() {
    MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate)
      .ignoreExpectOrder(true)
      .build();
    setupMockRequests(server);

    given()
      .when()
        .get(productEndpoint)
      .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void shouldCallDownstreamServicesFail() {
    MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
    setupMockRequests(server);

    given()
      .when()
        .get(productEndpoint)
      .then()
        .statusCode(HttpStatus.OK.value());
  }
}
