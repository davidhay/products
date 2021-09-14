package com.davidhay.jlassignment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.mapper.ProductToProductInfoMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductInfoServiceTest {

  @Mock
  ValidProductCatalogService mProductCatalogService;

  @Mock
  ProductToProductInfoMapper mMapper;


  @InjectMocks
  ProductInfoService sut;

  List<Product> validProducts;

  @Mock
  Product mProduct1, mProduct2, mProduct3;

  @Mock
  ProductInfo mProductInfo1, mProductInfo2, mProductInfo3;

  @Captor
  ArgumentCaptor<Product> argProduct;

  @BeforeEach
  void setup() {
    validProducts = List.of(mProduct1, mProduct2, mProduct3);
  }

  @Test
  void testGetProductInfo() {
    when(mMapper.mapToProductInfo(argProduct.capture())).thenReturn(mProductInfo1, mProductInfo2,
        mProductInfo3);
    when(mProductCatalogService.getValidProducts()).thenReturn(validProducts);

    List<ProductInfo> result = sut.getProductInfo();
    assertEquals(result, List.of(mProductInfo1, mProductInfo2, mProductInfo3));

    assertEquals(argProduct.getAllValues(), List.of(mProduct1, mProduct2, mProduct3));

    verify(mMapper, times(3)).mapToProductInfo(any(Product.class));

    verify(mProductCatalogService).getValidProducts();

    verifyNoMoreInteractions(mMapper, mProductCatalogService);
  }

}
