package com.davidhay.jlassignment.service;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class ReducedPriceDressesService {

    public static final String PRODUCT_DRESSES = "dresses";

    private final ProductInfoService productInfoService;
    private final Predicate<ProductInfo> hasPriceReductionPredicate;
    private final Comparator<ProductInfo> sortProductInfoByPriceReductionInPenceDescending;
    private final Function<LabelType, Consumer<ProductInfo>> labelTypeToProductInfoLabeler;

    public ReducedPriceDressesService(ProductInfoService productInfoService,
                                      Predicate<ProductInfo> hasPriceReductionPredicate,
                                      Comparator<ProductInfo> sortProductInfoByPriceReductionInPenceDescending,
                                      Function<LabelType, Consumer<ProductInfo>> labelTypeToProductInfoLabeler) {
        this.productInfoService = productInfoService;
        this.hasPriceReductionPredicate = hasPriceReductionPredicate;
        this.labelTypeToProductInfoLabeler = labelTypeToProductInfoLabeler;
        this.sortProductInfoByPriceReductionInPenceDescending = sortProductInfoByPriceReductionInPenceDescending;
    }

    public List<ProductInfo> getReducedPriceDresses(LabelType labelType) {
        Consumer<ProductInfo> productInfoLabeler = labelTypeToProductInfoLabeler.apply(labelType);
        return productInfoService.getProductInfo(PRODUCT_DRESSES, hasPriceReductionPredicate, sortProductInfoByPriceReductionInPenceDescending, productInfoLabeler);
    }

}
