package com.davidhay.jlassignment.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.domain.outbound.ProductsInfo;
import com.davidhay.jlassignment.domain.outbound.ProductsInfoSerializationTest;
import com.davidhay.jlassignment.service.ReducedProductsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
public class DressesControllerMockMvcTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ReducedProductsService mService;

  @Captor
  ArgumentCaptor<LabelType> argLabelType;

  @Autowired
  ObjectMapper mapper;

  @Test
  void testNoProductsFound() throws Exception {

    when(mService.getReducedPriceDresses(argLabelType.capture())).thenReturn(
        Collections.emptyList());
    mockMvc
        .perform(MockMvcRequestBuilders.get(DressesController.REDUCED_PRICES_DRESSES_PATH))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.products").isArray())
        .andExpect(jsonPath("$.products").isEmpty());
  }

  @Test
  void testProductsFound() throws Exception {
    ProductsInfo productInfos1 = ProductsInfoSerializationTest.getProductsInfoWithSwatches();
    List<ProductInfo> products = productInfos1.getProducts();

    when(mService.getReducedPriceDresses(argLabelType.capture())).thenReturn(products);

    mockMvc
        .perform(MockMvcRequestBuilders.get(DressesController.REDUCED_PRICES_DRESSES_PATH))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.products").isArray())
        .andExpect(jsonPath("$.products").isNotEmpty())

        .andExpect(jsonPath("$.products[0].productId").value("productId1"))
        .andExpect(jsonPath("$.products[0].title").value("title1"))
        .andExpect(jsonPath("$.products[0].nowPrice").value("nowPrice1"))
        .andExpect(jsonPath("$.products[0].priceLabel").value("priceLabel1"))
        .andExpect(jsonPath("$.products[0].colorSwatches").isArray())
        .andExpect(jsonPath("$.products[0].colorSwatches").isNotEmpty())
        .andExpect(jsonPath("$.products[0].colorSwatches[0].color").value("colorA1"))
        .andExpect(jsonPath("$.products[0].colorSwatches[0].rgbColor").value("rgbColorA1"))
        .andExpect(jsonPath("$.products[0].colorSwatches[0].skuid").value("skuIdA1"))
        .andExpect(jsonPath("$.products[0].colorSwatches[1].color").value("colorA2"))
        .andExpect(jsonPath("$.products[0].colorSwatches[1].rgbColor").value("rgbColorA2"))
        .andExpect(jsonPath("$.products[0].colorSwatches[1].skuid").value("skuIdA2"))
        .andExpect(jsonPath("$.products[1].productId").value("productId2"))
        .andExpect(jsonPath("$.products[1].title").value("title2"))
        .andExpect(jsonPath("$.products[1].nowPrice").value("nowPrice2"))
        .andExpect(jsonPath("$.products[1].priceLabel").value("priceLabel2"))
        .andExpect(jsonPath("$.products[1].colorSwatches[0].color").value("colorB1"))
        .andExpect(jsonPath("$.products[1].colorSwatches[0].rgbColor").value("rgbColorB1"))
        .andExpect(jsonPath("$.products[1].colorSwatches[0].skuid").value("skuIdB1"))
        .andExpect(jsonPath("$.products[1].colorSwatches[1].color").value("colorB2"))
        .andExpect(jsonPath("$.products[1].colorSwatches[1].rgbColor").value("rgbColorB2"))
        .andExpect(jsonPath("$.products[1].colorSwatches[1].skuid").value("skuIdB2"))
        .andDo(MockMvcResultHandlers.print());
  }

}
