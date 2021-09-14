package com.davidhay.jlassignment.domain.outbound;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.davidhay.jlassignment.mapping.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
public class ProductsInfoSerializationTest {

  @Autowired
  ObjectMapper mapper;

  @Value("classpath:info/productInfosNoSwatches.json")
  Resource productInfosNoSwatches;

  @Value("classpath:info/productInfosWithSwatches.json")
  Resource productInfosWithSwatches;

  @Test
  void testSerializationNoSwatches() throws IOException {

    JsonNode expected = mapper.reader()
        .readTree(productInfosNoSwatches.getInputStream());

    JsonNode actual = mapper.valueToTree(getProductsInfoNoSwatches());

    assertEquals(expected, actual);
  }

  @Test
  void testSerializationWithSwatches() throws IOException {

    JsonNode expected = mapper.reader()
        .readTree(productInfosWithSwatches.getInputStream());

    JsonNode actual = mapper.valueToTree(getProductsInfoWithSwatches());

    assertEquals(expected, actual);
  }

  public static ProductInfo getProductInfo1(List<ColorSwatchInfo> swatches) {
    return ProductInfo.builder()
        .productId("productId1")
        .priceLabel("priceLabel1")
        .nowPrice("nowPrice1")
        .title("title1")
        .priceInfo(getPriceInfo("22", "11"))
        .colorSwatches(swatches).build();
  }

  private static PriceInfo getPriceInfo(String was, String now) {
    return new PriceInfo("GBP", JsonUtils.getAmount(was), JsonUtils.getAmount(now));
  }

  public static ProductInfo getProductInfo2(List<ColorSwatchInfo> swatches) {
    return ProductInfo.builder()
        .productId("productId2")
        .priceLabel("priceLabel2")
        .nowPrice("nowPrice2")
        .title("title2")
        .priceInfo(getPriceInfo("21", "12"))
        .colorSwatches(swatches).build();
  }

  public static ProductsInfo getProductsInfoNoSwatches() {
    ProductInfo p1 = getProductInfo1(null);
    ProductInfo p2 = getProductInfo2(null);
    return new ProductsInfo(Arrays.asList(p1, p2));
  }

  public static ProductsInfo getProductsInfoWithSwatches() {
    List<ColorSwatchInfo> swatchesA = getSwatches("A");
    List<ColorSwatchInfo> swatchesB = getSwatches("B");
    ProductInfo p1 = getProductInfo1(swatchesA);
    ProductInfo p2 = getProductInfo2(swatchesB);
    return new ProductsInfo(Arrays.asList(p1, p2));
  }

  public static ColorSwatchInfo getColorSwatchInfo(String prefix, String suffix) {
    return ColorSwatchInfo
        .builder()
        .color("color" + prefix + suffix)
        .rgbColor("rgbColor" + prefix + suffix)
        .skuId("skuId" + prefix + suffix)
        .build();
  }

  public static List<ColorSwatchInfo> getSwatches(String prefix) {
    ColorSwatchInfo info1 = getColorSwatchInfo(prefix, "1");
    ColorSwatchInfo info2 = getColorSwatchInfo(prefix, "2");
    ColorSwatchInfo info3 = getColorSwatchInfo(prefix, "3");
    return Arrays.asList(info1, info2, info3);
  }
}
