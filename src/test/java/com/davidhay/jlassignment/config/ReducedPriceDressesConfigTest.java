package com.davidhay.jlassignment.config;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.formatter.PriceLabelFormatter;
import com.davidhay.jlassignment.util.MoneyUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReducedPriceDressesConfigTest {

    @Mock
    PriceLabelFormatter mLabelFormatter;

    @InjectMocks
    ReducedPriceDressesConfig config;

    @Nested
    class LabellerChecks {

        void checkLabeller(LabelType type) {
            PriceInfo priceInfo = new PriceInfo(MoneyUtils.getMoney("12.34"));
            ProductInfo pInfo = ProductInfo.builder().priceInfo(priceInfo).build();
            String label = UUID.randomUUID().toString();
            when(mLabelFormatter.getPriceLabel(priceInfo, type)).thenReturn(label);

            Function<LabelType, Consumer<ProductInfo>> labelTypeToLabeller = config.productInfoLabeler();
            assertThat(pInfo.getPriceLabel()).isNull();
            labelTypeToLabeller.apply(type).accept(pInfo);
            assertThat(pInfo.getPriceLabel()).isEqualTo(label);

            verify(mLabelFormatter).getPriceLabel(priceInfo, type);
            verifyNoMoreInteractions(mLabelFormatter);
        }

        @Test
        void testShowWasNow() {
            checkLabeller(LabelType.ShowWasNow);
        }

        @Test
        void testShowWasThenNow() {
            checkLabeller(LabelType.ShowWasThenNow);
        }

        @Test
        void testShowPercDscount() {
            checkLabeller(LabelType.ShowPercDscount);
        }

    }

    @Mock
    ProductInfo mProductInfo1, mProductInfo2, mProductInfo3;

    @Test
    void testHasPriceReductionPredicate(){
        when(mProductInfo1.hasPriceReduction()).thenReturn(true);
        when(mProductInfo2.hasPriceReduction()).thenReturn(false);
        when(mProductInfo3.hasPriceReduction()).thenReturn(true);

        List<ProductInfo> result = Stream.of(mProductInfo1, mProductInfo2, mProductInfo3)
                .filter(config.hasPriceReductionPredicate())
                .collect(Collectors.toList());

        assertEquals(result, List.of(mProductInfo1, mProductInfo3));
    }

    @Test
    void testSortProductInfoByPriceReductionInPenceDescending() {
        Comparator<ProductInfo> comp = config.sortProductInfoByPriceReductionInPenceDescending();

        ProductInfo pi1 = getProductInfo("1.01","1");
        ProductInfo pi2 = getProductInfo("1",".1");
        ProductInfo pi3 = getProductInfo("1.02","1");
        ProductInfo pi4 = getProductInfo("21.12","21.12");

        List<ProductInfo> result = Stream.of(pi1, pi2,pi3,pi4).sorted(comp).collect(Collectors.toList());
        assertEquals(result, List.of(pi2,pi3,pi1,pi4));
    }

    private ProductInfo getProductInfo(String was, String now){
        BigDecimal wasM = MoneyUtils.getMoney(was);
        BigDecimal nowM = MoneyUtils.getMoney(now);
        PriceInfo price = new PriceInfo("GBP", wasM, nowM);
        return ProductInfo.builder().priceInfo(price).build();
    }

}
