package com.davidhay.jlassignment.domain.inbound;


import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
public class ProductDeserializerTest {

  @Autowired
  ObjectMapper mapper;

  @Value("classpath:catalog/exampleProduct.json")
  Resource exampleProduct;

  Product getProduct(Resource res) throws Exception {
    return mapper.reader().readValue(res.getInputStream(), Product.class);
  }

  @Test
  void testExampleProduct() throws Exception {
    Product p = getProduct(this.exampleProduct);
    assertThat(p.getProductId()).isEqualTo("5520412");
    assertThat(p.getTitle()).isEqualTo(
        "John Lewis & Partners School Belted Gingham Checked Summer Dress");
    Price price = p.getPrice();
    PriceDeserializerTest.checkPrice(price);

    ColorSwatch sw1 = getSwatch("239018760", "Yellow", "Yellow");
    ColorSwatch sw2 = getSwatch("239018691", "Green", "Green");
    ColorSwatch sw3 = getSwatch("239018687", "Purple", "Lilac");
    ColorSwatch sw4 = getSwatch("239018713", "Blue", "Navy");
    ColorSwatch sw5 = getSwatch("239018723", "Pink", "Pink");
    ColorSwatch sw6 = getSwatch("239018816", "Red", "Red");
    ColorSwatch sw7 = getSwatch("239018712", "Blue", "Light Blue");

    assertThat(p.getColorSwatches()).isEqualTo(List.of(sw1, sw2, sw3, sw4, sw5, sw6, sw7));

  }

  ColorSwatch getSwatch(String sku, String basicColor, String color) {
    return ColorSwatch.builder().skuId(sku).basicColor(basicColor).color(color).build();
  }

}
