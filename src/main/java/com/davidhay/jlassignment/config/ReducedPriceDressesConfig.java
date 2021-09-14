package com.davidhay.jlassignment.config;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.formatter.PriceLabelFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Configuration
public class ReducedPriceDressesConfig {

    @Autowired
    PriceLabelFormatter labelFormatter;

    @Bean
    Predicate<ProductInfo> hasPriceReductionPredicate() {
        return ProductInfo::hasPriceReduction;
    }

    @Bean
    Comparator<ProductInfo> sortProductInfoByPriceReductionInPenceDescending() {
        return Comparator.comparing(ProductInfo::getPriceReductionAmountPence).reversed();
    }

    @Bean
    Function<LabelType, Consumer<ProductInfo>> productInfoLabeler() {
        return labelType -> (productInfo) -> {
            String priceLabel = labelFormatter.getPriceLabel(productInfo.getPriceInfo(), labelType);
            productInfo.setPriceLabel(priceLabel);
        };
    }

}
