package io.emss.blackboxmockserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class SearchController {

  private RestTemplate restTemplate;
  private String searchEndpoint;

  @Autowired
  public SearchController(RestTemplate restTemplate, @Value("${emss.products.endpoint}") String searchEndpoint) {
    this.restTemplate = restTemplate;
    this.searchEndpoint = searchEndpoint;
  }

  @RequestMapping("/products")
  public List<Product> get() {
    return Stream
      .concat(getProducts("ie/products").stream(), getProducts("dk/products").stream())
      .collect(Collectors.toList());
  }

  private List<Product> getProducts(String path) {
    return restTemplate
        .exchange(
          new RequestEntity<>(HttpMethod.GET, URI.create(searchEndpoint + path)),
          new ParameterizedTypeReference<List<Product>>(){}).getBody();
  }
}
