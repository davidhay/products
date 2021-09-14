package com.davidhay.jlassignment.service;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReducedPriceDressesServiceTest {

    @Mock
    ProductInfoService mProductInfoService;

    @Mock
    Predicate<ProductInfo> mHasPriceReductionPredicate;

    @Mock
    Comparator<ProductInfo> mSortProductInfoByPriceReductionInPenceDescending;

    @Mock
    Function<LabelType, Consumer<ProductInfo>> mLabelTypeToProductInfoLabeler;

    @Mock
    Consumer<ProductInfo> mProductInfoLabeler;

    @Mock
    List<ProductInfo> mProductInfoList;

    @InjectMocks
    ReducedPriceDressesService sut;

    void checkReducedPriceDressesForLabelType(LabelType type) {
        when(mLabelTypeToProductInfoLabeler.apply(type)).thenReturn(mProductInfoLabeler);

        when(mProductInfoService.getProductInfo(ReducedPriceDressesService.PRODUCT_DRESSES,
                mHasPriceReductionPredicate, mSortProductInfoByPriceReductionInPenceDescending, mProductInfoLabeler)).thenReturn(mProductInfoList);

        List<ProductInfo> result = sut.getReducedPriceDresses(type);

        assertEquals(result, mProductInfoList);

        verify(mLabelTypeToProductInfoLabeler).apply(type);
        verify(mProductInfoService).getProductInfo(ReducedPriceDressesService.PRODUCT_DRESSES,
                mHasPriceReductionPredicate, mSortProductInfoByPriceReductionInPenceDescending, mProductInfoLabeler);

        verifyNoMoreInteractions(mProductInfoList,
                mLabelTypeToProductInfoLabeler, mProductInfoService,
                mHasPriceReductionPredicate, mSortProductInfoByPriceReductionInPenceDescending,
                mProductInfoList);
    }

    @Test
    void testShowWasNow() {
        checkReducedPriceDressesForLabelType(LabelType.ShowWasNow);
    }

    @Test
    void testShowWasThenNow() {
        checkReducedPriceDressesForLabelType(LabelType.ShowWasThenNow);
    }

    @Test
    void testShowPercentageDiscount() {
        checkReducedPriceDressesForLabelType(LabelType.ShowPercDscount);
    }
}
