package com.davidhay.jlassignment.service;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.formatter.PriceLabelFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ReducedProductsService {

  private final ProductInfoService productInfoService;
  private final PriceLabelFormatter priceLabelFormatter;

  public ReducedProductsService(
      ProductInfoService productInfoService, PriceLabelFormatter priceLabelFormatter) {
    this.productInfoService = productInfoService;
    this.priceLabelFormatter = priceLabelFormatter;
  }

  List<ProductInfo> getProductInfo() {
    return this.productInfoService.getProductInfo();
  }


  List<ProductInfo> getReducedProductInfoStream(List<ProductInfo> productInfos) {

    // filter and sort
    return productInfos
        .stream()
        .filter(ProductInfo::hasPriceReduction)
        .sorted(Comparator.comparing(ProductInfo::getPriceReductionAmountPence).reversed())
        .collect(Collectors.toList());
  }


  List<ProductInfo> labelReducedProductInfos(LabelType labelType,
      List<ProductInfo> reducedProductInfos) {
    return reducedProductInfos
        .stream()
        .peek(productInfo -> {
          String label = this.priceLabelFormatter.getPriceLabel(productInfo.getPriceInfo(),
              labelType);
          productInfo.setPriceLabel(label);
        })
        .collect(Collectors.toList());
  }

  public List<ProductInfo> getReducedPriceDresses(LabelType labelType) {
    //ProductInfo represent data going out of this controller
    //this list of data could be cached for a short period
    List<ProductInfo> productInfos = getProductInfo();

    //We only want products that have been reduced - sorted so products with the biggest reduction (in pence) are first
    List<ProductInfo> reducedPriceProductInfos = getReducedProductInfoStream(productInfos);

    //We add labels
    return labelReducedProductInfos(labelType,
        reducedPriceProductInfos);
  }

}
