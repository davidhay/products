package com.davidhay.jlassignment.service;

import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.inbound.ProductType;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.mapper.ProductToProductInfoMapper;
import com.davidhay.jlassignment.repository.ProductCatalogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductInfoServiceTest {

    @Mock
    ProductToProductInfoMapper mMapper;

    @Mock
    ProductCatalogRepository mRepo;

    @InjectMocks
    ProductInfoService sut;

    @Mock
    Product mProduct1, mProduct2, mProduct3;

    @Mock
    ProductInfo mProductInfo1, mProductInfo2, mProductInfo3;

    @Captor
    ArgumentCaptor<ProductType> argProductType;

    @Captor
    ArgumentCaptor<Product> argProduct;


    @Test
    void testGetProductInfo() {

        when(mRepo.getProductsFromCatalog(argProductType.capture())).thenReturn(List.of(mProduct1, mProduct2, mProduct3));

        when(mMapper.mapToProductInfo(argProduct.capture())).thenReturn(mProductInfo1, mProductInfo2, mProductInfo3);

        Predicate<ProductInfo> filter = p -> p != mProductInfo2;

        List<ProductInfo> order = List.of(mProductInfo3, mProductInfo2, mProductInfo1);
        Comparator<ProductInfo> comparator = Comparator.comparingInt(order::indexOf);

        Consumer<ProductInfo> labeller = (pi) -> {
            if (pi == mProductInfo1) {
                pi.setPriceLabel("LABEL-ONE");
            } else {
                pi.setPriceLabel("LABEL-TWO");
            }
        };
        List<ProductInfo> result = sut.getProductInfo(ProductType.DRESSES, filter, comparator, labeller);

        assertEquals(result, List.of(mProductInfo3, mProductInfo1));

        verify(mProductInfo1).setPriceLabel("LABEL-ONE");
        verify(mProductInfo3).setPriceLabel("LABEL-TWO");

        assertEquals(argProductType.getValue(), ProductType.DRESSES);

        assertEquals(argProduct.getAllValues(), List.of(mProduct1, mProduct2, mProduct3));
        verify(mMapper, times(3)).mapToProductInfo(any(Product.class));

        verifyNoMoreInteractions(mMapper, mRepo);
    }


}
