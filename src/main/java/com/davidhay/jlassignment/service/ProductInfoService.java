package com.davidhay.jlassignment.service;

import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.mapper.ProductToProductInfoMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductInfoService {

  private final ValidProductCatalogService productCatalogService;

  private final ProductToProductInfoMapper productToProductInfoMapper;

  public ProductInfoService(
      ValidProductCatalogService productCatalogService,
      ProductToProductInfoMapper productToProductInfoMapper) {
    this.productCatalogService = productCatalogService;
    this.productToProductInfoMapper = productToProductInfoMapper;
  }

  /**
   * We could cache this data a short period of time to reduce load on back end.
   */
  List<ProductInfo> getProductInfo() {
    return
        productCatalogService.getValidProducts()
            .stream()
            .map(productToProductInfoMapper::mapToProductInfo)
            .collect(Collectors.toList());
  }

}
