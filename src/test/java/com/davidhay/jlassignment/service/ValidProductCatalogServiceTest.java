package com.davidhay.jlassignment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.repository.ProductCatalogRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidProductCatalogServiceTest {

  @Mock
  ProductCatalogRepository mRepo;

  @InjectMocks
  ValidProductCatalogService sut;

  @Mock
  Product mProduct1, mProduct2, mProduct3, mProduct4, mProduct5;

  @Test
  void testServiceFilter() {
    when(mRepo.getProductsFromCatalog()).thenReturn(
        List.of(mProduct1, mProduct2, mProduct3, mProduct4, mProduct5));
    when(mProduct1.hasValidNowPrice()).thenReturn(true);
    when(mProduct3.hasValidNowPrice()).thenReturn(true);
    when(mProduct5.hasValidNowPrice()).thenReturn(true);

    List<Product> result = sut.getValidProducts();

    assertThat(result).isEqualTo(List.of(mProduct1, mProduct3, mProduct5));

    verify(mProduct1).hasValidNowPrice();
    verify(mProduct2).hasValidNowPrice();
    verify(mProduct3).hasValidNowPrice();
    verify(mProduct4).hasValidNowPrice();
    verify(mProduct5).hasValidNowPrice();

    verify(mRepo).getProductsFromCatalog();

    verifyNoMoreInteractions(mRepo, mProduct1, mProduct2, mProduct3, mProduct4, mProduct5);
  }


}
