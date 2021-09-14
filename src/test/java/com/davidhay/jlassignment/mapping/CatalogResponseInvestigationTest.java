package com.davidhay.jlassignment.mapping;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.davidhay.jlassignment.domain.inbound.Price;
import com.davidhay.jlassignment.domain.inbound.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

/**
 * This test was used to look at an example CatalogResponse and check that we can de-serialize it.
 */
@SpringBootTest
@Disabled
public class CatalogResponseInvestigationTest {

  @Autowired
  private ObjectMapper mapper;

  @Value("classpath:catalog/exampleProductCatalogResponse.json")
  Resource exampleSearchResponse;

  @Test
  public void testDeSeralizeExampleResponse() throws IOException {
    System.out.println(exampleSearchResponse);

    JsonNode tree = mapper.reader()
        .readTree(exampleSearchResponse.getInputStream());

    JsonNode productsRaw = tree.findPath("products");
    AtomicInteger count = new AtomicInteger(0);
    Set<String> colors = new HashSet<>();
    List<JsonNode> nowNodes = new ArrayList<>();
    if (productsRaw.isArray()) {
      for (JsonNode jsonNode : productsRaw) {
        count.incrementAndGet();
        Product o = mapper.treeToValue(jsonNode, Product.class);
        o.getColorSwatches().forEach(col -> colors.add(col.getBasicColor()));
        assertThat(o.getTitle()).isNotBlank();
        assertThat(o.getProductId()).isNotBlank();
        Price price = o.getPrice();
        assertThat(price).isNotNull();
        assertThat(price.getCurrency()).isNotNull();
        nowNodes.add(jsonNode.get("price").get("now"));
      }
    }
    System.out.println("-------------------------------");
    System.out.println(new ArrayList<>(colors).stream().sorted().collect(Collectors.toList()));
    System.out.println("-------------------------------");
    nowNodes.forEach(nn -> {
      try {
        mapper.writer().writeValue(System.out, nn);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        System.out.println("-------------------------------");
      }
    });
    assertEquals(24, count.get());
    System.out.printf("PRODUCTS %d", count.get());
    System.out.println("FIN");
  }
}
