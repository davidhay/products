package com.davidhay.jlassignment.controller;

import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.domain.outbound.ProductsInfo;
import com.davidhay.jlassignment.service2.ReducedPriceDressesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class DressesController2 {

  public static final String REDUCED_PRICES_DRESSES_PATH = "/dresses/reduced";

  public static final String LABEL_TYPE = "labelType";

  private final ReducedPriceDressesService service;

  public DressesController2(ReducedPriceDressesService service) {
    this.service = service;
  }

  @RequestMapping(REDUCED_PRICES_DRESSES_PATH)
  public ProductsInfo getReducedPriceDresses(
      @RequestParam(value = LABEL_TYPE) Optional<LabelType> optLabelType) {
    // be extra careful
    if (optLabelType == null) {
      optLabelType = Optional.empty();
    }
    LabelType labelType = optLabelType.orElse(LabelType.ShowWasNow);
    List<ProductInfo> productInfos = service.getReducedPriceDresses(labelType);

    return new ProductsInfo(productInfos);
  }


}
